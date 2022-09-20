package com.bard.pluginlib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class PluginManager {
    private static PluginManager pluginManager = new PluginManager();

    private Context context;
    private PluginAPK mPluginApk;

    public static PluginManager getInstance(){
        if(pluginManager == null){
            synchronized (PluginManager.class){
                if(pluginManager == null){
                    pluginManager = new PluginManager();
                }
            }
        }
        return pluginManager;
    }

    private PluginManager() {
    }

    public PluginAPK getPluginApk(){
        return mPluginApk;
    }

    public void init(Context context){
        this.context = context.getApplicationContext();
    }

    /**
     * 根据传进来的路径去动态加载第三方插件APK的资源对象以及类加载器
     * @param apkPath
     */
    //加载apk文件
    public void loadApk(String apkPath){
        //获取到包管理器
        PackageManager packageManager = context.getPackageManager();
        //通过包管理器获取到传进来的这个路径下面的dex文件的包信息类
        PackageInfo packageInfo  = packageManager.getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
        if(packageInfo == null){
            return;
        }
        DexClassLoader dexClassLoader = createDexClassLoader(apkPath);

        AssetManager assetManager = createAssetManager(apkPath);

        Resources resources = createResources(assetManager);

        mPluginApk = new PluginAPK(packageInfo, resources, dexClassLoader);
    }



    //创建dexclassloader 加载dex文件
    private DexClassLoader createDexClassLoader(String apkPath) {
        //获取到当前应用的私有存储路径
        File file = context.getDir("dex", Context.MODE_PRIVATE);
        //获取到了传进来的这个路径下面的dex文件的类加载器
        return new DexClassLoader(apkPath, file.getAbsolutePath(), null, context.getClassLoader());
    }

    private AssetManager createAssetManager(String apkPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            method.invoke(assetManager, apkPath);
            return assetManager;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    //创建resource 加载资源文件
    private Resources createResources(AssetManager assetManager){
        Resources resources = context.getResources();
        return new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
    }
}
