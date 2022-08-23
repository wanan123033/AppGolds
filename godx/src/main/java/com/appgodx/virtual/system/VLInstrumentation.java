package com.appgodx.virtual.system;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.UserHandle;
import android.util.Log;

import com.appgodx.Reflector;
import com.appgodx.virtual.PluginManager;
import com.appgodx.virtual.ProxyActivity;

import dalvik.system.DexClassLoader;

public class VLInstrumentation extends Instrumentation {
    private PluginManager pluginManager;
    private Instrumentation instrumentation;
    private static final String TARGET = "target_intent";

    public VLInstrumentation(PluginManager pluginManager, Instrumentation instrumentation) {
        this.pluginManager = pluginManager;
        this.instrumentation = instrumentation;
    }
    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {
        intent = injectIntent(intent);
        try {
            return Reflector.with(instrumentation).method("execStartActivity",Context.class,IBinder.class,IBinder.class,Activity.class,
                    Intent.class,int.class,Bundle.class).call(who,contextThread,token,target,intent,requestCode,options);
        } catch (Reflector.ReflectedException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, String target,
            Intent intent, int requestCode, Bundle options) {
        intent = injectIntent(intent);
        try {
            return Reflector.with(instrumentation).method("execStartActivity",Context.class,IBinder.class,IBinder.class,String.class,
                    Intent.class,int.class,Bundle.class)
                    .call(who,contextThread,token,target,intent,requestCode,options);
        } catch (Reflector.ReflectedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, String resultWho,
            Intent intent, int requestCode, Bundle options, UserHandle user) {
        intent = injectIntent(intent);
        try {
            return Reflector.with(instrumentation).method("execStartActivity",Context.class,IBinder.class,IBinder.class,String.class,
                    Intent.class,int.class,Bundle.class,UserHandle.class)
                    .call(who,contextThread,token,resultWho,intent,requestCode,options,user);
        } catch (Reflector.ReflectedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ComponentName component = intent.getComponent();
        if(component != null){
            if (component.getPackageName().equals(pluginManager.getHostContext().getPackageName()) && component.getClassName().equals(ProxyActivity.class.getName())){
                intent = intent.getParcelableExtra(TARGET);
                Activity activity = null;
                try {
                    activity = newActivity(intent);
                } catch (Reflector.ReflectedException e) {
                    e.printStackTrace();
                }
                if (activity != null){
                    return activity;
                }
            }
        }
        return super.newActivity(cl, className, intent);
    }

    private Activity newActivity(Intent intent) throws Reflector.ReflectedException {
        ComponentName component = intent.getComponent();
        if (component != null){
            String packageName = component.getPackageName();
            PluginApk pluginApk = pluginManager.getPluginApk(packageName);
            if (pluginApk != null){
                Activity activity = null;
                try {
                    activity = (Activity) pluginApk.getClassLoader().loadClass(intent.getComponent().getClassName()).newInstance();
                } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Reflector.with(activity).field("mResources").set(pluginApk.getResource());
                activity.setIntent(intent);
                return activity;
            }
        }
        return null;
    }

    private Intent injectIntent(Intent intent) {
        //查看启动的Activity与宿主的包名是否一致
        ComponentName component = intent.getComponent();
        if (component != null && component.getPackageName().equals(pluginManager.getHostContext().getPackageName())) { //判断启动的是不是宿主的Activity
            return intent;
        }else {
            Intent proxyIntent = new Intent(pluginManager.getHostContext(), ProxyActivity.class);
            proxyIntent.putExtra(TARGET,intent);
            return proxyIntent;
        }
    }
}
