package com.bard.gplearning.permissions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.bard.gplearning.R;
import com.bard.gplearning.permissions.core.IPermissionRequestCallback;
import com.bard.gplearning.permissions.util.PermissionUtils;

//专门处理权限的Activity
public class MyPermissionActivity extends Activity {
    private static final String TAG = PermissionUtils.class.getSimpleName();

    //定义权限处理的标识 -- 接收用户传递进来的
    private final static String PARAM_PERMISSION = "param_permission";
    private final static String PARAM_REQUEST_CODE = "param_request_code";
    public final static int PARAM_REQUEST_CODE_DEFAULT = 1;

    private String[] permissions; //存储用户要申请的权限
    private int requestCode;
    private static IPermissionRequestCallback permissionListener; //这个Activity 已经授权/取消授权/被拒绝授权

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_permission);

        permissions = getIntent().getStringArrayExtra(PARAM_PERMISSION);
        requestCode = getIntent().getIntExtra(PARAM_REQUEST_CODE, PARAM_REQUEST_CODE_DEFAULT);

        if(permissions == null && requestCode < 0 && permissionListener == null){
            this.finish();
            return;
        }

        boolean permissionRequest = PermissionUtils.hasPermissionRequest(this, permissions);
        if(permissionRequest){
            permissionListener.granted();
            this.finish();
            return;
        }

        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(PermissionUtils.requestPermissionSuccess(grantResults)){
            permissionListener.granted();
            this.finish();
            return;
        }

        //如果用户点击了 拒绝（不再提示打钩）等操作，告诉外界
        if(PermissionUtils.shouldShowRequestPermissionRationale(this, permissions)){
            //用户拒绝，不再提醒
            permissionListener.denied();
            this.finish();
            return;
        }

        //如果执行到这里来了，就证明权限申请被取消了
        permissionListener.cancel();
        this.finish();
        return;
    }

    //结束Activity，不需要有任何动画效果
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    // 把当前整个空白Activity暴露给外界使用【给大管家AspectJ监听到申请权限的注解时使用】
    public static void requestPermissionAction(Context context, String[] permissions,
                                               int requestCode, IPermissionRequestCallback iPermissionRequestCallback){
        Log.e(TAG, "requestPermissionAction");
        permissionListener = iPermissionRequestCallback;

        Intent intent = new Intent(context, MyPermissionActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Bundle bundle = new Bundle();
        bundle.putInt(PARAM_REQUEST_CODE, requestCode);
        bundle.putStringArray(PARAM_PERMISSION, permissions);
        intent.putExtras(bundle);

        context.startActivity(intent);
    }
}
