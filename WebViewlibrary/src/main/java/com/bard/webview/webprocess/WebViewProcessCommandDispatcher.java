package com.bard.webview.webprocess;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.bard.webview.ICallbackFromMainProcessToWebProcessInterface;
import com.bard.webview.IWebProcessToMainProcessInterface;
import com.bard.webview.mainprocess.MainProcessCommandService;

/**
 *
 * 这里是客户端，是WebViewActivity以及WebView所在的 process=":remoteweb"进程
 *
 * 客户端与服务端绑定时的回调，返回 IBinder 后，客户端就可以将其转换为IXXXInterface代理，通过它远程调用服务端的方法，即实现了通讯
 *
 *
 * web进程调用 executeCommand(...)，进而调用 iWebProcessToMainProcessInterface.handleWebCommand(...)
 * 达到了：在客户端【web进程】调用服务端【主进程】的方法【handleWebCommand(...)】的目的，完成了web进程到主进程的跨越
 * 在主进程里 接收到了web发来的指令
 *
 */
public class WebViewProcessCommandDispatcher implements ServiceConnection {
    private static WebViewProcessCommandDispatcher sInstance;
    private IWebProcessToMainProcessInterface iWebProcessToMainProcessInterface;
    private Context context;

    public static WebViewProcessCommandDispatcher getInstance() {
        if (sInstance == null) {
            synchronized (WebViewProcessCommandDispatcher.class) {
                sInstance = new WebViewProcessCommandDispatcher();
            }
        }
        return sInstance;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void initAidlConnection(){
//        Intent intent = new Intent(BaseApplication.sApplication, MainProcessCommandService.class);
//        BaseApplication.sApplication.bindService(intent, this, Context.BIND_AUTO_CREATE);
        Intent intent = new Intent(context, MainProcessCommandService.class);
        context.bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        //连接后拿到 IBinder，转换成 AIDL，在不同进程会返回个代理
        iWebProcessToMainProcessInterface = IWebProcessToMainProcessInterface.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        iWebProcessToMainProcessInterface = null;
        initAidlConnection();
    }

    @Override
    public void onBindingDied(ComponentName name) {
        iWebProcessToMainProcessInterface = null;
        initAidlConnection();
    }

    public void executeCommand(String commandName, String params, final BaseWebView baseWebView) {
        if(iWebProcessToMainProcessInterface != null) {
            try {
                Log.e("executeCommand","commandName="+commandName+" params="+params);
                iWebProcessToMainProcessInterface.handleWebCommand(commandName, params, new ICallbackFromMainProcessToWebProcessInterface.Stub() {
                    @Override
                    public void onResult(String callbackname, String response) throws RemoteException {
                        Log.e("executeCommand","onResult callbackname="+callbackname+" response="+response);
                        baseWebView.handleCallback(callbackname, response);
                    }
                });
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
