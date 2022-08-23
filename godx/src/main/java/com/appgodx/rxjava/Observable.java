package com.appgodx.rxjava;



import com.appgodx.http.Call;
import com.appgodx.rxjava.observable.ObservableJust;
import com.appgodx.rxjava.observable.ObservableMap;
import com.appgodx.rxjava.observable.ObservableObserverOn;
import com.appgodx.rxjava.observable.ObservableSubscriOn;
import com.appgodx.rxjava.observable.ObservableTag;

import java.lang.reflect.Method;

public abstract class Observable<T> implements ObservableSource<T> {
    public static <T> Observable<T> just(T t){
        return RxJavaPlugin.onApply(new ObservableJust<T>(t));
    }

    public static <T> Observable<T> call(Call<T> call) {
        return RxJavaPlugin.onApply(new ObservableCall<T>(call));
    }

    public static ObservableTag tag(String name) {
        return (ObservableTag) RxJavaPlugin.onApply(new ObservableTag(name));
    }
    public <R> Observable<R> map(Function<T,R> function){
        return RxJavaPlugin.onApply(new ObservableMap<>(this,function));
    }
    public Observable<T> observerOn(Scheduler scheduler){
        return RxJavaPlugin.onApply(new ObservableObserverOn<T>(this,scheduler));
    }
    public Observable<T> subscriOn(Scheduler scheduler){
        return RxJavaPlugin.onApply(new ObservableSubscriOn<T>(this,scheduler));
    }
    public void subscribe(Observer<T> observer){
        subscribeActual(observer);
    }
    public void subscribe(){
        subscribeActual(new LambdaObserver<T>());
    }

}
