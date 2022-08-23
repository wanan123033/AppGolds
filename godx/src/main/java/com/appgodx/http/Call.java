package com.appgodx.http;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;
import okio.Timeout;

public interface Call<T> {
    Request request();
    Response execute() throws IOException;
    void enqueue(CallBack<T> responseCallback);
    void cancel();
    boolean isExecuted();

    boolean isCanceled();
    Timeout timeout();
    Call<T> clone();
}
