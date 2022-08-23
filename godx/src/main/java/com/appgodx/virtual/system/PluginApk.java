package com.appgodx.virtual.system;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.appgodx.Reflector;
import com.appgodx.virtual.PluginManager;

import java.io.File;

import dalvik.system.DexClassLoader;

public class PluginApk {
    private DexClassLoader classLoader;
    private Object mPackage;
    private PackageInfo packageInfo;
    private Resources resources;
    public PluginApk(PluginManager pluginManager,String apkPath){
        mPackage = PackageParserCompat.parsePackage(pluginManager.getHostContext(), new File(apkPath), 1 << 2);
        classLoader = new DexClassLoader(apkPath,pluginManager.getHostContext().getCacheDir().getAbsolutePath(),null,pluginManager.getHostContext().getClassLoader());
        packageInfo = new PackageInfo();
        try {
            packageInfo.applicationInfo = Reflector.with(mPackage).field("applicationInfo").get();
            packageInfo.applicationInfo.sourceDir = apkPath;
            packageInfo.packageName = Reflector.with(mPackage).field("packageName").get();
            resources = createResource(pluginManager.getHostContext(),apkPath);
        } catch (Reflector.ReflectedException e) {
            e.printStackTrace();
        }
    }

    private Resources createResource(Context hostContext, String apkPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Reflector.with(assetManager).method("addAssetPath",String.class).call(apkPath);
            return new Resources(assetManager,hostContext.getResources().getDisplayMetrics(),hostContext.getResources().getConfiguration());
        } catch (IllegalAccessException | InstantiationException | Reflector.ReflectedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPackageName(){
        return packageInfo.packageName;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public Resources getResource() {
        return resources;
    }
}
