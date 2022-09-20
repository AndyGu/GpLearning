package com.bard.pluginlib;

import android.app.Activity;
import android.os.Bundle;

public class PluginActivity extends Activity implements IPlugin {

    private int from = FROM_INTERNAL;

    private Activity mProxyActivity;


    @Override
    public void attach(Activity proxyActivity) {
        mProxyActivity = proxyActivity;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        if(saveInstanceState != null){
            from = saveInstanceState.getInt("FROM");
        }

        if(from == FROM_INTERNAL){
            super.onCreate(saveInstanceState);
            mProxyActivity = this;
        }
    }


    @Override
    public void setContentView(int layoutResID) {
        if(from == FROM_INTERNAL){
            super.setContentView(layoutResID);
        }else{
            mProxyActivity.setContentView(layoutResID);
        }
    }

    @Override
    public void onResume() {
        if(from == FROM_INTERNAL){
            super.onResume();
        }

    }

    @Override
    public void onPause() {
        if(from == FROM_INTERNAL){
            super.onPause();
        }
    }

    @Override
    public void onDestroy() {
        if(from == FROM_INTERNAL){
            super.onDestroy();
        }
    }
}
