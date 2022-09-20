package com.bard.gplearning.permission.core;

public interface IPermissionRequestCallback {
    void granted();

    void cancel();

    void denied();
}
