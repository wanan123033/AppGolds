package com.appgodx.rxjava.observable;

import com.appgodx.rxjava.Dispose;
import com.appgodx.rxjava.DisposeImpl;
import com.appgodx.rxjava.Observable;
import com.appgodx.rxjava.Observer;

import java.lang.reflect.Method;

public class ObservableTag extends Observable<String> {
    private String name;
    private Method method;
    private Object exec;
    private Object data;

    public ObservableTag(String name) {
        this.name = name;
    }



    public ObservableTag method(Method method) {
        this.method = method;
        return this;
    }

    public ObservableTag obj(Object obj) {
        this.exec = obj;
        return this;
    }

    public ObservableTag data(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public void subscribeActual(Observer<? super String> observer) {
        Dispose dispose = new DisposeImpl();
        observer.onSubscribe(dispose);
        try {
            method.invoke(exec,data);
            observer.onNext(name);
        }catch (Exception e){
            e.printStackTrace();
            observer.onError(e);
        }
        observer.onComplete();
    }
}
