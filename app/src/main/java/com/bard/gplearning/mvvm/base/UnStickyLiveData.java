package com.bard.gplearning.mvvm.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class  UnStickyLiveData<T> extends MutableLiveData<T>{

    private boolean stickyFlag = false;

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        super.observe(owner, observer);
        if(!stickyFlag){
            hook(observer);
        }
    }

    public void observeSticky(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer){
        super.observe(owner, observer);
    }

    private void hook(@NonNull Observer<? super T> observer) {
        try{
            //1.得到mLastVersion
            //获取到LiveData类中的mObserver对象
            //SafeIterableMap<Observer<? super T>, ObserverWrapper> mObservers
            Class<LiveData> classLiveData = LiveData.class;
            Field mObserversField = classLiveData.getDeclaredField("mObservers");
            mObserversField.setAccessible(true);
            //获取这个成员变量的对象
            Object mObserversObject = mObserversField.get(this);
            //得到map对应的class对象
            Class<?> mObserversClass = mObserversObject.getClass();
            //获取到mObservers对象的get方法 entry
            Method methodGet = mObserversClass.getDeclaredMethod("get", Object.class);
            methodGet.setAccessible(true);
            //执行get方法
            Object invokeEntry = methodGet.invoke(mObserversObject, observer);
            //定义一个空的对象
            Object objectWrapper = null;
            if(invokeEntry instanceof Map.Entry){
                objectWrapper = ((Map.Entry)invokeEntry).getValue();
            }

            if(objectWrapper == null){
                throw new NullPointerException("observerWrapper is null");
            }

            //获取ObserverWrapper的类对象，编译擦除问题会导致很多问题，所以用getSuperclass()
            Class<?> mObserverSuperClass = objectWrapper.getClass().getSuperclass();
            Field mLastVersion = mObserverSuperClass.getDeclaredField("mLastVersion");
            mLastVersion.setAccessible(true);
            //得到mVersion
            Field mVersion = classLiveData.getDeclaredField("mVersion");
            mVersion.setAccessible(true);
            //把mVersion的值填入mLastVersion中
            Object mVersionValue = mVersion.get(this);
            mLastVersion.set(objectWrapper, mVersionValue);

            stickyFlag = true;
        } catch (Exception e){
            e.printStackTrace();
        }





    }






































}
