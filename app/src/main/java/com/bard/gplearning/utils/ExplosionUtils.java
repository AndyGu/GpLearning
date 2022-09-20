package com.bard.gplearning.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import java.util.Random;

public class ExplosionUtils {
    public static final Random RANDOM = new Random(System.currentTimeMillis());
    private static final Canvas CANVAS = new Canvas();

    public static Bitmap createBitmapFromView(View view){
        view.clearFocus();//使view失去焦点回复原本样式
        Bitmap bitmap = createBitmapSafely(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888, 5);
        if(bitmap != null){
            synchronized (CANVAS){
                CANVAS.setBitmap(bitmap);
                view.draw(CANVAS);
                CANVAS.setBitmap(null);
            }
        }
        return bitmap;
    }


    public static Bitmap createBitmapSafely(int width, int height, Bitmap.Config config, int retryCount){
        try {
            return Bitmap.createBitmap(width, height, config);
        }catch (OutOfMemoryError error){
            error.printStackTrace();
            if(retryCount > 0){
                System.gc();
                return createBitmapSafely(width, height, config, retryCount - 1);
            }
        }
        return null;
    }
}
