package com.appgodx.rxjava.schedule;

import android.os.Handler;
import android.os.Looper;

import com.appgodx.rxjava.Scheduler;

public class AndroidMainScheduler implements Scheduler {
    @Override
    public Worker createWorker() {
        return new HandlerWorker();
    }
    private static class HandlerWorker implements Worker{
        private Handler handler = new Handler(Looper.getMainLooper());
        @Override
        public void schedule(Runnable runnable) {
            handler.post(runnable);
        }
    }
}
