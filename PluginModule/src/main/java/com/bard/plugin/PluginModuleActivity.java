package com.bard.plugin;

import android.os.Bundle;

import com.bard.pluginlib.PluginActivity;

public class PluginModuleActivity extends PluginActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
