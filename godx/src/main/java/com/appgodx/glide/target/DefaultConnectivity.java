package com.appgodx.glide.target;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.appgodx.glide.LifecycleListener;
import com.appgodx.glide.NetworkBorcastRecevier;

public class DefaultConnectivity implements LifecycleListener {
    private final Context applicationContext;
    private NetworkBorcastRecevier recevier;

    public DefaultConnectivity(Context applicationContext) {
        this.applicationContext = applicationContext;
        recevier = new NetworkBorcastRecevier();
    }

    @Override
    public void onStart() {
        applicationContext.registerReceiver(recevier,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onStop() {
        applicationContext.unregisterReceiver(recevier);
    }

    @Override
    public void onDestory() {

    }
}
