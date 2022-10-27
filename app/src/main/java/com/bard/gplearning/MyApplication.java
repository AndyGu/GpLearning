package com.bard.gplearning;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.bard.base.autoservice.BaseApplication;
import com.bard.gplearning.utils.Density;
import com.bard.webview.WebViewActivity;
import com.kingja.loadsir.core.LoadSir;

import loadsir.CustomCallback;
import loadsir.EmptyCallback;
import loadsir.ErrorCallback;
import loadsir.LoadingCallback;
import loadsir.TimeoutCallback;


public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Density.setDensity(MyApplication.this, activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.e("onActivityStarted", "activity="+activity);
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.e("onActivityResumed", "activity="+activity);
                if(activity instanceof WebViewActivity){
                    Log.e("onActivityResumed", "WebViewActivity Resumed");
                    ((WebViewActivity)activity).testToast("xxoo");
                } else if(activity instanceof FourthActivity){
                    Log.e("onActivityResumed", "FourthActivity Resumed");
                    ((FourthActivity)activity).testToast("xixi");
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .addCallback(new TimeoutCallback())
                .addCallback(new CustomCallback())
                .setDefaultCallback(LoadingCallback.class)
                .commit();


//        NetworkManager.getInstance().init(this);

//        ARouter.getInstance().init(this);
    }
}
