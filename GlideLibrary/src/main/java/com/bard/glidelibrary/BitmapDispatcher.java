package com.bard.glidelibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;

//消费窗口、bitmaprequest分发器
public class BitmapDispatcher extends Thread {

    private Handler handler = new Handler(Looper.getMainLooper());

    //创建一个阻塞队列
    private LinkedBlockingQueue<BitmapRequest> requestQueue;

    public BitmapDispatcher(LinkedBlockingQueue requestQueue){
        this.requestQueue = requestQueue;
    }

    @Override
    public void run() {
        super.run();
        while(!isInterrupted()){
            try{
                //从队列中获取图片请求
                BitmapRequest br = requestQueue.take();
                //设置占位图片
                showLoadingImage(br);
                //加载图片
                Bitmap bitmap= findBitmap(br);
                //将图片显示到ImageView
                showImageView(br, bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void showLoadingImage(BitmapRequest br){
        Log.e("showLoadingImage","showLoadingImage="+br.getUrl());
        if(br.getResID() > 0 && br.getImageView() != null){
            final int resId = br.getResID();
            final ImageView imageView = br.getImageView();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageResource(resId);
                }
            });
        }
    }

    private Bitmap findBitmap(BitmapRequest br){
        Log.e("findBitmap","url="+br.getUrl());
        Bitmap bitmap = downloadBitmap(br.getUrl());
        return bitmap;
    }

    private Bitmap downloadBitmap(String url){
        FileOutputStream fos = null;
        InputStream is = null;
        Bitmap bitmap = null;

        try {
            Log.e("downloadBitmap","url="+url);
            //创建一个URL对象
            URL mUrl = new URL(url);
            //然后使用httpURLConnection通过URL去开始读数据
            HttpURLConnection connection = (HttpURLConnection)mUrl.openConnection();
            is = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        }catch (Exception e){
            e.printStackTrace();
        }finally {

            try{
                if(is != null){
                    is.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }

            try{
                if(fos != null){
                    fos.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return bitmap;
    }


    private void showImageView(final BitmapRequest br, final Bitmap bitmap){
        Log.e("showImageView","bitmap="+bitmap);
        if(bitmap != null && br.getImageView() != null &&
                MD5Util.encryptMd5(br.getUrl()).equals(br.getImageView().getTag())){
            final ImageView imageView = br.getImageView();
            handler.post(new Runnable() {
                @Override
                public void run() {
                  imageView.setImageBitmap(bitmap);
                  if(br.getRequestListener() != null){
                      RequestListener listener = br.getRequestListener();
                      listener.onSuccess(bitmap);
                  }
                }
            });
        }
    }


}
