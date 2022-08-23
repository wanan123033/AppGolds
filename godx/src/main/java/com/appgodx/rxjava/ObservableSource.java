package com.appgodx.rxjava;

public interface ObservableSource<T> {
    void subscribeActual(Observer<? super T> observer);
}
