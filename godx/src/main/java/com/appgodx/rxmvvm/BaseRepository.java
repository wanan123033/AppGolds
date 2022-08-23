package com.appgodx.rxmvvm;

import com.appgodx.http.Call;
import com.appgodx.http.CallBack;
import com.appgodx.rxjava.Dispose;
import com.appgodx.rxjava.Observable;
import com.appgodx.rxjava.Observer;
import com.appgodx.rxjava.Schedulers;

import java.io.IOException;

public abstract class BaseRepository<T> implements Observer<T>, CallBack<T> {

    public void addHttpObservable(Observable<T> observable){
        observable.observerOn(Schedulers.io()).subscriOn(Schedulers.androidMainThread()).subscribe(this);
    }
    public void addCallRequest(Call<T> call){
        call.enqueue(this);
    }

    @Override
    public void onSubscribe(Dispose dispose) {

    }

    public abstract void onNext(T response);

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public final void onFailure(Call<T> call, IOException e) {
        onError(e);
    }

    @Override
    public final void onResponse(Call<T> call, T response) throws IOException {
        onNext(response);
    }
}
