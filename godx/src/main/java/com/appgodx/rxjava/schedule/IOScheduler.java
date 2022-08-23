package com.appgodx.rxjava.schedule;

import com.appgodx.rxjava.Scheduler;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IOScheduler implements Scheduler {
    @Override
    public Worker createWorker() {
        return new IOWorker();
    }
    private static class IOWorker implements Worker {
        private ExecutorService executorService;
        public IOWorker(){
            executorService = Executors.newCachedThreadPool();
        }

        @Override
        public void schedule(Runnable runnable) {
            executorService.execute(runnable);
        }
    }
}
