package com.bard.gplearning.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class LocalBindService extends Service {

    LocalBinder localBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        LocalBinder getService(){
            return LocalBinder.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }
}
