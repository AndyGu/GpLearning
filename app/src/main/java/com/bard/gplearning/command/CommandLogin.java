package com.bard.gplearning.command;

import android.os.RemoteException;
import android.util.Log;

import com.bard.base.autoservice.CustomServiceLoader;
import com.bard.common.autoservice.IUserCenterService;
import com.bard.common.eventbus.LoginEvent;
import com.bard.webview.ICallbackFromMainProcessToWebProcessInterface;
import com.bard.webview.command.Command;
import com.google.auto.service.AutoService;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

@AutoService({Command.class})
public class CommandLogin implements Command {
    IUserCenterService iUserCenterService = CustomServiceLoader.load(IUserCenterService.class);
    ICallbackFromMainProcessToWebProcessInterface callback;
    String callbacknameFromNativeJs;

    public CommandLogin() {
        EventBus.getDefault().register(this);
    }

    @Override
    public String name() {
        return "login";
    }

    @Override
    public void execute(final Map parameters, ICallbackFromMainProcessToWebProcessInterface callback) {
        Log.d("CommandLogin", new Gson().toJson(parameters));
        if (iUserCenterService != null && !iUserCenterService.isLogined()) {
            iUserCenterService.login();
            this.callback = callback;
            this.callbacknameFromNativeJs = parameters.get("callbackname").toString();
        }
    }

    @Subscribe
    public void onMessageEvent(LoginEvent event) {
        Log.d("CommandLogin", event.userName);
        Map map = new HashMap();
        map.put("accountName", event.userName);
        if (this.callback != null) {
            try {
                this.callback.onResult(callbacknameFromNativeJs, new Gson().toJson(map));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
