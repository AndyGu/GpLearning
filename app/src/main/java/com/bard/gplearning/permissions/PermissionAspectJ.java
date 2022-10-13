package com.bard.gplearning.permissions;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.bard.gplearning.permissions.annotation.PermissionCancel;
import com.bard.gplearning.permissions.annotation.PermissionDenied;
import com.bard.gplearning.permissions.annotation.PermissionNeed;
import com.bard.gplearning.permissions.core.IPermissionRequestCallback;
import com.bard.gplearning.permissions.util.PermissionUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PermissionAspectJ {

    /**
     * JoinPoint            说明           Pointcut语法
     * -----------------------------------------------------------
     * method call          函数调用        call(MethodSignature)
     * method execution     函数执行        execution(MethodSignature)
     * constructor call     构造函数调用     call(ConstructorSignature)
     * constructor execution构造函数执行内部  execution(ConstructorSignature)
     * field get            读变量          get(FieldSignature)
     * field set            写变量          set(FieldSignature)
     * static initialization static块初始化 staticinitialization(TypeSignature)
     * handler              异常处理        handler(TypeSignature) 注意：只能与@Before()配合使用
     *
     *
     * Signature            语法
     * ---------------------------------------------------------------------------
     * MethodSignature      @注解 访问权限 返回值类型 类名.函数名(参数)
     *
     *
     * Signature语法明细      说明
     * ---------------------------------------------------------------------------
     * @注解                 注解类的完整路径，如果没有则不写
     * 访问权限               public/private/protect/static/final，如果没有则不写
     * 返回值类型             如果不限定类型，使用通配符*表示
     * 类名.函数名            可以使用的通配符包括*和..以及+号
     * 参数                  例子：
     *                      1.(int,char):表示参数只有哦两个，并且类型也要符合
     *                      2.(String,..):表示至少有一个String参数，后面参数类型和个数不限
     *                      3.（Object...）:表示不定个数的参数，且类型都是Object，
     *                          这里...不是通配符，而是JAVA中代表不定参数的意思
     *
     *
     * AspectJ_Advice
     *
     * Advice               说明
     * ----------------------------------------------------
     * @Before(Pointcut)    切入到JoinPoint之前
     * @After(Pointcut)     切入到JoinPoint之后
     * @Around(Pointcur)    替代原来的代码，执行切入点方法使用 ProceedingJoinPoint.proceed()
     *                      注意：不支持和@Before(), @After()等一起使用
     * @AfterReturning      @AfterReturning(pointcut="xxx",returning="retValue")
     * @AfterThrowing       @AfterThrowing(pointcut="xxx",throwing="throwable")
     *
     *
     */

    @Pointcut("execution(@com.bard.gplearning.permissions.annotation.PermissionNeed * *(..)) && @annotation(permissionNeed)")
    public void pointActionMethod(PermissionNeed permissionNeed){  //这个参数名和 @annotation(permissionNeed)的参数名一定要一样！！！
        Log.e("PermissionAspect","pointActionMethod");
        //通过这种方式，拿到注解的相关信息
    }


//    @Around("call(* com.bard.gplearning.MainActivity.testAspect())")
//    public void pointActionMethod(final ProceedingJoinPoint point){
//        //通过这种方式，拿到注解的相关信息
//        Log.e("PermissionAspectJ","pointActionMethod");
//        try {
//            point.proceed();
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//    }

    @Around("pointActionMethod(permissionNeed)")
    public void getPermissionPoint(final ProceedingJoinPoint point, final PermissionNeed permissionNeed) throws Throwable{
        Log.e("PermissionAspect","getPermissionPoint length="+permissionNeed.value().length);
        //操作所有注解的API  == point

        //直接申请权限，可以开启空白Activity了

        //先定义一个上下文操作环境
        Context context = null;

        final Object thisObject = point.getThis(); //获取到注解所在的类的实例【MainActivity的实例/对象】
        //获取到注解携带的权限数据
        final String[] value = permissionNeed.value();
        final int requestCode = permissionNeed.requestCode();


        //判断thisObject是否是Context的子类，如果是就进行赋值
        if (thisObject instanceof Context){
            context = (Context) thisObject;
        }else  if (thisObject instanceof Fragment){ //同时判断Fragment的情况
            context = ((Fragment) thisObject).getActivity();
        }

        //上下文为空直接就不用玩了，抛异常
        if (context == null){
            throw new IllegalAccessException("context == null || permission == null");
        }

        //如果不是在Context的子类，或者是在Context的子类但是申请的权限没有或者有但是长度为0就返回不处理
        if (permissionNeed.value().length == 0){
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
