package com.bard.gplearning.command;

import android.content.ComponentName;
import android.content.Intent;
import android.text.TextUtils;

import com.bard.gplearning.MyApplication;
import com.bard.webview.ICallbackFromMainProcessToWebProcessInterface;
import com.bard.webview.command.Command;
import com.google.auto.service.AutoService;

import java.util.Map;

@AutoService({Command.class})
public class CommandOpenPage implements Command {

    @Override
    public String name() {
        return "openPage";
    }

    @Override
    public void execute(Map parameters, ICallbackFromMainProcessToWebProcessInterface callback) {
        String targetClass = String.valueOf(parameters.get("target_class"));
        if (!TextUtils.isEmpty(targetClass)) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(MyApplication.sApplication, targetClass));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApplication.sApplication.startActivity(intent);
        }
    }
}
