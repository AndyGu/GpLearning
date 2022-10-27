package com.bard.gplearning.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout

class MyTouchViewGroupB(context: Context, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.e("MyTouchViewGroupB", "onTouchEvent1")
        val returnValue = super.onTouchEvent(event)
        Log.e("MyTouchViewGroupB", "onTouchEvent2 returnValue = $returnValue event=$event")
        return returnValue
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        Log.e("MyTouchViewGroupB", "dispatchTouchEvent1")
        val returnValue = super.dispatchTouchEvent(event)
        Log.e("MyTouchViewGroupB", "dispatchTouchEvent2 returnValue = $returnValue event=$event")
        return returnValue
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.e("MyTouchViewGroupB", "onInterceptTouchEvent1")
        val returnValue = super.onInterceptTouchEvent(ev)
        Log.e("MyTouchViewGroupB", "onInterceptTouchEvent2 returnValue = $returnValue event=$ev")
        return returnValue
    }
}