package com.bard.netstatelibrary.listener;


import com.bard.netstatelibrary.type.NetType;

public interface NetChangeObserver {
    void onConnect(NetType type);
    void onDisConnect();
}
