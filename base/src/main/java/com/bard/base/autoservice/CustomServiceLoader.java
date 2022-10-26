package com.bard.base.autoservice;

import android.app.Activity;

import java.util.ServiceLoader;

public final class CustomServiceLoader {
    private CustomServiceLoader() {
    }


    public static <S> S load(Class<S> service) {
        try {
            return ServiceLoader.load(service).iterator().next();
        } catch (Exception e) {
            return null;
        }
    }
}
