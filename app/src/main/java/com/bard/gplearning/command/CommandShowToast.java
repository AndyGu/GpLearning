package com.bard.gplearning.command;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.bard.gplearning.BaseApplication;
import com.bard.webview.ICallbackFromMainProcessToWebProcessInterface;
import com.bard.webview.command.Command;
import com.google.auto.service.AutoService;

import java.util.Map;

@AutoService({Command.class})
public class CommandShowToast implements Command {
    @Override
    public String name() {
        return "showToast";
    }

    @Override
    public void execute(final Map parameters, ICallbackFromMainProcessToWebProcessInterface callback) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseApplication.sApplication, String.valueOf(parameters.get("message")), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
