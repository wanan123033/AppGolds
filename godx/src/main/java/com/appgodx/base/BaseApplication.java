package com.appgodx.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import com.appgodx.appdelegate.AppDelegate;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseApplication extends Application {
    private AppDelegate delegate;
    private Map<String,Object> registCache;
    public static final String ROUTER = "ROUTER";
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        delegate = createAppDelegate();
        registCache = new HashMap<>();
        delegate.attachBaseContext(this,base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        delegate.onCreate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        delegate.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        delegate.onTrimMemory(level);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        delegate.onTerminate();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        delegate.onConfigurationChanged(newConfig);
    }

    protected void register(String key,Object obj){
        registCache.put(key,obj);
    }
    public Object getCache(String key){
        return registCache.get(key);
    }

    protected abstract AppDelegate createAppDelegate();
}
