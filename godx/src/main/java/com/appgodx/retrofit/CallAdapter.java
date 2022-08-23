package com.appgodx.retrofit;

import com.appgodx.http.Call;

import java.lang.reflect.Type;

public interface CallAdapter<R,T> {
    T adap(Call<R> call);
    interface Factory<R,T>{
        CallAdapter<R,T> get(Type returnType);
    }
}
