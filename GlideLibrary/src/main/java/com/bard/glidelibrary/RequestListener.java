package com.bard.glidelibrary;

import android.graphics.Bitmap;

public interface RequestListener {
    boolean onSuccess(Bitmap bitmap);
    boolean onFailure();
}
