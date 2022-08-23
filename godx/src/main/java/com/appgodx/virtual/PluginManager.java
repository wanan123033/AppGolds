package com.appgodx.virtual;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.os.Build;
import android.os.Handler;

import com.appgodx.Reflector;
import com.appgodx.virtual.system.PluginApk;
import com.appgodx.virtual.system.VLInstrumentation;

import java.util.HashMap;
import java.util.Map;


public class PluginManager {
    private static PluginManager pluginManager;
    private Context hostContext;
    private Application hostApplication;
    private Map<String,PluginApk> pluginApkMap;
    public static PluginManager getInstance(Context context){
        if (pluginManager == null){
            synchronized (PluginManager.class){
                if (pluginManager == null){
                    pluginManager = new PluginManager(context);
                }
            }
        }
        return pluginManager;
    }
    public void loadApk(String apkPath){
        PluginApk pluginApk = new PluginApk(this,apkPath);
        pluginApkMap.put(pluginApk.getPackageName(),pluginApk);

    }

    public PluginApk getPluginApk(String packageName){
        return pluginApkMap.get(packageName);
    }
    private PluginManager(Context context) {
        if (context instanceof Application){
            hostApplication = (Application) context;
            hostContext = hostApplication.getBaseContext();
        }else {
            hostContext = context.getApplicationContext();
            hostApplication = (Application) hostContext;
        }
        pluginApkMap = new HashMap<>();
        hookCurrentProcess();
    }

    private void hookCurrentProcess() {
        hookInstrumentationAndHandler();
        hookAMS();
    }

    private void hookInstrumentationAndHandler() {
        try {
            Object activityThread = Reflector.on("android.app.ActivityThread").method("currentActivityThread").call();
            Reflector field = Reflector.with(activityThread).field("mInstrumentation");
            Instrumentation instrumentation = Reflector.with(activityThread).field("mInstrumentation").get();
            VLInstrumentation vlInstrumentation = createInstrumentation(instrumentation);
            field.set(activityThread,vlInstrumentation);

//            Handler mainHandler = Reflector.with(activityThread).method("getHandler").call();

        } catch (Reflector.ReflectedException e) {
            e.printStackTrace();
        }
    }

    private VLInstrumentation createInstrumentation(Instrumentation instrumentation) {
        return new VLInstrumentation(this,instrumentation);
    }

    private void hookAMS() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

        }
    }

    public Context getHostContext() {
        return hostContext;
    }
}
