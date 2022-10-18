package com.bard.kotlinlibrary.observer

import com.bard.kotlinlibrary.interfaces.OnLocationChange
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

class MyLocationObserver(context: Context) : LifecycleEventObserver {
    private val mContext = context
    private var locationManager : LocationManager? = null
    private lateinit var myLocationListener: MyLocationListener

    companion object {
        var onLocationChange: OnLocationChange? = null
    }

    private fun startLocate(){
        Log.e("MyLocationObserver","startLocate")
        locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        myLocationListener =  MyLocationListener()
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1f, myLocationListener)
    }


    private fun stopLocate(){
        Log.e("MyLocationObserver","stopLocate")
        locationManager?.removeUpdates(myLocationListener)
//        myLocationListener.let { locationManager?.removeUpdates(it) }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when(event){
            Lifecycle.Event.ON_CREATE -> startLocate()
            Lifecycle.Event.ON_DESTROY -> stopLocate()
            else -> {}
        }
    }

    class MyLocationListener : LocationListener{
        override fun onLocationChanged(location: Location) {
            Log.e("onLocationChanged", "onLocationChanged=$location.toString()")
            onLocationChange?.onLocationChange(location.latitude, location.longitude)
        }
    }
}