package com.bard.gplearning.gif;

import android.graphics.Bitmap;

public class GifManager {
    private volatile long gifInfo; //volatile作同步，保证线程安全

    static {
        System.loadLibrary("native-lib");
    }

    public GifManager(String path) {
        gifInfo = openFile(path);
    }



    public synchronized int getWidth(){
        return getGifWidth(gifInfo);
    }

    public synchronized int getHeight(){
        return getGifHeight(gifInfo);
    }

    public long renderFrame(Bitmap bitmap){
        return renderFrame(gifInfo, bitmap);
    }


    private native long openFile(String path);

    private native int getGifWidth(long gifInfo);

    private native int getGifHeight(long gifInfo);

    private native long renderFrame(long gifInfo, Bitmap bitmap);


}
