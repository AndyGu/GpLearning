package com.bard.gplearning.permissions.annotation;


import com.bard.gplearning.permissions.MyPermissionActivity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//申请权限被拒绝的注解
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionDenied {
    int requestCode() default MyPermissionActivity.PARAM_REQUEST_CODE_DEFAULT;
}
