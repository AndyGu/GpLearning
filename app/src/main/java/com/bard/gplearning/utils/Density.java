package com.bard.gplearning.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

public class Density {
    private static float appDensity; //表示屏幕密度
    private static float appScaleDensity; //表示字体缩放比例，默认appDensity

    private static final float WIDTH = 360; //参考设备的宽，单位是dp 如果设置一个控件为屏幕宽度一半 则为180dp

    //需要在setContentView之前调用
    public static void setDensity(final Application application, Activity activity){
        //获取当前app的屏幕显示信息
        DisplayMetrics displayMetrics = application.getResources().getDisplayMetrics();
        if(appDensity == 0){
            //初始化赋值操作
            appDensity = displayMetrics.density;
            appScaleDensity = displayMetrics.scaledDensity;

            //添加字体变化监听回调
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    //判断字体发生更改，重新对scaleDensity进行赋值
                    if(newConfig != null && newConfig.fontScale > 0){
                        appScaleDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }

        //计算目标值 density，scaleDensity，densityDpi
        float targetDensity = displayMetrics.widthPixels / WIDTH; // 1080/360=3
        float targetScaleDensity = targetDensity * (appScaleDensity / appDensity);
        int targetDensityDpi = (int)(targetDensity * 160);

        //替换Activity的density scaleDensity，densityDpi
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        dm.density = targetDensity;
        dm.scaledDensity = targetScaleDensity;
        dm.densityDpi = targetDensityDpi;
    }
}
