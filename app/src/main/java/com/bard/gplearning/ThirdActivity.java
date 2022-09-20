package com.bard.gplearning;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bard.annotation.BindPath;
import com.bard.gplearning.gif.GifManager;

import java.io.File;
import java.lang.ref.WeakReference;

@BindPath("app/Third")
public class ThirdActivity extends AppCompatActivity {

    String path = Environment.getExternalStorageDirectory().getAbsolutePath() +
            File.separator +
            "Download/test.gif";
    GifManager gifManager;
    Bitmap bitmap;
    ImageView iv_gif;

    MyHandler myHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        gifManager = new GifManager(path);
        int width = gifManager.getWidth();
        int height = gifManager.getHeight();
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        long nextFrameTime = gifManager.renderFrame(bitmap);


        myHandler = new MyHandler(this);
        myHandler.sendEmptyMessageDelayed(1, nextFrameTime);

        iv_gif = findViewById(R.id.iv_gif);
        iv_gif.setImageBitmap(bitmap);
    }


    static class MyHandler extends Handler{
        WeakReference<ThirdActivity> weakReference;

        MyHandler(ThirdActivity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ThirdActivity activity = weakReference.get();
            if(activity != null){
                activity.sendMsg();
            }
        }
    }


    private void sendMsg() {
        long nextFrameTime = gifManager.renderFrame(bitmap);
        iv_gif.setImageBitmap(bitmap);
        myHandler.sendEmptyMessageDelayed(1, nextFrameTime);
    }
}
