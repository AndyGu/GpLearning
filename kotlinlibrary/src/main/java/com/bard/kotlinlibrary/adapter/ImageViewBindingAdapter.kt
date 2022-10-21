package com.bard.kotlinlibrary.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bard.kotlinlibrary.R
import com.squareup.picasso.Picasso

class ImageViewBindingAdapter {

    companion object{
        @JvmStatic
        @BindingAdapter("myImage")
        fun setImage(img: ImageView, url: String){
            if(url.isNotEmpty()){
                Picasso.get()
                    .load(url)
                    .placeholder(R.drawable.sad)
                    .into(img)
            }else{
                img.setBackgroundResource(R.color.cardview_shadow_start_color)
            }
        }

        @JvmStatic
        @BindingAdapter("myImage")
        fun setImage(img: ImageView, resource: Int){
            img.setBackgroundResource(resource)
        }


        @JvmStatic
        @BindingAdapter(value = ["imageUrl", "defaultResId"], requireAll = false)
        fun setImage(img: ImageView, url : String, resource: Int){
            if(url.isNotEmpty()){
                Picasso.get()
                    .load(url)
                    .placeholder(R.drawable.sad)
                    .into(img)
            }else{
                img.setBackgroundResource(resource)
            }
        }
    }
}