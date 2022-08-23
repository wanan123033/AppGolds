package com.appgodx.rxjava;

public class DisposeImpl implements Dispose{
    boolean dispose = true;
    @Override
    public void dispose() {
        dispose = !dispose;
    }

    @Override
    public boolean isDispose() {
        return dispose;
    }
}
