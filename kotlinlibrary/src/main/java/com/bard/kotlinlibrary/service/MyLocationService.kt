package com.bard.kotlinlibrary.service

import android.util.Log
import androidx.lifecycle.LifecycleService
import com.bard.kotlinlibrary.observer.MyLocationObserver

class MyLocationService : LifecycleService() {

    init{
        Log.e("TAG", "MyLocationService")
        val observer = MyLocationObserver(this)
        lifecycle.addObserver(observer)
    }

}