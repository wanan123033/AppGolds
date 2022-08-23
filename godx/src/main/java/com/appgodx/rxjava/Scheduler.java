package com.appgodx.rxjava;

import com.appgodx.rxjava.observable.ObservableObserverOn;

public interface Scheduler {
    Worker createWorker();
    interface Worker{
        void schedule(Runnable runnable);
    }
}
