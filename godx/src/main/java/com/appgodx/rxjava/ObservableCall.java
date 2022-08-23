package com.appgodx.rxjava;

import com.appgodx.http.Call;
import com.appgodx.http.CallBack;

import java.io.IOException;

public class ObservableCall<T> extends Observable<T> {
    private final Call<T> call;

    public ObservableCall(Call<T> call) {
        this.call = call;
    }

    @Override
    public void subscribeActual(Observer<? super T> observer) {
        call.enqueue(new CallBack<T>() {
            @Override
            public void onFailure(Call<T> call, IOException e) {
                observer.onError(e);
            }

            @Override
            public void onResponse(Call<T> call, T response) throws IOException {
                try {
                    observer.onNext(response);
                } catch (Exception e) {
                    observer.onError(e);
                }
                observer.onComplete();
            }
        });
    }
}
