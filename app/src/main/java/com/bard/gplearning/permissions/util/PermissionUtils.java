package com.bard.gplearning.permissions.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.collection.SimpleArrayMap;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bard.gplearning.permissions.annotation.PermissionCancel;
import com.bard.gplearning.permissions.annotation.PermissionDenied;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 *
 */
public class PermissionUtils {
    private static final String TAG = PermissionUtils.class.getSimpleName();

    private static SimpleArrayMap<String, Integer> MIN_SDK_PERMISSIONS;

    static {
        MIN_SDK_PERMISSIONS = new SimpleArrayMap<>(8);
        MIN_SDK_PERMISSIONS.put("com.android.voicemail.permission.ADD_VOICEMAIL", 14);
        MIN_SDK_PERMISSIONS.put("android.permission.BODY_SENSORS", 20);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_CALL_LOG", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_EXTERNAL_STORAGE", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.USE_SIP", 9);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_CALL_LOG", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.SYSTEM_ALERT_WINDOW", 23);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_SETTINGS", 23);
    }


    private static HashMap<String, Class<? extends ISetting>> permissionMenu = new HashMap<>();

    private static final String MANUFACTURER_DEFAULT = "Default";

    public static final String MANUFACTURER_HUAWEI = "huawei";//华为
    public static final String MANUFACTURER_MEIZU = "meizu";//魅族
    public static final String MANUFACTURER_XIAOMI = "xiaomi";//小米
    public static final String MANUFACTURER_SONY = "sony";//索尼
    public static final String MANUFACTURER_OPPO = "oppo";
    public static final String MANUFACTURER_LG = "lg";
    public static final String MANUFACTURER_VIVO = "vivo";
    public static final String MANUFACTURER_SAMSUNG = "samsung";//三星
    public static final String MANUFACTURER_LETV = "letv";//乐视
    public static final String MANUFACTURER_ZTE = "zte";//中兴
    public static final String MANUFACTURER_YULONG = "yulong";//酷派
    public static final String MANUFACTURER_LENOVO = "lenovo";//联想


    static {
        permissionMenu.put(MANUFACTURER_DEFAULT, DefaultStartSettings.class);
        permissionMenu.put(MANUFACTURER_OPPO, OPPOStartSettings.class);
        permissionMenu.put(MANUFACTURER_VIVO, VIVOStartSettings.class);
    }

    /**
     * 判断所有权限是否都给了，如果有一个权限没给，就返回false
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasPermissionRequest(Context context, String... permissions) {

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG,"hasPermissionRequest return false");
                return false;
            }
        }
        Log.e(TAG,"hasPermissionRequest return true");
        return true;
    }


    /**
     * 判断是否全部真正的成功
     *
     * @param gantedResult
     * @return
     */
    public static boolean requestPermissionSuccess(int... gantedResult) {
        if (gantedResult == null || gantedResult.length <= 0) {
            Log.e(TAG,"requestPermissionSuccess length<=0 return false");
            return false;
        }

        for (int permissionValue : gantedResult) {
            if (permissionValue != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG,"requestPermissionSuccess !=granted return false");
                return false;
            }
        }

        Log.e(TAG,"requestPermissionSuccess return true");
        return true;
    }


    /**
     * 用户是否拒绝了并且点击了不再提示
     *
     * @return
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity, String... permissions) {
        for (String permission : permissions) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                Log.e(TAG,"shouldShowRequestPermissionRationale return true");
                return true;
            }
        }
        Log.e(TAG,"shouldShowRequestPermissionRationale return false");
        return false;
    }


    /**
     * 专门去执行被注解了的方法
     *
     * @param object
     * @param annotationClass
     */
    public static void invokeAnnotation(Object object, Class annotationClass, int requestCode) {
        Log.e(TAG,"invokeAnnotation annotationClass="+annotationClass.getName());
        //获取object的class对象
        Class<?> aClass = object.getClass();

        //获取object的所有方法
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            //让虚拟机不去检测 private
            method.setAccessible(true);

            //判断方法是否有被annotationClass注解过
            boolean annotationPresent = method.isAnnotationPresent(annotationClass);
            if (annotationPresent) {
                try {
                    Annotation annotation = method.getAnnotation(annotationClass);
                    //请求码和处理请求的接收码一致才会调用函数，否则不调用
                    if (annotation instanceof PermissionDenied) {
                        PermissionDenied permissionDenied = (PermissionDenied) annotation;
                        if (permissionDenied.requestCode() == requestCode) {
                            method.invoke(object);
                        }
                    } else if (annotation instanceof PermissionCancel) {
                        PermissionCancel permissionCancel = (PermissionCancel) annotation;
                        if (permissionCancel.requestCode() == requestCode) {
                            method.invoke(object);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, "没设置annotationClass");
            }
        }
    }

    // TODO 专门去 跳转到 设置界面
    public static void startAndroidSettings(Context context) {
        // 拿到当前手机品牌制造商，来获取 具体细节
        Class aClass = permissionMenu.get(Build.MANUFACTURER.toLowerCase());

        Looper.prepare();
        if (aClass == null) {
            aClass = permissionMenu.get(MANUFACTURER_DEFAULT);
        }

        try {
            Object newInstance = aClass.newInstance(); // new OPPOStartSettings()

            ISetting iMenu = (ISetting) newInstance; // ISetting iMenu = (ISetting) oPPOStartSettings;

            // 高层 面向抽象，而不是具体细节
            Intent startActivityIntent = iMenu.getStartSettingsIntent(context);

            if (startActivityIntent != null) {
                context.startActivity(startActivityIntent);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }


}
