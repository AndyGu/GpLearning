// ICallbackFromMainProcessToWebProcessInterface.aidl
package com.bard.webview;

// Declare any non-default types here with import statements

interface ICallbackFromMainProcessToWebProcessInterface {

    void onResult(String callbackname, String response);
}