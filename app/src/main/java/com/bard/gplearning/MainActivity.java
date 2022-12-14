package com.bard.gplearning;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PersistableBundle;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import android.util.Printer;
import android.view.DisplayCutout;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bard.arouter_annotation.BindPath;
import com.bard.arouter_api.RouterManager;
import com.bard.base.autoservice.CustomServiceLoader;
import com.bard.common.autoservice.IWebViewService;
import com.bard.eventbuslibrary.EventBus;
import com.bard.eventbuslibrary.GPBean;
import com.bard.eventbuslibrary.Subscriber;
import com.bard.eventbuslibrary.ThreadMode;
import com.bard.glidelibrary.GPGlide;
import com.bard.glidelibrary.RequestListener;
import com.bard.gplearning.aidl.aidlbean.Person;
import com.bard.gplearning.aidl.service.MyAidlService;
import com.bard.gplearning.permissions.annotation.PermissionNeed;
import com.bard.gplearning.utils.UriParseUtils;
import com.bard.gplearning.utils.MockDownloadUtils;
import com.bard.httprequestlibrary.GPHttp;
import com.bard.httprequestlibrary.IJsonDataTransformListener;
import com.bard.httprequestlibrary.ResponseBean;
import com.bard.kotlinlibrary.activity.ChronometerActivity;
import com.bard.kotlinlibrary.fragment.TestStatic;
import com.bard.netstatelibrary.NetworkManager;
import com.bard.netstatelibrary.annotation.NetworkSubscriber;
import com.bard.netstatelibrary.type.NetType;
import com.bard.netstatelibrary.utils.Constants;
import com.bard.pluginlib.PluginManager;
import com.bard.pluginlib.ProxyActivity;
import com.bard.webview.WebViewActivity;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;


@BindPath(path = "/app/MainActivity")
public class MainActivity extends AppCompatActivity
//        implements NetChangeObserver
{

    //?????????????????????????????? ????????????lib??? native-lib
    static {
        System.loadLibrary("native-lib");
    }

    private static int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
    };
    ScrollView scrollView;
    LinearLayout scrollView_linearlayout;


    private String url = "http://v.juhe.cn/historyWeather/citys?provice_id=2&key=bb52107206585ab074f5e59a8c73875b";

    private IMyAidlInterface mAidl;

    /**
     * ??????????????????
     *
     * ???????????????????????????????????????????????? IBinder ?????????????????????????????????????????????????????????????????????????????????
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //??????????????? Binder???????????? AIDL????????????????????????????????????
            mAidl = IMyAidlInterface.Stub.asInterface(service);
            Log.e("MyAidlClient", "onServiceConnected pid="+ Process.myPid());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mAidl = null;
            Log.e("MyAidlClient", "onServiceDisconnected pid="+ Process.myPid());
        }
    };


    @Override
    protected void onStart() {
        Log.e("MainActivity", "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.e("MainActivity", "onResume");
        super.onResume();


//        new Handler().postDelayed(() -> {
//            new Thread(() -> {
//                Looper.prepare();
//                TextView textView = new TextView(MainActivity.this);
//                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                textView.setBackgroundResource(R.color.colorAccent);
//                WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
//                wm.addView(textView, new WindowManager.LayoutParams());
//                textView.setText("???????????????");
//                textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
//                textView.setBackgroundResource(android.R.color.transparent);
//                Looper.loop();
//            }).start();
//        }, 1000);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("MainActivity", "onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("MainActivity", "onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MainActivity", "onPause");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("MainActivity", "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("MainActivity", "onRestoreInstanceState 1");
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        Log.e("MainActivity", "onRestoreInstanceState 2");
    }

    public class MyRecevier extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = getResultExtras(true);
        }
    }
    /**
     * Messenger?????????
     */
//    /**
//     * ???????????? Messenger
//     */
//    Messenger mClientMessenger = new Messenger(new Handler() {
//        @Override
//        public void handleMessage(final Message msg) {
//            if (msg != null && msg.arg1 == ConfigHelper.MSG_ID_SERVER){
//                if (msg.getData() == null){
//                    return;
//                }
//
//                String content = (String) msg.getData().get(ConfigHelper.MSG_CONTENT);
//                Log.d("TAG", "Message from server: " + content);
//            }
//        }
//    });
//
//    //???????????? Messenger
//    private Messenger mServerMessenger;
//
//    private ServiceConnection mMessengerConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(final ComponentName name, final IBinder service) {
//            mServerMessenger = new Messenger(service);
//        }
//
//        @Override
//        public void onServiceDisconnected(final ComponentName name) {
//            mServerMessenger = null;
//        }
//    };
//
//
//    private void bindMessengerService() {
//        Intent intent = new Intent(this, MessengerService.class);
//        bindService(intent, mMessengerConnection, BIND_AUTO_CREATE);
//    }
//
//    public void sendMsg() {
//        String msgContent = "????????????_" + System.currentTimeMillis();
//
//        Message message = Message.obtain();
//        message.arg1 = ConfigHelper.MSG_ID_CLIENT;
//        Bundle bundle = new Bundle();
//        bundle.putString(ConfigHelper.MSG_CONTENT, msgContent);
//        message.setData(bundle);
//        message.replyTo = mClientMessenger;     //????????????????????????????????????
//
//        try {
//            mServerMessenger.send(message);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unbindService(mMessengerConnection);
//    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(getApplicationContext(), MyAidlService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);

        verifyStoragePermission(this);
        scrollView = findViewById(R.id.scrollView);
        scrollView_linearlayout = findViewById(R.id.scrollView_linearlayout);



/*

????????????????????????????????????????????????
1.????????????????????????????????????
2.??????????????????????????????????????????????????????????????????0????????????100%????????????
3.??????????????????????????????????????????????????????


??????????????????????????????????????????????????????????????????
1.??????????????????????????????????????????
2.????????????ColorMatrix???????????????ColorMatrix????????????ColorMatrixColorFilter?????????????????????????????????????????????????????????????????????????????????0??????????????????????????????
3.??????????????????paint.setColorFilter(ColorFilter filter)?????????ColorMatrix??????????????????
4.??????canvas.drawBitmap????????????????????????????????????

?????????????????????????????????????????????
a.??????????????????ColorMatrix?????????????????????????????????????????????????????????ColorMatrix.set(float[] src)?????????????????????????????????
b.??????????????????ColorMatrix???????????????setRotate(int axis, float degrees)?????????????????????????????????????????????????????????
c.??????????????????ColorMatrix??????????????????setSaturation(float sat)???????????????
d.??????????????????a.b.c????????????


private Bitmap setImageMatrix1(Bitmap bitmap) {
        Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);

        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return bmp;
}

 */

        PluginManager.getInstance().init(MainActivity.this);

        findViewById(R.id.my_btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleImgLoad();
                testAspect();
            }
        });


        findViewById(R.id.my_btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multiImgLoad();
                testABC();
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RouterManager.getInstance().build("/app/SecondActivity").navigation(MainActivity.this);
            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String apkPatch = MockDownloadUtils.copyAssetAndWrite(MainActivity.this, "aa.apk");
                PluginManager.getInstance().loadApk(apkPatch);
            }
        });

        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ProxyActivity.class);
                intent.putExtra("className", "com.bard.plugin.PluginModuleActivity");
                startActivity(intent);
            }
        });

        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ORCActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouterManager.getInstance().build("/app/ThirdActivity").navigation(MainActivity.this);
            }
        });

        findViewById(R.id.button9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouterManager.getInstance().build("/Component/ComponentActivity").navigation(MainActivity.this);
            }
        });

        findViewById(R.id.button10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, WaterViewActivity.class);
                startActivity(intent);
            }
        });


        findViewById(R.id.button11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ImmersiveActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, DisplayCutoutActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button13).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                Person person = new Person(10 + random.nextInt(10), "Andy_" + random.nextInt(10));

                try {
                    mAidl.addPerson(person);
                    List<Person> personList = mAidl.getPersonList();
                    Toast.makeText(MainActivity.this, personList.toString(), Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });


        findViewById(R.id.button14).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FourthActivity.class);
                startActivity(intent);
            }
        });


        findViewById(R.id.button15).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChronometerActivity.class);
                startActivity(intent);
            }
        });


        findViewById(R.id.button16).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IWebViewService webviewService = CustomServiceLoader.load(IWebViewService.class);
//                webviewService.startWebViewActivity(MainActivity.this, "https://www.baidu.com", "hello", true);
                if (webviewService != null) {
                    webviewService.startDemoHtml(MainActivity.this);
                }
            }
        });


        findViewById(R.id.button17).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TouchActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.button18).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AnimActivity.class);
                startActivity(intent);
            }
        });


        GPHttp.sendJsonRequest(null, url, ResponseBean.class, new IJsonDataTransformListener<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean o) {
                Log.e("onSuccess", o.toString());
            }
        });


        //??????
//        NetworkManager.getInstance().setPwdCompleteListener(this);
        NetworkManager.getInstance().registerObserver(this);


        EventBus.getInstance().register(this);
    }

    @NetworkSubscriber(netType = NetType.AUTO)
    public void network(NetType netType) {
        switch (netType) {
            case WIFI:
                Log.e(Constants.LOG_TAG, "MainActivity --- WIFI");
                break;

            case CMNET:
            case CMWAP:
                Log.e(Constants.LOG_TAG, "MainActivity --- ???????????????" + netType.name());
                break;

            case NONE:
                Log.e(Constants.LOG_TAG, "MainActivity --- ????????????");
                break;
        }
    }


    @NetworkSubscriber(netType = NetType.WIFI)
    public void test(NetType netType) {
        switch (netType) {
            case WIFI:
                Log.e(Constants.LOG_TAG, "test MainActivity --- WIFI");
                break;

            case CMNET:
            case CMWAP:
                Log.e(Constants.LOG_TAG, "test MainActivity --- ???????????????" + netType.name());
                break;

            case NONE:
                Log.e(Constants.LOG_TAG, "test MainActivity --- ????????????");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkManager.getInstance().unRegisterObserver(this);
    }


    //    @Override
//    public void onConnect(NetType type) {
//        Log.e(Constants.LOG_TAG,"onConnect = "+type.name());
//    }
//
//    @Override
//    public void onDisConnect() {
//        Log.e(Constants.LOG_TAG,"onDisConnect");
//    }


    public void singleImgLoad() {
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(1000, 668));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        ImageView imageView2 = new ImageView(this);
        imageView2.setLayoutParams(new ViewGroup.LayoutParams(1000, 668));
        imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.timg);
        imageView.setImageBitmap(bmp);

        Bitmap bitmap = setImageMatrix(bmp);
        imageView2.setImageBitmap(bitmap);

        scrollView_linearlayout.addView(imageView);
        scrollView_linearlayout.addView(imageView2);

//        GPGlide.with(this).load("https://ss1.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=2581bca42f3fb80e13d167d706d02ffb/4034970a304e251fb1a2546da986c9177e3e53c9.jpg")
//                .placeHolder(R.mipmap.ic_launcher).listener(new RequestListener() {
//            @Override
//            public boolean onSuccess(Bitmap bitmap) {
//                Toast.makeText(MainActivity.this, "load onSuccess", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//
//            @Override
//            public boolean onFailure() {
//                Toast.makeText(MainActivity.this, "load fail", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        }).into(imageView);


    }


    public void multiImgLoad() {

        for (int i = 0; i < 10; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            scrollView_linearlayout.addView(imageView);


            String url = null;

            switch (i) {
                case 0:
                    url = "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=4107751820,287160106&fm=26&gp=0.jpg";
                    break;
                case 1:
                    url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1560607704173&di=fcaaa0f1e8dea66e353c9b46746b2783&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2Fe3e47bfe6519ca2c477b6b5f42a37f33b11db909267bc-JLT6x0_fw658";
                    break;
                case 2:
                    url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1560607737714&di=c09b1b11c2e1ac872faf50ea748cebf4&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2Fdbbbbf82e6a7d1981ac2c0473121b990269c896e166e8-RAaza0_fw658";
                    break;
                case 3:
                    url = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3343507656,2009419952&fm=26&gp=0.jpg";
                    break;
                case 4:
                    url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1560607774455&di=97e4b293909972369cd6490866c3ba64&imgtype=0&src=http%3A%2F%2Fimg1.cache.netease.com%2Fcatchpic%2F9%2F97%2F975F6562D17A50559F982F3740663404.jpg";
                    break;
                case 5:
                    url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1560607786176&di=6c4d703253b8011209c7047a476ba583&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2Fa69c01446e4a9b77ab564e63a54f4209ca6a96bf2044c-MzNREH_fw658";
                    break;
                case 6:
                    url = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=693859391,3907462733&fm=15&gp=0.jpg";
                    break;
                case 7:
                    url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1560607806341&di=3ab41eca6b11ad8ab0abef40d399ab32&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201603%2F27%2F20160327170017_cweLH.jpeg";
                    break;
                case 8:
                    url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1560607816295&di=2076fb9bd5238419cb00c99739230d87&imgtype=0&src=http%3A%2F%2Fwww.bkjia.com%2Fuploads%2Fallimg%2F180320%2F140915G42-4.jpg";
                    break;
                case 9:
                    url = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1244919695,3049783709&fm=26&gp=0.jpg";
                    break;
            }

            GPGlide.with(this).load(url)
                    .placeHolder(R.mipmap.ic_launcher).listener(new RequestListener() {
                        @Override
                        public boolean onSuccess(Bitmap bitmap) {
                            Toast.makeText(MainActivity.this, "coming", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        @Override
                        public boolean onFailure() {
                            return false;
                        }
                    }).into(imageView);
        }

    }

    public static void verifyStoragePermission(Activity activity) {
        try {
            int permission = ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Subscriber(threadMode = ThreadMode.MAIN)
    public void getMessage(GPBean gpBean) {
        Log.e("getMessage", "MainActivity=" + gpBean.toString());
    }


    //???????????????
    public native void bsPatch(String oldApk, String patch, String output);


    //????????????
    public void update() {
        //1?????????????????? patch????????????????????????????????????SDCard
        //???????????????????????????????????????

        new AsyncTask<Void, Void, File>() {
            //?????????????????????
            @Override
            protected File doInBackground(Void... voids) {
                String oldApk = getApplicationInfo().sourceDir; //???????????????apk??????
                String patch = new File(Environment.getExternalStorageDirectory(), "patch").getAbsolutePath();
                //??????????????????????????????
                String output = createNewApk().getAbsolutePath();
                bsPatch(oldApk, patch, output);
                return new File(output);
            }

            //??????doInBackground?????????????????????????????????
            //??????doInBackground?????????File???????????????????????????

            @Override
            protected void onPostExecute(File file) {
                //?????????????????????apk???????????????
                UriParseUtils.installApk(MainActivity.this, file);
            }
        }.execute();

    }

    //???????????????????????????apk???????????????????????????????????????
    private File createNewApk() {
        File newApk = new File(Environment.getExternalStorageDirectory(), "bsdiff.apk");

        try {
            if (!newApk.exists()) {
                newApk.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newApk;
    }


    private Bitmap setImageMatrix(Bitmap bitmap) {
        Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);

        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return bmp;
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    public void openFullScreenModel(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        activity.getWindow().setAttributes(lp);

        View decorView = activity.getWindow().getDecorView();
        int systemUiVisibility = decorView.getSystemUiVisibility();
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        systemUiVisibility |= flags;
        activity.getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        setCutout();


        Printer printer = new Printer() {
            @Override
            public void println(String x) {

            }
        };
        Looper.getMainLooper().setMessageLogging(printer);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void setCutout() {
        View decorView = getWindow().getDecorView();
        WindowInsets rootWindowInsets = decorView.getRootWindowInsets();
        if (rootWindowInsets != null) {
            DisplayCutout cutout = rootWindowInsets.getDisplayCutout();
            List<Rect> boundingRects = cutout.getBoundingRects();
            if (boundingRects != null && boundingRects.size() > 0) {
                String msg;
                for (Rect rect : boundingRects) {
                    msg = "right-" + rect.right + " left-" + rect.left;
                    Log.d("setCutout", "msg = " + msg);
                }
            }
        }
    }

    @PermissionNeed({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    private void testAspect() {
        Log.e("MainActivity", "testAspectJ");
    }


    @PermissionNeed({Manifest.permission.ACCESS_FINE_LOCATION})
    private void testABC() {
        Log.e("MainActivity", "testABC");
    }
}
