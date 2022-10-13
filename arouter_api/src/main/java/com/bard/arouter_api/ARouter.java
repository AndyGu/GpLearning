package com.bard.arouter_api;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dalvik.system.DexFile;

/**
 * 中间人 代理
 * 采用 RouterManager，利用apt + 缓存
 */
//@Deprecated
//public class ARouter {
//    private static ARouter instance = new ARouter();
//
//    //装载所有的activity的类对象的容器
//    private Map<String, Class<? extends Activity>> activityList;
//
//    private Context context;
//
//    private ARouter() {
//        activityList = new HashMap<>();
//    }
//
//
//    public static ARouter getInstance() {
//        if (instance == null) {
//            synchronized (ARouter.class) {
//                if (instance == null) {
//                    instance = new ARouter();
//                }
//            }
//        }
//        return instance;
//    }
//
//
//    /**
//     * 不建议传Activity的Context，难以维护，传Application的
//     */
//    public void init(Application application) {
//        this.context = application;
//
//        List<String> classNameList = getClassName("com.bard.gplearning.utils"); //通过包名获取类名，此包名应是AnnotationCompiler中生成的类的所在包名
//        for (String className : classNameList) {
//            try {
//                Class clazz = Class.forName(className);
//                //判断这个类 是否是IRouter的实现类
//                if (IRouter.class.isAssignableFrom(clazz)) {
//                    IRouter iRouter = (IRouter) clazz.newInstance(); //面向接口编程
//                    iRouter.putActivity();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//    /**
//     * 将Activity的类对象 添加进map中
//     *
//     * @param path
//     * @param clazz
//     */
//    public void putActivity(String path, Class<? extends Activity> clazz) {
//        if (!TextUtils.isEmpty(path) && clazz != null) {
//            activityList.put(path, clazz);
//        }
//    }
//
//
//    /**
//     * 跳转的方法
//     *
//     * @param path
//     * @param bundle
//     */
//    public void jumpActivity(String path, Bundle bundle) {
//        //这就是Activity的class对象
//        Class<? extends Activity> clazz = activityList.get(path);
//        if (clazz == null) {
//            return;
//        }
//
//        Intent intent = new Intent().setClass(context, clazz);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (bundle != null) {
//            intent.putExtras(bundle);
//        }
//        context.startActivity(intent);
//    }
//
//
//    /**
//     * 通过报名，获取到包下所有类的类名
//     *
//     * @param packageName
//     * @return
//     */
//    private List<String> getClassName(String packageName) {
//        //创建一个class对象集合
//        List<String> classList = new ArrayList<>();
//        String path;
//        try {
//            //通过包管理器 获取到应用信息类，然后获取到APK的完整路径
//            path = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).sourceDir;
//            //根据APK的完整路径获取到编译后的dex文件
//            DexFile dexFile = new DexFile(path);
//            //获取编译后的dex文件中的所有class
//            Enumeration entries = dexFile.entries();
//            //进行遍历
//            while (entries.hasMoreElements()) {
//                //遍历所有的class的报名
//                String name = (String) entries.nextElement();
//                //判断类的报名是否符合
//                if (name.contains(packageName)) {
//                    //如果符合，就加入到集合中
//                    classList.add(name);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return classList;
//    }
//}
