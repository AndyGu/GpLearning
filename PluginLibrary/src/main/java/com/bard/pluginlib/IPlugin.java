package com.bard.pluginlib;

import android.app.Activity;
import android.os.Bundle;

public interface IPlugin {

    int FROM_INTERNAL = 0;
    int FROM_EXTERNAL = 1;

    void attach(Activity proxyActivity);

    void onCreate(Bundle saveInstanceState);

    void onResume();

    void onPause();

    void onDestroy();
}
