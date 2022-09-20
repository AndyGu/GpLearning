package com.bard.httprequestlibrary;

public class GPHttp {


    public static<T, M> void sendJsonRequest(T requestData, String url, Class<M> reponse, IJsonDataTransformListener listener){
        IHttpRequest httpRequest = new JsonHttpRequest();
        CallBackListener callBackListener = new JsonCallBackListener<>(reponse, listener);
        HttpTask ht = new HttpTask(url, requestData, httpRequest, callBackListener);
        ThreadPoolManager.getInstance().addTask(ht);
    }
}
