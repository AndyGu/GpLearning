package com.bard.kotlinlibrary.widgets

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.widget.Chronometer
import androidx.lifecycle.*

class MyChronometer: Chronometer, LifecycleEventObserver  {
    constructor(context: Context): this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?): this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int): super(context, attributeSet, defStyleAttr)

    private var elapsedTime : Long = 0

    private fun startMeter(){
        Log.e("TAG", "startMeter: ")
        base = SystemClock.elapsedRealtime() - elapsedTime
        start()
    }

    private fun stopMeter(){
        Log.e("TAG", "stopMeter: ")
        // 由于Chronometer的stop方法调用，也不会停止内部的计时器在走，所以我们想要只在onResume计时的需求就需要感知生命周期
        // base用于记录每次开启的时间点，elapsedTime则记录每次暂停的时间点
        // 每次开启的时候，需要重新设置base
        elapsedTime = SystemClock.elapsedRealtime() - base
        stop()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when(event){
            Lifecycle.Event.ON_RESUME -> startMeter()
            Lifecycle.Event.ON_PAUSE -> stopMeter()
            else -> {}
        }
    }
}