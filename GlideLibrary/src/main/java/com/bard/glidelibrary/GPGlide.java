package com.bard.glidelibrary;

import android.content.Context;

public class GPGlide {
    public static BitmapRequest with(Context context){
        return new BitmapRequest(context);
    }
}
