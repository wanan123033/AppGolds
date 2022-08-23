package com.appgodx.rxjava;

import com.appgodx.rxjava.schedule.AndroidMainScheduler;
import com.appgodx.rxjava.schedule.IOScheduler;

public class Schedulers {
    private static IOScheduler io;
    private static AndroidMainScheduler androidMainScheduler;
    static {
        io = new IOScheduler();
        androidMainScheduler = new AndroidMainScheduler();
    }
    public static Scheduler io(){
        return io;
    }
    public static Scheduler androidMainThread(){
        return androidMainScheduler;
    }
}
