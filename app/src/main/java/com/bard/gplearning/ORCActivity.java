package com.bard.gplearning;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bard.arouter_annotation.BindPath;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@BindPath(path = "/app/ORCActivity")
public class ORCActivity extends Activity {

    private ImageView idCard;
    private TextView tv_text;
    private int index = 0;
    private int[] ids = {
            R.drawable.id_0,
            R.drawable.id_1,
            R.drawable.id_2,
            R.drawable.id_3,
            R.drawable.id_4,
            R.drawable.id_5,
            R.drawable.id_6,
            R.drawable.id_7,
            R.drawable.id_8
    };

    private TessBaseAPI tessBaseAPI;
    private String language = "ck";
    private AsyncTask<Void, Void, Boolean> asyncTask;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orc);

        idCard = findViewById(R.id.iv_idcard);
        tv_text = findViewById(R.id.tv_identification_text);
        idCard.setImageResource(R.drawable.id_0);

        //初始化OCR
        tessBaseAPI = new TessBaseAPI();
        initTess();
    }


    public void identify(View view) {
        //图像识别主要区域
        //从原图Bitmap中查找
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), ids[index]);
        Bitmap bitmap_id_number = findIdNumber(bitmap, Bitmap.Config.ARGB_8888);

        bitmap.recycle();
        Log.e("identify", "bitmap_id_number=" + bitmap_id_number);
        if (bitmap_id_number != null) {
            idCard.setImageBitmap(bitmap_id_number);
        } else {
            bitmap_id_number.recycle();
            return;
        }

        //OCR文字识别
        tessBaseAPI.setImage(bitmap_id_number);
        tv_text.setText(tessBaseAPI.getUTF8Text());
    }

    private native Bitmap findIdNumber(Bitmap bitmap, Bitmap.Config config);

    public void previous(View view) {
        tv_text.setText(null);
        index--;
        if (index < 0) {
            index = ids.length - 1;
        }
        idCard.setImageResource(ids[index]);
    }

    public void next(View view) {
        tv_text.setText(null);
        index++;
        if (index >= ids.length) {
            index = 0;
        }
        idCard.setImageResource(ids[index]);
    }


    @SuppressLint("StaticFieldLeak")
    private void initTess() {
        //让它在后台去初始化，记得加读写权限
        asyncTask = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                //目录+文件名 目录下需要tessdata目录
                InputStream is = null;
                FileOutputStream fos = null;
                try {
                    is = getAssets().open(language + ".traineddata");
                    File file = new File("sdcard/tess/tessdata/" + language + ".traineddata");
                    if (!file.exists()) {
                        file.getParentFile().mkdirs();
                        fos = new FileOutputStream(file);
                        byte[] buffer = new byte[2048];
                        int len;
                        while ((len = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                        }
                        fos.close();
                    }
                    is.close();
                    return tessBaseAPI.init("/sdcard/tess", language);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }

            @Override
            protected void onPreExecute() {
                showProgress();
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                dismissProgress();
                if (aBoolean) {
                    Toast.makeText(ORCActivity.this, "初始化ORC成功", Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }
            }
        };
        asyncTask.execute();
    }


    private void showProgress() {
        if (progressDialog != null) {
            progressDialog.show();
        } else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("请稍后...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    private void dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
