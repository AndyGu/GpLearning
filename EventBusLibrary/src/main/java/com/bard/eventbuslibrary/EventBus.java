package com.bard.eventbuslibrary;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventBus {

    private static volatile EventBus eventBus = new EventBus();
    private Map<Object, List<SubscribeMethod>> cacheMap = new HashMap<>();

    public static EventBus getInstance(){
        if(eventBus == null){
            synchronized (EventBus.class){
                if(eventBus == null){
                    eventBus = new EventBus();
                }
            }
        }
        return  eventBus;
    }


    private EventBus(){

    }

    public void register(Object object){
        List<SubscribeMethod> list = cacheMap.get(object);
        if(list == null){
            list = findSubscribeMethod(object);
            cacheMap.put(object, list);
        }


    }

    private List<SubscribeMethod> findSubscribeMethod(Object object) {
        List<SubscribeMethod> list = new ArrayList<>();
        Class<?> clazz = object.getClass();

        //循环找起父类的带有subscriber的方法
        while(clazz != null){
            //凡是系统级别的父类，直接省略
            String name = clazz.getName();
            if(name.startsWith("java.") ||
                name.startsWith("javax.") ||
                name.startsWith("android.")){
                break;
            }

            Method[] methods = clazz.getDeclaredMethods();

            for(Method method : methods){

                //寻找所有带scbscriber注解的方法
                Subscriber subscriber = method.getAnnotation(Subscriber.class);

                if(subscriber == null){
                    continue;
                }

                //判断当前带有Subscriber注解的方法，是否有且仅有一个参数
                Class<?>[] types = method.getParameterTypes();
                if(types.length != 1){
                    Log.e("findSubscribeMethod", "错误 eventBus only accept One parameter");
                }

                ThreadMode threadMode = subscriber.threadMode();
                SubscribeMethod subscribeMethod = new SubscribeMethod(method, threadMode, types[0]);
                list.add(subscribeMethod);
            }
            clazz = clazz.getSuperclass();
        }

        return list;
    }


    public void post(Object bean){
        //直接循环cacheMao然后找到带有对应bean对象的方法进行调用

        Set<Object> set = cacheMap.keySet();
        Iterator<Object> iterator = set.iterator();

        while(iterator.hasNext()){
            Object obj = iterator.next();
            List<SubscribeMethod> list = cacheMap.get(obj);
            for(SubscribeMethod subscribeMethod : list){

                //a（if条件前面的对象）对象所对应的类 是不是b（if条件后面的对象）对象对应的类信息的父类或接口
                if(subscribeMethod.getType().isAssignableFrom(bean.getClass())){
                    invoke(subscribeMethod, obj, bean);
                }
            }
        }
    }

    private void invoke(SubscribeMethod subscribeMethod, Object obj, Object bean) {
        Method method = subscribeMethod.getMethod();
        try {
            method.invoke(obj, bean);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
