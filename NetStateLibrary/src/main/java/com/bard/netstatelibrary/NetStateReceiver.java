package com.bard.netstatelibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bard.netstatelibrary.annotation.NetworkSubscriber;
import com.bard.netstatelibrary.bean.MethodManager;
import com.bard.netstatelibrary.type.NetType;
import com.bard.netstatelibrary.utils.Constants;
import com.bard.netstatelibrary.utils.NetworkUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class NetStateReceiver extends BroadcastReceiver {

    private NetType netType;
//    private NetChangeObserver listener;
    private Map<Object, List<MethodManager>> networkList;


    public NetStateReceiver() {
        //初始化网络状态
        netType = NetType.NONE;
        networkList = new HashMap<>();
    }

//    public void setListener(NetChangeObserver listener) {
//        this.listener = listener;
//    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent  == null || intent.getAction() == null){
            Log.e(Constants.LOG_TAG, "intent空异常");
            return;
        }

        //处理广播事件
        if(intent.getAction().equalsIgnoreCase(Constants.ANDROID_NET_CHANGE_ACTION)){
            Log.e(Constants.LOG_TAG, "网络发生改变");
            netType = NetworkUtils.getNetType();
            if(NetworkUtils.isNetworkAvailable()){
                Log.e(Constants.LOG_TAG, "网络连接成功");
//                if(listener != null){
//                    listener.onConnect(netType);
//                }
            }else{
                Log.e(Constants.LOG_TAG, "网络连接失败");
//                if(listener != null){
//                    listener.onConnect(netType);
//                }
            }

            //总线：全局通知
            post(netType);
        }
    }


    //同时分发过程
    private void post(NetType netType){
        Set<Object> set = networkList.keySet();


        //例：获取MainActivity对象
        for(Object register : set){
            //所有注解的方法
            List<MethodManager> methodManagerList = networkList.get(register);
            if(methodManagerList != null){
                for(MethodManager methodManager : methodManagerList){
                    //两者参数比较
                    if(methodManager.getType().isAssignableFrom(netType.getClass())){
                        switch (methodManager.getNetType()){
                            case AUTO:
                                invoke(methodManager, register, netType);
                                break;

                            case WIFI:
                                if(netType == NetType.WIFI || netType == NetType.NONE){
                                    invoke(methodManager, register, netType);
                                }
                                break;

                            case CMNET:
                                if(netType == NetType.CMNET || netType == NetType.NONE){
                                    invoke(methodManager, register, netType);
                                }
                                break;

                            case CMWAP:
                                if(netType == NetType.CMWAP || netType == NetType.NONE){
                                    invoke(methodManager, register, netType);
                                }
                                break;
                        }
                    }
                }
            }

        }
    }


    //在MainActivity中执行方法
    private void invoke(MethodManager methodManager, Object register, NetType netType){
        try {
            methodManager.getMethod().invoke(register, netType);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    //将应用总所有Activity注册了网络监听的方法，添加到集合
    //key MainActivity value：MainActivity注解方法
    public void registerObserver(Object register){

        //MainActivity所有网络监听注解方法
        List<MethodManager> methodList = networkList.get(register);
        if(methodList == null){
            //开始添加方法
            methodList = findAnnotationMethod(register);

            networkList.put(register, methodList);
        }
    }

    //通过反射技术，从主接种找到方法，添加到集合
    private List<MethodManager> findAnnotationMethod(Object register) {
        List<MethodManager> methodList = new ArrayList<>();

        Class<?> clazz = register.getClass();

        //获取class中的所有方法
        Method[] methods = clazz.getMethods();
        for(Method method : methods){
            //获取方法注解
            NetworkSubscriber networkSubscriber = method.getAnnotation(NetworkSubscriber.class);
            if(networkSubscriber == null){
                continue;
            }

            //方法返回值校验
//            Class<?> returnTypes = method.getReturnType();
//            if(returnTypes){
//
//            }


            //方法的参数校验
            Class<?>[] parameterTypes = method.getParameterTypes();
            if(parameterTypes.length != 1){
                throw new RuntimeException(method.getName() + "方法参数错误，应有且只有一个");
            }
            //参数类型校验

            //过滤方法完成，添加到集合
            MethodManager methodManager = new MethodManager(parameterTypes[0], networkSubscriber.netType(), method);
            methodList.add(methodManager);
        }


        return methodList;
    }


    public void unRegisterObserver(Object register){
        if(!networkList.isEmpty()){
            networkList.remove(register);
        }
        Log.e(Constants.LOG_TAG, register.getClass().getName()+"注销成功");
    }



    public void removeAllObserver(Object register){
        //应用退出时
        if(!networkList.isEmpty()){
            networkList.clear();
        }

        NetworkManager.getInstance().getApplication().unregisterReceiver(this);
        networkList = null;
        Log.e(Constants.LOG_TAG, register.getClass().getName()+"注销所有成功");
    }



}
