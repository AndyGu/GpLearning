package com.bard.gplearning.permissions;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.bard.gplearning.permissions.annotation.Permission;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PermissionAspectJ {

//    @Pointcut("execute(@com.bard.gplearning.permission.annotation.Permission * *(..)) && @annotation(permissions)")
//    public void pointActionMethod(Permission permission){
//        //通过这种方式，拿到注解的相关信息
//    }


    @Around("call(* com.bard.gplearning.MainActivity.testAspect())")
    public void pointActionMethod(final ProceedingJoinPoint point){
        //通过这种方式，拿到注解的相关信息
        Log.e("PermissionAspectJ","pointActionMethod");
        try {
            point.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
