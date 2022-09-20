package com.bard.httprequestlibrary;

public interface IJsonDataTransformListener<T> {
    //用来传递数据
    void onSuccess(T t);
}
