package com.bard.gplearning.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout

class MyTouchViewGroupA(context: Context, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.e("MyTouchViewGroupA", "onTouchEvent1")
        val returnValue = super.onTouchEvent(event)
        Log.e("MyTouchViewGroupA", "onTouchEvent2 returnValue = $returnValue event=$event")
        return returnValue
    }


    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        Log.e("MyTouchViewGroupA", "dispatchTouchEvent1")
        val returnValue = super.dispatchTouchEvent(event)
        Log.e("MyTouchViewGroupA", "dispatchTouchEvent2 returnValue = $returnValue event=$event")
        return returnValue
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.e("MyTouchViewGroupA", "onInterceptTouchEvent1")
        val returnValue = super.onInterceptTouchEvent(ev)
        Log.e("MyTouchViewGroupA", "onInterceptTouchEvent2 returnValue = $returnValue event=$ev")
        return returnValue
    }


}