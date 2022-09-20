package com.bard.gplearning.permission.annotation;

import com.bard.gplearning.permission.MyPermissionActivity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//申请权限的注解
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {
    String[] value();

    int requestCode() default MyPermissionActivity.PARAM_REQUEST_CODE_DEFAULT;
}
