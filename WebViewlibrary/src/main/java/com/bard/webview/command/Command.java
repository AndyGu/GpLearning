package com.bard.webview.command;

import com.bard.webview.ICallbackFromMainProcessToWebProcessInterface;

import java.util.Map;

public interface Command {
    String name();
    void execute(Map parameters, ICallbackFromMainProcessToWebProcessInterface callback);
}
