package com.appgodx;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * 通过生命周期感知
 *   有效防止数据丢失及不必要的内存泄漏。
 */
public final class LifecycleHandler extends Handler implements LifecycleObserver {
    private LifecycleOwner owner;
    public LifecycleHandler(Looper looper, LifecycleOwner owner){
        super(looper);
        this.owner = owner;
        addObserver();
    }

    public LifecycleHandler(Looper looper, Callback callback, LifecycleOwner owner){
        super(looper, callback);
        this.owner = owner;
        addObserver();
    }
    private void addObserver() {
        owner.getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestory(LifecycleOwner owner){
        removeCallbacksAndMessages(null);
        owner.getLifecycle().removeObserver(this);
    }

}
