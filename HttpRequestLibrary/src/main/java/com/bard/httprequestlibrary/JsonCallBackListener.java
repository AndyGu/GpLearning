package com.bard.httprequestlibrary;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonCallBackListener<T> implements CallBackListener{

    private Class<T> responseClass;
    private IJsonDataTransformListener<T> listener;
    private Handler handler = new Handler(Looper.getMainLooper());

    public JsonCallBackListener(Class<T> responseClass, IJsonDataTransformListener listener) {
        this.responseClass = responseClass;
        this.listener = listener;
    }


    @Override
    public void onSuccess(InputStream inputStream) {
        //将流转换成String
        String response = getContent(inputStream);
        //将String转换成对应的class对象
        final T clazz = JSON.parseObject(response, responseClass);
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onSuccess(clazz);
            }
        });
    }


    private String getContent(InputStream inputStream){
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder sb = new StringBuilder();
            String line;
            try{
                while ((line = reader.readLine()) != null){
                    sb.append(line +"\n");
                }
            }catch (IOException e){
                System.out.println("error="+e.toString());
            }finally {
                try{
                    inputStream.close();
                }catch (IOException e){
                    System.out.println("error="+e.toString());
                }
            }
            return sb.toString();

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onFailure() {

    }
}
