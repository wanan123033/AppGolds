package com.appgodx.glide;

import android.os.Looper;

public class Util {
    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
