package com.bard.pluginlib;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


/**
 * 代理Activity 壳
 *
 * 当我们跳转到第三方插件APK里面的Activity的时候 会先跳转到这个壳里面
 * 然后再经过这个代理Activity 去动态加载第三方插件Apk的activity的class对象
 * 然后去调用这个Activity的相对应的生命周期的方法
 */
public class ProxyActivity extends Activity {
    private String mClassName;
    private PluginAPK mPluginApk;
    private IPlugin mIPlugin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClassName = getIntent().getStringExtra("className");
        mPluginApk = PluginManager.getInstance().getPluginApk();

        launchPluginActivity();
    }

    private void launchPluginActivity() {
        if(mPluginApk == null){
            Toast.makeText(this, "apk 加载有误，无法进行跳转", Toast.LENGTH_SHORT).show();
            Log.e("launchPluginActivity", "apk 加载有误，无法进行跳转");
            finish();
            return;
        }

        try {
            Class<?> clazz = mPluginApk.mDexClassLoader.loadClass(mClassName);
            Object object = clazz.newInstance();
            if(object instanceof IPlugin){
                mIPlugin = (IPlugin)object;
                mIPlugin.attach(this);
                Bundle bundle = new Bundle();
                bundle.putInt("FROM", IPlugin.FROM_EXTERNAL);
                mIPlugin.onCreate(bundle);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Resources getResources() {
        return mPluginApk != null ? mPluginApk.mResources : super.getResources();
    }

    @Override
    public AssetManager getAssets() {
        return mPluginApk != null ? mPluginApk.mAssetManager : super.getAssets();
    }

    @Override
    public ClassLoader getClassLoader() {
        return mPluginApk != null ? mPluginApk.mDexClassLoader : super.getClassLoader();
    }
}
