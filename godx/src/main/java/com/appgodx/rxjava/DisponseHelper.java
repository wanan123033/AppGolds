package com.appgodx.rxjava;

public class DisponseHelper{

    public static synchronized void dispose(Dispose dispose) {
        dispose.dispose();
    }


    public static synchronized boolean isDispose(Dispose dispose) {
        return dispose.isDispose();
    }
}
