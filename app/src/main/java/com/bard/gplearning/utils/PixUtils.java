package com.bard.gplearning.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class PixUtils {
    private static PixUtils utils;

    //设计稿参考的宽高
    private static final float STANDARD_WIDTH = 1080;
    private static final float STANDARD_HEIGHT = 1920;

    //这里是屏幕显示宽高
    private int mDisplayWidth;
    private int mDisplayHeight;

    private PixUtils(Context context){
        //获取屏幕的宽高
        if(mDisplayWidth == 0 || mDisplayHeight == 0){
            WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            if(manager != null){
                DisplayMetrics displayMetrics = new DisplayMetrics();
                manager.getDefaultDisplay().getMetrics(displayMetrics);
                if(displayMetrics.widthPixels > displayMetrics.heightPixels){
                    //横屏
                    mDisplayWidth = displayMetrics.heightPixels  - getStatusBarHeight(context);
                    mDisplayHeight = displayMetrics.widthPixels;
                }else{
                    mDisplayWidth = displayMetrics.widthPixels;
                    mDisplayHeight = displayMetrics.heightPixels - getStatusBarHeight(context);
                }

            }

        }
    }


    public static PixUtils getInstance(Context context){
        if(utils == null){
            synchronized (PixUtils.class){
                if(utils == null){
                    utils = new PixUtils(context.getApplicationContext());
                }
            }
        }
        return utils;
    }


    //获得水平方向上的缩放比例
    public float getHorizontalScale(){
        return mDisplayWidth / STANDARD_WIDTH;
    }

    //获得垂直方向上的缩放比例
    public float getVerticalScale(){
        return mDisplayHeight / STANDARD_HEIGHT;
    }

    public static int getStatusBarHeight(Context context){
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if(resId > 0){
            return context.getResources().getDimensionPixelSize(resId);
        }
        return 0;
    }
}
