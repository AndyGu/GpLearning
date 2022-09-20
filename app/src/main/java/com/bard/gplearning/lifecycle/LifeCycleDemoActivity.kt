package com.bard.gplearning.lifecycle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.bard.gplearning.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LifeCycleDemoActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_component)
        lifecycle.addObserver(MyObserver())

        GlobalScope.launch {  }
    }


    class MyObserver : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
        fun onAny(){

        }
    }
}