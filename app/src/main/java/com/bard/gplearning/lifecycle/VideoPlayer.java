package com.bard.gplearning.lifecycle;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class VideoPlayer implements LifecycleObserver {
    //Activity中【实现LifeCycleOwner】
//    VideoPlayer videoPlayer = new VideoPlayer();
//    //添加监听者
//    getLifecycle().addObserver(videoPlayer);

    private static final String TAG = "VideoPlayer";

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void startPlay(){
        Log.e(TAG,"开始播放视频");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void pausePlay(){
        Log.e(TAG,"暂停播放视频");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void stopPlay(){
        Log.e(TAG,"停止播放视频");
    }
}
