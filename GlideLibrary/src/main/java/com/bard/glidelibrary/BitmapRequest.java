package com.bard.glidelibrary;

import android.content.Context;
import android.widget.ImageView;


import java.lang.ref.SoftReference;

//加载图片请求
public class BitmapRequest {
    //图片请求地址
    private String url;

    //图片加载控件
    private SoftReference<ImageView> imageView;

    //占位图片
    private int resID;

    //回调对象
    private RequestListener requestListener;

    //图片标识
    private String urlMd5;

    private Context context;

    public BitmapRequest(Context context){
        this.context = context;
    }


    public BitmapRequest load(String url){
        this.url = url;
        this.urlMd5 = MD5Util.encryptMd5(url);
        return this;
    }


    public BitmapRequest placeHolder(int resID){
        this.resID = resID;
        return this;
    }

    public BitmapRequest listener(RequestListener listener){
        this.requestListener = listener;
        return this;
    }

    public void into(ImageView imageView){
        imageView.setTag(urlMd5);
        this.imageView = new SoftReference<>(imageView);
        //将图片请求添加入队列中
        BitmapRequestManager.getInstance().addBitmapRequest(this);
    }


    public String getUrl() {
        return url;
    }

    public ImageView getImageView() {
        return imageView.get();
    }

    public int getResID() {
        return resID;
    }

    public RequestListener getRequestListener() {
        return requestListener;
    }

    public String getUrlMd5() {
        return urlMd5;
    }

    public Context getContext() {
        return context;
    }
}
