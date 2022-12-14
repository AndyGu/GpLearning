package com.bard.arouter_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <strong>Activity使用的布局文件注解</strong>
 * <ul>
 * <li>@Target(ElementType.TYPE)   // 接口、类、枚举、注解</li>
 * <li>@Target(ElementType.FIELD) // 属性、枚举的常量</li>
 * <li>@Target(ElementType.METHOD) // 方法</li>
 * <li>@Target(ElementType.PARAMETER) // 方法参数</li>
 * <li>@Target(ElementType.CONSTRUCTOR)  // 构造函数</li>
 * <li>@Target(ElementType.LOCAL_VARIABLE)// 局部变量</li>
 * <li>@Target(ElementType.ANNOTATION_TYPE)// 该注解使用在另一个注解上</li>
 * <li>@Target(ElementType.PACKAGE) // 包</li>
 * <li>@Retention(RetentionPolicy.RUNTIME) <br>注解会在class字节码文件中存在，jvm加载时可以通过反射获取到该注解的内容</li>
 * </ul>
 * <p>
 * 生命周期：SOURCE < CLASS < RUNTIME
 * 1、一般如果需要在运行时去动态获取注解信息，用RUNTIME注解
 * 2、要在编译时进行一些预处理操作，如ButterKnife，用CLASS注解。注解会在class文件中存在，但是在运行时会被丢弃
 * 3、做一些检查性的操作，如@Override，用SOURCE源码注解。注解仅存在源码级别，在编译的时候丢弃该注解
 * <p>
 * APT中，的确有时候使用 SOURCE，有时候使用CLASS，但是他们从角色上考虑，还是有细微区别的哦
 *  CLASS：要在编译时进行一些预处理操作，如ButterKnife，用CLASS注解。注解会在class文件中存在
 *  SOURCE：做一些检查性的操作，既然是做一些检查工作，是不是和编译期非常类似，但是他的角色是，如@Override，用SOURCE源码注解。注解仅存在源码级别
 */
@Target(ElementType.TYPE) //声明这个注解是放在什么上面，本例是类
@Retention(RetentionPolicy.CLASS) //声明这个注解的生命周期  源码期--编译期--运行期  java--class--runtime 本例是源码期
public @interface BindPath {
    String path();

    // 路由组名（选填，如果开发者不填写，可以从path中截取出来）
    String group() default "";
}
