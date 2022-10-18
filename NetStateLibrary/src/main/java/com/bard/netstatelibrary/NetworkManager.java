package com.bard.netstatelibrary;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Build;

import com.bard.netstatelibrary.core.NetworkCallbackImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;


//
public class NetworkManager {

    private static volatile NetworkManager networkManager;
    private Application application;
    private NetStateReceiver receiver;


    public NetworkManager() {
        this.receiver = new NetStateReceiver();
    }

//    public void setListener(NetChangeObserver listener){
//        receiver.setListener(listener);
//    }

    public static NetworkManager getInstance(){
        if(networkManager == null){
            synchronized (NetworkManager.class){
                if(networkManager == null){
                    networkManager = new NetworkManager();
                }
            }
        }
        return networkManager;
    }

    public Application getApplication() {
        return application;
    }


    public void init(Application application){
        this.application = application;

        //做动态广播注册
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Constants.ANDROID_NET_CHANGE_ACTION);
//        application.registerReceiver(receiver, intentFilter);


        //第二种方式监听，不通过广播
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ConnectivityManager.NetworkCallback networkCallback = new NetworkCallbackImpl();

            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            NetworkRequest request = builder.build();
            ConnectivityManager connectivityManager = (ConnectivityManager)NetworkManager.getInstance().getApplication()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivityManager != null){
                connectivityManager.registerNetworkCallback(request, networkCallback);
            }
        }
    }

    //注册
    public void registerObserver(Object register){
        receiver.registerObserver(register);
    }

    //移除
    public void unRegisterObserver(Object register){
        receiver.unRegisterObserver(register);
    }

    //退出
    public void removeAllObserver(Object register){
        receiver.removeAllObserver(register);
    }
}
