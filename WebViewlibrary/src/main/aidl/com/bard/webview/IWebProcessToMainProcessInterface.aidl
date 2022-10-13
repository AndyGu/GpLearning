// IWebProcessToMainProcessInterface.aidl
package com.bard.webview;

// Declare any non-default types here with import statements
import com.bard.webview.ICallbackFromMainProcessToWebProcessInterface;

interface IWebProcessToMainProcessInterface {
    void handleWebCommand(String commandName, String jsonParams, in ICallbackFromMainProcessToWebProcessInterface callback);
}