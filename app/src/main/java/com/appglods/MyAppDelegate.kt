package com.appglods

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.appgodx.appdelegate.AppDelegate
import com.appgodx.base.BaseApplication
import com.appgodx.virtual.ClassLoaderUtil
import com.appgodx.virtual.PluginManager
import com.godx.annotation.router.AutoService

@AutoService(AppDelegate::class)
open class MyAppDelegate : AppDelegate {
    var application:Application? = null
    override fun attachBaseContext(baseApplication: BaseApplication?, base: Context?) {
        application = baseApplication
    }

    override fun onCreate() {
//        ClassLoaderUtil.loadDex(application,"/sdcard/Download/plugin-debug.apk",false)
//        PluginManager.getInstance(application).loadApk("/sdcard/Download/plugin-debug.apk")
    }

    override fun onLowMemory() {
    }

    override fun onTrimMemory(level: Int) {
    }

    override fun onTerminate() {
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
    }
}