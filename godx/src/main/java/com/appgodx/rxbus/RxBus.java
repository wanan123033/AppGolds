package com.appgodx.rxbus;

import com.appgodx.rxjava.Dispose;
import com.appgodx.rxjava.Observable;
import com.appgodx.rxjava.Observer;
import com.appgodx.rxjava.Schedulers;
import com.appgodx.rxjava.observable.ObservableTag;
import com.godx.annotation.bus.Subscribetion;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RxBus {
    private Map<String, ObservableTag> observableMap;
    private static RxBus rxBus;
    private RxBus(){
        observableMap = new HashMap<>();
    }
    public static RxBus getDefault(){
        if (rxBus == null){
            synchronized (RxBus.class){
                if (rxBus == null){
                    rxBus = new RxBus();
                }
            }
        }
        return rxBus;
    }

    public void register(Object obj){
        Method[] methods = obj.getClass().getDeclaredMethods();
        for (Method method : methods){
            if (method.isAnnotationPresent(Subscribetion.class)){
                Subscribetion subscribetion = method.getAnnotation(Subscribetion.class);
                if (subscribetion.mode() == Subscribetion.ThreadMode.CHILD){
                    observableMap.put(subscribetion.name(), (ObservableTag) Observable.tag(subscribetion.name()).method(method).obj(obj).subscriOn(Schedulers.io()));
                }else if (subscribetion.mode() == Subscribetion.ThreadMode.MAIN){
                    observableMap.put(subscribetion.name(), (ObservableTag) Observable.tag(subscribetion.name()).method(method).obj(obj).subscriOn(Schedulers.androidMainThread()));
                }else {
                    observableMap.put(subscribetion.name(),Observable.tag(subscribetion.name()).method(method).obj(obj));
                }

            }
        }
    }
    public void post(String name,Object data){
        observableMap.get(name).data(data).subscribe();
    }
    public void unregister(Object obj){
        Method[] methods = obj.getClass().getDeclaredMethods();
        for (Method method : methods){
            if (method.isAnnotationPresent(Subscribetion.class)){
                Subscribetion subscribetion = method.getAnnotation(Subscribetion.class);
                observableMap.remove(subscribetion.name());
            }
        }
    }
}
