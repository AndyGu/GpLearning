package com.bard.httprequestlibrary;

import java.io.InputStream;

public interface CallBackListener {

    void onSuccess(InputStream inputStream);

    void onFailure();
}
