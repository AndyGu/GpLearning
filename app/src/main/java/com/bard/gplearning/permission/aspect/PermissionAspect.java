package com.bard.gplearning.permission.aspect;

//专门处理权限的 AspectJ

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.bard.gplearning.permission.MyPermissionActivity;
import com.bard.gplearning.permission.annotation.Permission;
import com.bard.gplearning.permission.annotation.PermissionCancel;
import com.bard.gplearning.permission.annotation.PermissionDenied;
import com.bard.gplearning.permission.core.IPermissionRequestCallback;
import com.bard.gplearning.permission.util.PermissionUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PermissionAspect { //大管家

    /**
     * 声明切入点是所有的Permission注解标记了的方法
     * @param permission
     */
    @Pointcut("execute(@com.bard.gplearning.permission.annotation.Permission * *(..)) && @annotation(permissions)")
    public void pointActionMethod(Permission permission){
        //通过这种方式，拿到注解的相关信息
    }

    /**
     * 下面的函数，是真正干活的
     *
     * 根据切入点注入代码
     * @param point
     * @param permission
     */
    @Around("pointActionMethod(permission)")
    public void getPermissionPoint(final ProceedingJoinPoint point, final Permission permission) throws Throwable{
        //操作所有注解的API  == point

        //直接申请权限，可以开启空白Activity了

        //先定义一个上下文操作环境
        Context context = null;

        final Object thisObject = point.getThis(); //获取到注解所在的类的实例【MainActivity的实例/对象】
        //获取到注解携带的权限数据
        final String[] value = permission.value();
        final int requestCode = permission.requestCode();


        //判断thisObject是否是Context的子类，如果是就进行赋值
        if (thisObject instanceof Context){
            context = (Context) thisObject;
        }else  if (thisObject instanceof Fragment){ //同时判断Fragment的情况
            context = ((Fragment) thisObject).getActivity();
        }

        //上下文为空直接就不用玩了，抛异常
        if (context == null || permission == null){
            throw new IllegalAccessException("context == null || permission == null");
        }

        //如果不是在Context的子类，或者是在Context的子类但是申请的权限没有或者有但是长度为0就返回不处理
        if (permission.value().length == 0){
            return;
        }

        //捕获用户写的注解，
        MyPermissionActivity.requestPermissionAction(context, value, requestCode, new IPermissionRequestCallback() {
            @Override
            public void granted() { //空白Activity反馈给我们的信息 权限申请成功
                try {
                    point.proceed(); //被注解@Permission修饰的方法体 -》 正常执行 -》
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void cancel() { //空白Activity反馈给我们的信息 权限申请取消 反射去执行用户标记注解@PermissionCancel的方法
                Log.e("PermissionAspect","权限申请结果：取消");
                //反射执行某个方法，必须要有持有这个方法的类的对象，即thisObject【MainActivity的实例/对象】
                PermissionUtils.invokeAnnotation(thisObject, PermissionCancel.class, requestCode);
            }

            @Override
            public void denied() { //空白Activity反馈给我们的信息 权限申请被拒绝
                Log.e("PermissionAspect","权限申请结果：被永久拒绝");
                //反射执行某个方法，必须要有持有这个方法的类的对象，即thisObject【MainActivity的实例/对象】
                PermissionUtils.invokeAnnotation(thisObject, PermissionDenied.class, requestCode);
            }
        });


    }
}
