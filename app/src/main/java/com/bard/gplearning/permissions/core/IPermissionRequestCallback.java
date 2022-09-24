package com.bard.gplearning.permissions.core;

public interface IPermissionRequestCallback {
    void granted();

    void cancel();

    void denied();
}
