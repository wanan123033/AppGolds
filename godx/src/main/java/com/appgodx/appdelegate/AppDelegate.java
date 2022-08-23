package com.appgodx.appdelegate;

import android.content.Context;
import android.content.res.Configuration;

import com.appgodx.base.BaseApplication;

public interface AppDelegate {
    void attachBaseContext(BaseApplication baseApplication, Context base);

    void onCreate();

    void onLowMemory();

    void onTrimMemory(int level);

    void onTerminate();

    void onConfigurationChanged(Configuration newConfig);
}
