package com.bard.gplearning.lifecycle;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class LocationHelper implements LifecycleObserver {
//    Service中添加监听【Service继承自LifecycleService】
//    public MyService() {
//        LocationHelper locationHelper = new LocationHelper();
//        getLifecycle().addObserver(locationHelper);
//    }
    private static final String TAG = "LocationHelper";
    /**
     * 开始定位
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void startLocation(){
        Log.e(TAG,"开始定位");
    }

    /**
     * 停止定位
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void stopLocation(){
        Log.e(TAG,"停止定位");
    }
}
