package com.appgodx.retrofit;

import com.appgodx.http.Call;
import com.appgodx.rxjava.Observable;

import java.lang.reflect.Type;

public class RxJavaCallAdapterFactory<T> implements CallAdapter.Factory<T, Observable<T>>{
    @Override
    public CallAdapter<T, Observable<T>> get(Type returnType) {
        return new RxJavaCallAdapter<T>();
    }
    static class RxJavaCallAdapter<T> implements CallAdapter<T, Observable<T>> {

        @Override
        public Observable<T> adap(Call<T> call) {
            return Observable.call(call);
        }
    }
}
