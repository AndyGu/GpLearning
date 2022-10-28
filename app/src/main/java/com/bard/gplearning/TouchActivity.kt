package com.bard.gplearning

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_touch.*
import kotlin.concurrent.thread
import kotlin.random.Random

class TouchActivity: AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_touch)

        thread {
            img.setBackgroundResource(R.color.colorAccent)
            textview.text = "thread = ${Thread.currentThread().name}"
        }

        textview.setOnClickListener{
            it.requestLayout()
            img.requestLayout()

            thread {
                textview.text = "${Thread.currentThread().name} - ${SystemClock.uptimeMillis()}"

                val color: Color = Color.valueOf(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1f)
                img.setBackgroundColor(color.toArgb())
            }
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        Log.e("TouchActivity", "dispatchTouchEvent1")
        val returnValue = super.dispatchTouchEvent(event)
        Log.e("TouchActivity", "dispatchTouchEvent2 returnValue = $returnValue event=$event")
        return returnValue
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.e("TouchActivity", "onTouchEvent1")
        val returnValue = super.onTouchEvent(event)
        Log.e("TouchActivity", "onTouchEvent2 returnValue = $returnValue event=$event")
        return returnValue
    }
}


/**
 * MyTouchViewA MyTouchViewB not clickable 
 */
//TouchActivity: dispatchTouchEvent1
//MyTouchViewGroupA: dispatchTouchEvent1
//MyTouchViewGroupA: onInterceptTouchEvent1
//MyTouchViewGroupA: onInterceptTouchEvent2 returnValue = false event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=183.51562, y[0]=859.21094, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=47241855, downTime=47241855, deviceId=2, source=0x1002, displayId=0, eventId=388097623 }
//MyTouchViewGroupB: dispatchTouchEvent1
//MyTouchViewGroupB: onInterceptTouchEvent1
//MyTouchViewGroupB: onInterceptTouchEvent2 returnValue = false event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=183.51562, y[0]=210.21094, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=47241855, downTime=47241855, deviceId=2, source=0x1002, displayId=0, eventId=388097623 }
//MyTouchViewB: dispatchTouchEvent1
//MyTouchViewB: onTouchEvent1
//MyTouchViewB: onTouchEvent2 returnValue = false event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=183.51562, y[0]=210.21094, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=47241855, downTime=47241855, deviceId=2, source=0x1002, displayId=0, eventId=388097623 }
//MyTouchViewB: dispatchTouchEvent2 returnValue = false event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=183.51562, y[0]=210.21094, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=47241855, downTime=47241855, deviceId=2, source=0x1002, displayId=0, eventId=388097623 }
//MyTouchViewA: dispatchTouchEvent1
//MyTouchViewA: onTouchEvent1
//MyTouchViewA: onTouchEvent2 returnValue = false event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=183.51562, y[0]=210.21094, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=47241855, downTime=47241855, deviceId=2, source=0x1002, displayId=0, eventId=388097623 }
//MyTouchViewA: dispatchTouchEvent2 returnValue = false event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=183.51562, y[0]=210.21094, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=47241855, downTime=47241855, deviceId=2, source=0x1002, displayId=0, eventId=388097623 }
//MyTouchViewGroupB: onTouchEvent1
//MyTouchViewGroupB: onTouchEvent2 returnValue = false event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=183.51562, y[0]=210.21094, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=47241855, downTime=47241855, deviceId=2, source=0x1002, displayId=0, eventId=388097623 }
//MyTouchViewGroupB: dispatchTouchEvent2 returnValue = false event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=183.51562, y[0]=210.21094, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=47241855, downTime=47241855, deviceId=2, source=0x1002, displayId=0, eventId=388097623 }
//MyTouchViewGroupA: onTouchEvent1
//MyTouchViewGroupA: onTouchEvent2 returnValue = false event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=183.51562, y[0]=859.21094, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=47241855, downTime=47241855, deviceId=2, source=0x1002, displayId=0, eventId=388097623 }
//MyTouchViewGroupA: dispatchTouchEvent2 returnValue = false event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=183.51562, y[0]=859.21094, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=47241855, downTime=47241855, deviceId=2, source=0x1002, displayId=0, eventId=388097623 }
//TouchActivity: onTouchEvent1
//TouchActivity: onTouchEvent2 returnValue = false event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=183.51562, y[0]=1001.21094, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=47241855, downTime=47241855, deviceId=2, source=0x1002, displayId=0, eventId=388097623 }
//TouchActivity: dispatchTouchEvent2 returnValue = false event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=183.51562, y[0]=1001.21094, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=47241855, downTime=47241855, deviceId=2, source=0x1002, displayId=0, eventId=388097623 }


/**
 * MyTouchViewA MyTouchViewB all clickable
 */
//TouchActivity: dispatchTouchEvent1
//MyTouchViewGroupA: dispatchTouchEvent1
//MyTouchViewGroupA: onInterceptTouchEvent1
//MyTouchViewGroupA: onInterceptTouchEvent2 returnValue = false event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=170.15625, y[0]=869.60156, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=50001109, downTime=50001109, deviceId=2, source=0x1002, displayId=0, eventId=833526265 }
//MyTouchViewGroupB: dispatchTouchEvent1
//MyTouchViewGroupB: onInterceptTouchEvent1
//MyTouchViewGroupB: onInterceptTouchEvent2 returnValue = false event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=170.15625, y[0]=220.60156, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=50001109, downTime=50001109, deviceId=2, source=0x1002, displayId=0, eventId=833526265 }
//MyTouchViewB: dispatchTouchEvent1
//MyTouchViewB: onTouchEvent1
//MyTouchViewB: onTouchEvent2 returnValue = true event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=170.15625, y[0]=220.60156, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=50001109, downTime=50001109, deviceId=2, source=0x1002, displayId=0, eventId=833526265 }
//MyTouchViewB: dispatchTouchEvent2 returnValue = true event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=170.15625, y[0]=220.60156, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=50001109, downTime=50001109, deviceId=2, source=0x1002, displayId=0, eventId=833526265 }
//MyTouchViewGroupB: dispatchTouchEvent2 returnValue = true event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=170.15625, y[0]=220.60156, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=50001109, downTime=50001109, deviceId=2, source=0x1002, displayId=0, eventId=833526265 }
//MyTouchViewGroupA: dispatchTouchEvent2 returnValue = true event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=170.15625, y[0]=869.60156, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=50001109, downTime=50001109, deviceId=2, source=0x1002, displayId=0, eventId=833526265 }
//TouchActivity: dispatchTouchEvent2 returnValue = true event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=170.15625, y[0]=1011.60156, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=50001109, downTime=50001109, deviceId=2, source=0x1002, displayId=0, eventId=833526265 }

/**
 * MyTouchViewA MyTouchViewB MyTouchViewGroupB all clickable 
 * 与上一种没区别
 */
//TouchActivity: dispatchTouchEvent1
//MyTouchViewGroupA: dispatchTouchEvent1
//MyTouchViewGroupA: onInterceptTouchEvent1
//MyTouchViewGroupA: onInterceptTouchEvent2 returnValue = false event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=251.01562, y[0]=844.3672, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=50507835, downTime=50507835, deviceId=2, source=0x1002, displayId=0, eventId=601783764 }
//MyTouchViewGroupB: dispatchTouchEvent1
//MyTouchViewGroupB: onInterceptTouchEvent1
//MyTouchViewGroupB: onInterceptTouchEvent2 returnValue = false event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=251.01562, y[0]=195.36719, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=50507835, downTime=50507835, deviceId=2, source=0x1002, displayId=0, eventId=601783764 }
//MyTouchViewB: dispatchTouchEvent1
//MyTouchViewB: onTouchEvent1
//MyTouchViewB: onTouchEvent2 returnValue = true event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=251.01562, y[0]=195.36719, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=50507835, downTime=50507835, deviceId=2, source=0x1002, displayId=0, eventId=601783764 }
//MyTouchViewB: dispatchTouchEvent2 returnValue = true event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=251.01562, y[0]=195.36719, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=50507835, downTime=50507835, deviceId=2, source=0x1002, displayId=0, eventId=601783764 }
//MyTouchViewGroupB: dispatchTouchEvent2 returnValue = true event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=251.01562, y[0]=195.36719, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=50507835, downTime=50507835, deviceId=2, source=0x1002, displayId=0, eventId=601783764 }
//MyTouchViewGroupA: dispatchTouchEvent2 returnValue = true event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=251.01562, y[0]=844.3672, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=50507835, downTime=50507835, deviceId=2, source=0x1002, displayId=0, eventId=601783764 }
//TouchActivity: dispatchTouchEvent2 returnValue = true event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=251.01562, y[0]=986.3672, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=50507835, downTime=50507835, deviceId=2, source=0x1002, displayId=0, eventId=601783764 }






//TouchActivity: dispatchTouchEvent1
//MyTouchViewGroupA: dispatchTouchEvent1
//MyTouchViewGroupA: onInterceptTouchEvent1
//MyTouchViewGroupA: onInterceptTouchEvent2 returnValue = false event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=188.4375, y[0]=833.2344, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=50858210, downTime=50858210, deviceId=2, source=0x1002, displayId=0, eventId=36424817 }
//MyTouchViewGroupB: dispatchTouchEvent1
//MyTouchViewGroupB: onInterceptTouchEvent1
//MyTouchViewGroupB: onInterceptTouchEvent2 returnValue = false event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=188.4375, y[0]=184.23438, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=50858210, downTime=50858210, deviceId=2, source=0x1002, displayId=0, eventId=36424817 }
//MyTouchViewB: dispatchTouchEvent1
//MyTouchViewB: onTouchEvent1
//MyTouchViewB: onTouchEvent2 returnValue = true event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=188.4375, y[0]=184.23438, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=50858210, downTime=50858210, deviceId=2, source=0x1002, displayId=0, eventId=36424817 }
//MyTouchViewB: dispatchTouchEvent2 returnValue = true event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=188.4375, y[0]=184.23438, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=50858210, downTime=50858210, deviceId=2, source=0x1002, displayId=0, eventId=36424817 }
//MyTouchViewGroupB: dispatchTouchEvent2 returnValue = true event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=188.4375, y[0]=184.23438, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=50858210, downTime=50858210, deviceId=2, source=0x1002, displayId=0, eventId=36424817 }
//MyTouchViewGroupA: dispatchTouchEvent2 returnValue = true event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=188.4375, y[0]=833.2344, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=50858210, downTime=50858210, deviceId=2, source=0x1002, displayId=0, eventId=36424817 }
//TouchActivity: dispatchTouchEvent2 returnValue = true event=MotionEvent { action=ACTION_DOWN, actionButton=0, id[0]=0, x[0]=188.4375, y[0]=975.2344, toolType[0]=TOOL_TYPE_FINGER, buttonState=0, classification=NONE, metaState=0, flags=0x0, edgeFlags=0x0, pointerCount=1, historySize=0, eventTime=50858210, downTime=50858210, deviceId=2, source=0x1002, displayId=0, eventId=36424817 }



