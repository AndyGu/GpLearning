package com.bard.webview.webprocess;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bard.webview.WebViewCallBack;
import com.bard.webview.bean.JsParam;
import com.bard.webview.webprocess.webchromeclient.MyWebChromeClient;
import com.bard.webview.webprocess.webviewclient.MyWebViewClient;
import com.bard.webview.webprocess.settings.WebViewDefaultSettings;
import com.google.gson.Gson;

public class BaseWebView extends WebView {

    public BaseWebView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public BaseWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public BaseWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        WebViewProcessCommandDispatcher.getInstance().setContext(context);
        WebViewProcessCommandDispatcher.getInstance().initAidlConnection();
        WebViewDefaultSettings.getInstance().setSettings(this);
        addJavascriptInterface(this, "xiangxuewebview");
    }

    public void registerWebViewCallBack(WebViewCallBack webViewCallBack) {
        setWebViewClient(new MyWebViewClient(webViewCallBack));
        setWebChromeClient(new MyWebChromeClient(webViewCallBack));
    }

    @JavascriptInterface
    public void takeNativeAction(final String jsParam) {
        Log.e("TAG", jsParam);
        if (!TextUtils.isEmpty(jsParam)) {
            final JsParam jsParamObject = new Gson().fromJson(jsParam, JsParam.class);
            if (jsParamObject != null) {
                WebViewProcessCommandDispatcher.getInstance().executeCommand(jsParamObject.name, new Gson().toJson(jsParamObject.param), this);
            }
        }
    }

    public void handleCallback(final String callbackname, final String response){
        if(!TextUtils.isEmpty(callbackname) && !TextUtils.isEmpty(response)){
            post(new Runnable() {
                @Override
                public void run() {
                    String jscode = "javascript:xiangxuejs.callback('" + callbackname + "'," + response + ")";
                    Log.e("xxxxxx", jscode);
                    evaluateJavascript(jscode, null);
                }
            });
        }
    }
}
