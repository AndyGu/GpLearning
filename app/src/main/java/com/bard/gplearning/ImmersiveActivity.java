package com.bard.gplearning;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ImmersiveActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_immersive);



//        一、Android4.4的处理方式（4.4上只能设置成带阴影的沉浸式，而这种方式也能使用在4.4以上的版本）
//        1.主题方式
//          <item name="android:windowTranslucentStatus">true</item>
//          <item name="android:windowTranslucentNavigation">true</item>

//        2.代码方式
//          getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//          getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);


//        二、Android5.0及以上的处理方式
//          android5.0及以上可以设置状态栏的颜色，例如通过
//          <item name="android:statusBarColor">#ff0000</item> //设置状态栏颜色
//          而如果把这个颜色值设置成透明，并不能达到沉浸式效果，必须要对窗口进行设置：
//
//
//        private void immersive(){
//            //只能在4.4上实现沉浸式
//            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
//                return;
//            }
//
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//                Window window = getWindow();
//                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                window.setStatusBarColor(Color.TRANSPARENT);
//
//                int visibility = window.getDecorView().getSystemUiVisibility();
//                //布局全屏展示
//                visibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
//                //方式内容区域大小发生变化
//                visibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//                //隐藏底部虚拟导航栏
////            visibility |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//                window.getDecorView().setSystemUiVisibility(visibility);
//                //！！！这样设置的状态栏是没有阴影效果的
//            }else{
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            }
//        }
//
//
////获取状态栏高度
//        public int getStatusBarHeight(Context context){
//            int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
//            if(resId > 0){
//                return context.getResources().getDimensionPixelSize(resId);
//            }
//            return 0;
//        }
//
//
////给头部布局增加高度以及padding
//        public void setHeightAndPadding(Context context, View view){
//            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
//            layoutParams.height += getStatusBarHeight(context);
//            view.setPadding(view.getPaddingLeft(), view.getTop()+getStatusBarHeight(context), view.getPaddingRight(), view.getPaddingBottom());
//        }
//
//
//        但是注意，这种代码方式设置之后，沉浸式的状态栏是没有阴影效果的，整个状态栏是透明状态，如果还想要4.4的沉浸式状态栏状态，需要使用
//        主题方式 + 顶层布局的属性配合设置的方式，才能实现。
//
//        android:background="#00ffff"
//        android:fitsSystemWindows="true"




        immersive();
//        setHeightAndPadding(this, findViewById(R.id.imageview));

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }


    private void immersive(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
            //低于4.4版本无法实现沉浸式
            return;
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

            int visibility = window.getDecorView().getSystemUiVisibility();
            //布局全屏展示
            visibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            //方式内容区域大小发生变化
            visibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            //隐藏底部虚拟导航栏
//            visibility |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            window.getDecorView().setSystemUiVisibility(visibility);
            //这样设置的状态栏是没有阴影效果的
        }else{
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    public int getStatusBarHeight(Context context){
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if(resId > 0){
            return context.getResources().getDimensionPixelSize(resId);
        }
        return 0;
    }



    public void setHeightAndPadding(Context context, View view){
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height += getStatusBarHeight(context);
        view.setPadding(view.getPaddingLeft(), view.getTop()+getStatusBarHeight(context), view.getPaddingRight(), view.getPaddingBottom());
    }
}
