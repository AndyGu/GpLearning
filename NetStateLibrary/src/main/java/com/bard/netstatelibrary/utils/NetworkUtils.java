package com.bard.netstatelibrary.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.bard.netstatelibrary.NetworkManager;
import com.bard.netstatelibrary.type.NetType;

public class NetworkUtils {

    public static boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) NetworkManager.getInstance().getApplication()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {

                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {

                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }


    public static NetType getNetType() {
        try {
            ConnectivityManager connectivity = (ConnectivityManager)  NetworkManager.getInstance().getApplication()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivity == null) return NetType.NONE;


            NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
            if(networkInfo == null) return NetType.NONE;

            int nType = networkInfo.getType();
            if(nType == ConnectivityManager.TYPE_MOBILE){
                if(networkInfo.getExtraInfo().toLowerCase().equals("cmnet")){
                    return NetType.CMNET;
                }else{
                    return NetType.CMWAP;
                }
            }else if(nType == ConnectivityManager.TYPE_WIFI){
                return NetType.WIFI;
            }
            return NetType.NONE;
        } catch (Exception e) {
            return NetType.NONE;
        }
    }






    //打开网络设置界面
    public static void openSetting(Context context, int requestCode){
        Intent intent = new Intent("/");
        ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(componentName);
        intent.setAction("android.intent.action.VIEW");
        ((Activity)context).startActivityForResult(intent, requestCode);
    }
}
