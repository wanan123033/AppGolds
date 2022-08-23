package com.appgodx.retrofit;

import com.appgodx.http.Call;

import java.lang.reflect.Type;

public class RetrofitCallAdapterFactory<T> implements CallAdapter.Factory<T,Call<T>>,CallAdapter<T,Call<T>>{

    @Override
    public Call<T> adap(Call<T> call) {
        return call;
    }

    @Override
    public CallAdapter<T, Call<T>> get(Type returnType) {
        return new RetrofitCallAdapterFactory<T>();
    }
}
