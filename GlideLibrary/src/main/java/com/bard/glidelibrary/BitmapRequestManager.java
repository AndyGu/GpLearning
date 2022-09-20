package com.bard.glidelibrary;

import java.util.concurrent.LinkedBlockingQueue;

//图片加载管理大厅
public class BitmapRequestManager {
    private static BitmapRequestManager bitmapRequestManager = new BitmapRequestManager();

    public static BitmapRequestManager getInstance(){
        if(bitmapRequestManager == null){
            synchronized (BitmapRequestManager.class){
                if(bitmapRequestManager == null){
                    bitmapRequestManager = new BitmapRequestManager();
                }
            }
        }
        return bitmapRequestManager;
    }


    //创建队列
    private LinkedBlockingQueue<BitmapRequest> requestQueue = new LinkedBlockingQueue<>();

    //创建线程数组
    private BitmapDispatcher[] bitmapDispatchers;


    private BitmapRequestManager(){
        start();
    }

    public void start(){
        stop();
        startAllDispatcher();
    }


    public void addBitmapRequest(BitmapRequest bitmapRequest){
        if(bitmapRequest == null){
            return;
        }
        if(!requestQueue.contains(bitmapRequest)){
            requestQueue.add(bitmapRequest);
        }
    }


    //提醒窗口开始服务
    public void startAllDispatcher(){
        //获取手机支持的单个应用最大的线程数
        int threadCount = Runtime.getRuntime().availableProcessors();

        bitmapDispatchers = new BitmapDispatcher[threadCount];

        for(int i=0; i<threadCount; i++){
            BitmapDispatcher bitmapDispatcher = new BitmapDispatcher(requestQueue);
            bitmapDispatcher.start();

            //将每一个dispatcher放到数组中，方便统一管理
            bitmapDispatchers[i] = bitmapDispatcher;
        }
    }


    //停止所有线程
    public void stop(){
        if(bitmapDispatchers != null &&
                bitmapDispatchers.length > 0){
            for(BitmapDispatcher bitmapDispatcher : bitmapDispatchers){
                if(!bitmapDispatcher.isInterrupted()){
                    bitmapDispatcher.interrupt();
                }
            }
        }
    }

}
