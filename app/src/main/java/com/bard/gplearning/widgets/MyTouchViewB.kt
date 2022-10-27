package com.bard.gplearning.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class MyTouchViewB(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.e("MyTouchViewB", "onTouchEvent1")
        val returnValue = super.onTouchEvent(event)
        Log.e("MyTouchViewB", "onTouchEvent2 returnValue = $returnValue event=$event")
        return returnValue
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        Log.e("MyTouchViewB", "dispatchTouchEvent1")
        val returnValue = super.dispatchTouchEvent(event)
        Log.e("MyTouchViewB", "dispatchTouchEvent2 returnValue = $returnValue event=$event")
        return returnValue
    }
}