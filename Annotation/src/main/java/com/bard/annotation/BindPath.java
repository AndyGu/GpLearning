package com.bard.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) //声明这个注解是放在什么上面，本例是类
@Retention(RetentionPolicy.CLASS) //声明这个注解的生命周期  源码期--编译期--运行期  java--class--runtime 本例是源码期
public @interface BindPath {
    String value();
}
