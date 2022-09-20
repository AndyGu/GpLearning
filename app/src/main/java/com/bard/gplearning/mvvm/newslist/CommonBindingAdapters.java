package com.bard.gplearning.mvvm.newslist;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

public class CommonBindingAdapters {
    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String url){
        if(!TextUtils.isEmpty(url)){
            //加载图片
            Log.e("CommonBindingAdapters", "加载图片 url="+url);
        }
    }

    @BindingAdapter("android:visibility")
    public static void setVisibility(View view, Boolean value){
        view.setVisibility(value ? View.VISIBLE : View.GONE);
    }
}
