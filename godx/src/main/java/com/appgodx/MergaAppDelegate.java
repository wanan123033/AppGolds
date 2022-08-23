package com.appgodx;

import android.content.Context;
import android.content.res.Configuration;

import com.appgodx.appdelegate.AppDelegate;
import com.appgodx.base.BaseApplication;
import com.appgodx.virtual.ClassLoaderUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class MergaAppDelegate implements AppDelegate {
    private List<AppDelegate> appDelegates;
    @Override
    public void attachBaseContext(BaseApplication baseApplication, Context base) {
        ServiceLoader<AppDelegate> loader = ServiceLoader.load(AppDelegate.class);
        appDelegates = new ArrayList<>();
        Iterator<AppDelegate> iterator = loader.iterator();
        while (iterator.hasNext()){
            appDelegates.add(iterator.next());
        }
        for (AppDelegate appDelegate : appDelegates){
            appDelegate.attachBaseContext(baseApplication,base);
        }
    }

    @Override
    public void onCreate() {

        for (AppDelegate appDelegate : appDelegates){
            appDelegate.onCreate();
        }
    }

    @Override
    public void onLowMemory() {
        for (AppDelegate appDelegate : appDelegates){
            appDelegate.onLowMemory();
        }
    }

    @Override
    public void onTrimMemory(int level) {
        for (AppDelegate appDelegate : appDelegates){
            appDelegate.onTrimMemory(level);
        }
    }

    @Override
    public void onTerminate() {
        for (AppDelegate appDelegate : appDelegates){
            appDelegate.onTerminate();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        for (AppDelegate appDelegate : appDelegates){
            appDelegate.onConfigurationChanged(newConfig);
        }
    }
}
