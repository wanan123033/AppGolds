package com.appgodx.glide;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class ActivityFragmentLifecycle implements Lifecycle {
    private Set<LifecycleListener> lifecycleListenerSet;
    public ActivityFragmentLifecycle(){
        lifecycleListenerSet = new HashSet<>();
    }

    public void onStart(){
        for (LifecycleListener listener : lifecycleListenerSet){
            listener.onStart();
        }
    }

    public void onStop() {
        for (LifecycleListener listener : lifecycleListenerSet){
            listener.onStop();
        }
    }

    public void onDestory() {
        for (LifecycleListener listener : lifecycleListenerSet){
            listener.onDestory();
        }
    }

    @Override
    public void addListener(@NonNull LifecycleListener listener) {
        lifecycleListenerSet.add(listener);
    }

    @Override
    public void removeListener(@NonNull LifecycleListener listener) {
        lifecycleListenerSet.remove(listener);
    }
}
