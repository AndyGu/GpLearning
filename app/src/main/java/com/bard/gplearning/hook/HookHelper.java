package com.bard.gplearning.hook;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.bard.gplearning.SecondActivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class HookHelper {
    private static final String TAG = "HookHelper";

    public static final String EXTRA_TARGET_INTENT = "extra_target_intent";

    public static void hookAMSAidl(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P){
            hookIActivityTaskManager();
        } else{
            hookIActivityManager();
        }
    }

    public static void hookIActivityTaskManager() {
        try {
            Field singletonField = null;
            Class<?> activityManager = Class.forName("android.app.ActivityTaskManager");
            singletonField = activityManager.getDeclaredField("IActivityTaskManagerSingleton");
            singletonField.setAccessible(true);
            Object singleton = singletonField.get(null);

            //拿IActivityManager对象
            Class<?> singletonClass = Class.forName("android.util.Singleton");
            Field mInstanceField = singletonClass.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);

            //原生的IActivityTaskManager
            final Object IActivityTaskManager = mInstanceField.get(singleton);

            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    new Class[]{Class.forName("android.app.IActivityTaskManager")},
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            //偷梁换柱
                            //真正要启动的Activity目标
                            Intent raw = null;
                            int index = -1;
                            if("startActivity".equals(method.getName())){
                                for (int i=0; i<args.length; i++){
                                    if(args[i] instanceof Intent){
                                        raw = (Intent)args[i];
                                        index = i;
                                    }
                                }

                                Intent newIntent = new Intent();
                                newIntent.setComponent(new ComponentName("com.bard.gplearning", SecondActivity.class.getName()));
                                newIntent.putExtra(EXTRA_TARGET_INTENT, raw);

                                args[index] = newIntent;
                            }

                            return method.invoke(IActivityTaskManager, args);
                        }
                    });

            //IActivityMangerProxy 融入到framework
            mInstanceField.set(singleton, proxy);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void hookIActivityManager(){
        try{
            Field singletonField = null;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                Class<?> activityManager = Class.forName("android.app.ActivityManger");
                singletonField = activityManager.getDeclaredField("IActivityManagerSingleton");
            } else {
                Class<?> activityManager = Class.forName("android.app.ActivityMangerNative");
                singletonField = activityManager.getDeclaredField("gDefault");
            }
            singletonField.setAccessible(true);
            Object singleton = singletonField.get(null);

            //拿IActivityManager对象
            Class<?> singletonClass = Class.forName("android.util.Singleton");
            Field mInstanceField = singletonClass.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);

            //原生的IActivityManager
            final Object rawIActivityManager = mInstanceField.get(singleton);

            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    new Class[]{Class.forName("android.app.IActivityManager")},
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            //偷梁换柱
                            //真正要启动的Activity目标
                            Intent raw = null;
                            int index = -1;
                            if("startActivity".equals(method.getName())){
                                for (int i=0; i<args.length; i++){
                                    if(args[i] instanceof Intent){
                                        raw = (Intent)args[i];
                                        index = i;
                                    }
                                }

                                Intent newIntent = new Intent();
                                newIntent.setComponent(new ComponentName("com.bard.gplearning", SecondActivity.class.getName()));
                                newIntent.putExtra(EXTRA_TARGET_INTENT, raw);

                                args[index] = newIntent;
                            }

                            return method.invoke(rawIActivityManager, args);
                        }
                    });


            //IActivityMangerProxy 融入到framework
            mInstanceField.set(singleton, proxy);

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public static void hookHandler(){
        try {
            Class<?> atClass = Class.forName("android.app.ActivityThread");
            Field sCurrentActivityThreadField = atClass.getDeclaredField("sCurrentActivityThread");
            sCurrentActivityThreadField.setAccessible(true);
            Object sCurrentActivityThread = sCurrentActivityThreadField.get(null);

            //ActivityThread 一个app进程只有一个，获取它的mH
            Field mHField = atClass.getDeclaredField("mH");
            mHField.setAccessible(true);
            final Handler mH = (Handler)mHField.get(sCurrentActivityThread);

            //获取mCallback
            Field mCallbackField = Handler.class.getDeclaredField("mCallback");
            mCallbackField.setAccessible(true);
            mCallbackField.set(mH, new Handler.Callback(){
                @Override
                public boolean handleMessage(Message msg) {
                    Log.i(TAG, "handleMessage:"+msg.what);
                    switch (msg.what){
                        case 100:
                            //7.0之前版本
                            break;
                        case 159:
                            Object obj = msg.obj;
                            Log.i(TAG, "handleMessage: obj="+obj);
                            try{
                                Field mActivityCallbacksField = obj.getClass().getDeclaredField("mActivityCallbacks");
                                mActivityCallbacksField.setAccessible(true);
                                List mActivityCallbacks = (List)mActivityCallbacksField.get(obj);
                                Log.i(TAG, "handleMessage:mActivityCallbacks="+mActivityCallbacks);

                                //注意了，这里如果debug调试发现第一次 size=0 原因如下
                                //在Android O之前
                                //public static final int LAUNCH_ACTIVITY          = 100;
                                //public static final int PAUSE_ACTIVITY           = 101;
                                //public static final int LAUNCH_ACTIVITY_FINISHING= 102;
                                //public static final int STOP_ACTIVITY_SHOW       = 103;
                                //public static final int STOP_ACTIVITY_HIDE       = 104;
                                //public static final int SHOW_WINDOW              = 105;
                                //public static final int HIDE_WINDOW              = 106;
                                //public static final int RESUME_ACTIVITY          = 107;
                                //public static final int SEND_RESULT              = 108;
                                //public static final int DESTROY_ACTIVITY         = 109;

                                //从Android P开始重构了状态模式
                                //public static final int EXECUTE_TRANSACTION      = 159;

                                //首先一个app 只哟䘝ActivityThread 然后就只有一个mH
                                //我们app所有的activity的生命周期的处理都在 mH的 handleMessage里面
                                //在Android 8.0之前，不同的生命周期对应不同的msg.what处理
                                //在Android 8.0 改成了全部有EXECUTE_TRANSACTION来处理

                                //handleMessage: 159
                                //handleMessage: obj=android.app.servertransaction.ClientTransaction@efd342
                                //handleMessage: mActivityCallbacks=[]
                                //invoke: method activityPause
                                //handleMessage: 159
                                //handleMessage: obj=android.app.servertransaction.ClientTransaction@4962
                                //handleMessage: mActivityCallbacks=[WindowVisibilityItem{showWindow=true}]
                                //handleMessage: size= 1
                                //handleMessage: 159
                                //handleMessage: obj=android.app.servertransaction.ClientTransaction@9e98c6b
                                //handleMessage: mActivityCallbacks=[LaunchActivityItem{intent=Intent{cmp=com.bard.gplearning}}]
                                //handleMessage: size= 1

                                if(mActivityCallbacks.size() > 0){
                                    Log.i(TAG, "handleMessage: size=" + mActivityCallbacks.size());
                                    String className = "android.app.servertransaction.LaunchActivityItem";
                                    if(mActivityCallbacks.get(0).getClass().getCanonicalName().equals(className)){
                                        Object object = mActivityCallbacks.get(0);
                                        Field intentField = object.getClass().getDeclaredField("mIntent");
                                        intentField.setAccessible(true);
                                        Intent intent = (Intent)intentField.get(object);
                                        Intent targetIntent = intent.getParcelableExtra(EXTRA_TARGET_INTENT);
                                        intent.setComponent(targetIntent.getComponent());
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            break;
                    }
                    mH.handleMessage(msg);
                    return true;
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
