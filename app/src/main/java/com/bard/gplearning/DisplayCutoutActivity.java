package com.bard.gplearning;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.DisplayCutoutCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class DisplayCutoutActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //1.设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //华为 小米 oppo vivo
        //1.判断手机厂商，2.判断手机是否有刘海，3.设置是否让内容区域延伸入刘海 4.设置控件是否避开刘海区域





        //2.判断手机是否为刘海屏
//        boolean hasDisolayCutout = hasDisplayCutout(window);
        boolean hasDisolayCutout = samsungHasDisplayCutout(this);
        if(hasDisolayCutout){
            //3 让内容区域延伸进刘海
            WindowManager.LayoutParams params = window.getAttributes();
            /**
             * 刘海屏三种展示模式
             * LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT 全屏模式下内容区下移，非全屏不受影响
             * LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES 允许内容区延伸进刘海区
             * LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER 不允许内容延伸进刘海区，无论是否为全屏模式
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            }
            window.setAttributes(params);

            //4.设置成沉浸式
            int flags = View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            int visibility = window.getDecorView().getSystemUiVisibility();
            visibility |= flags; //追加沉浸式设置
            window.getDecorView().setSystemUiVisibility(visibility);
        }

        setContentView(R.layout.activity_cutout);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) findViewById(R.id.tv_jump).getLayoutParams();
        layoutParams.topMargin += getStatusBarHeight(this);
        findViewById(R.id.tv_jump).setLayoutParams(layoutParams);
    }

    private boolean hasDisplayCutout(Window window){
        DisplayCutout displayCutout;
        View rootView = window.getDecorView();
        WindowInsets insets = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            insets = rootView.getRootWindowInsets(); //窗口下挫
        }
        if(insets != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P ) {
                displayCutout = insets.getDisplayCutout();
                if(displayCutout != null){
                    if(displayCutout.getBoundingRects() != null &&
                            displayCutout.getBoundingRects().size() > 0 &&
                            displayCutout.getSafeInsetTop() > 0){ //留海的高度
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean samsungHasDisplayCutout(Context context){
        boolean mHasDisplayCutout = false;
        try {
            final Resources res = context.getResources();
            final int resId = res.getIdentifier("config_mainBuiltInDisplayCutout", "string", "android");
            final String spec = resId > 0 ? res.getString(resId): null;
            mHasDisplayCutout = spec != null && !TextUtils.isEmpty(spec);
        } catch (Exception e) {
            Log.w("samsungHasDisplayCutout", "Can not update hasDisplayCutout. " + e.toString());
        }
        return mHasDisplayCutout;
    }

    //通常情况下，留海的高就是状态栏的高，
    public static int getStatusBarHeight(Context context){
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if(resId > 0){
            return context.getResources().getDimensionPixelSize(resId);
        }
        return 0;
    }


}
