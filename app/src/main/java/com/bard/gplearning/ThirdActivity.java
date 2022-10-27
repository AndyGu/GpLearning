package com.bard.gplearning;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bard.arouter_annotation.BindPath;
import com.bard.gplearning.gif.GifManager;

import java.io.File;
import java.lang.ref.WeakReference;

@BindPath(path = "/app/ThirdActivity")
public class ThirdActivity extends AppCompatActivity {

    String path = Environment.getExternalStorageDirectory().getAbsolutePath() +
            File.separator +
            "Download/ok/test.gif";
    GifManager gifManager;
    Bitmap bitmap;
    ImageView iv_gif;

    MyHandler myHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        File file = new File(path);
        Log.e("ThirdActivity", "file.exists()="+file.exists()+" path="+path);

        path = "/data/data/com.bard.gplearning/files/test.gif";
        gifManager = new GifManager(path);
        int width = gifManager.getWidth();
        int height = gifManager.getHeight();
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        long nextFrameTime = gifManager.renderFrame(bitmap);


        myHandler = new MyHandler(this);
        myHandler.sendEmptyMessageDelayed(1, nextFrameTime);

        iv_gif = findViewById(R.id.iv_gif);
        iv_gif.setImageBitmap(bitmap);
        iv_gif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ThirdActivity.this, FourthActivity.class);
                startActivity(intent);
            }
        });
    }


    static class MyHandler extends Handler {
        WeakReference<ThirdActivity> weakReference;

        MyHandler(ThirdActivity activity) {
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ThirdActivity activity = weakReference.get();
            if (activity != null) {
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
