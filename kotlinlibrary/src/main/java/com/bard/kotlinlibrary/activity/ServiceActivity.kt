package com.bard.kotlinlibrary.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bard.kotlinlibrary.R
import com.bard.kotlinlibrary.interfaces.OnLocationChange
import com.bard.kotlinlibrary.observer.MyLocationObserver
import com.bard.kotlinlibrary.service.MyLocationService
import kotlinx.android.synthetic.main.activity_service.*

class ServiceActivity : AppCompatActivity(), OnLocationChange {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)

        my_btn_start.setOnClickListener{
            startGps()
        }

        my_btn_stop.setOnClickListener {
            stopGps()
        }
    }

    private fun startGps(){
        MyLocationObserver.onLocationChange = this
        startService(Intent(this, MyLocationService::class.java))
    }

    private fun stopGps(){
        MyLocationObserver.onLocationChange = null
        stopService(Intent(this, MyLocationService::class.java))
    }

    override fun onLocationChange(longitude: Double, latitude: Double) {
        Log.e("ServiceActivity","onLocationChange longitude=$longitude   latitude=$latitude")
        tv_location.text = "longitude=$longitude   latitude=$latitude"
    }
}