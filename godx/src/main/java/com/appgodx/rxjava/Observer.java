package com.appgodx.rxjava;

public interface Observer<T> {
    void onSubscribe(Dispose dispose);
    void onNext(T t) throws Exception;
    void onError(Throwable t);
    void onComplete();
}
