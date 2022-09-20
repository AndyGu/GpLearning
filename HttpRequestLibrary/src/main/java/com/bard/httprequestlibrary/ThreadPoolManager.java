package com.bard.httprequestlibrary;


import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//线程池管理队列
public class ThreadPoolManager {
    private static ThreadPoolManager threadPoolManager = new ThreadPoolManager();


    public static ThreadPoolManager getInstance(){
        return threadPoolManager;
    }


    //创建一个线程任务的队列
    private LinkedBlockingQueue<Runnable> mQueue = new LinkedBlockingQueue<>();


    //创建一个延迟任务队列
    private DelayQueue<HttpTask> mDelayQueue = new DelayQueue<>();

    public void addDelayTask(HttpTask ht){
        if(ht != null){
            ht.setDelayTime(3000);
            mDelayQueue.offer(ht);
        }
    }

    //将异步任务添加到队列中
    public void addTask(Runnable runnable){

        if(runnable != null){
            try {
                mQueue.put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //创建线程池
    private ThreadPoolExecutor mThreadPoolExecutor;

    private ThreadPoolManager(){
//        int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        mThreadPoolExecutor = new ThreadPoolExecutor(3, 10, 15, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
                //假如被拒（线程池不够用等原因），尝试重新添加到队列中
                addTask(runnable);
            }
        });

        mThreadPoolExecutor.execute(coreThread);
        mThreadPoolExecutor.execute(delayThread);
    }


    //创建核心线程(叫号员)，去队列中获取请求，提交给线程池处理
    public Runnable coreThread = new Runnable() {
        Runnable runnable = null;
        @Override
        public void run() {
            while(true){
                try {
                    runnable = mQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mThreadPoolExecutor.execute(runnable);
            }
        }
    };


    //另一个叫号员
    public Runnable delayThread = new Runnable() {
        @Override
        public void run() {
            HttpTask ht;
            while(true){
                //判断当前网络是否重新连接成功，成功才执行以下代码

                try{
                    ht = mDelayQueue.take();

                    if(ht.getRetryCount() < 3){
                        mThreadPoolExecutor.execute(ht);
                        ht.setRetryCount(ht.getRetryCount() + 1);
                        Log.e("重试机制",ht.getRetryCount()+" "+System.currentTimeMillis());
                    }else{
                        Log.e("重试机制", "重试次数过多，直接放弃");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    };

}
