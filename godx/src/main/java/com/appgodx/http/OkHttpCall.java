package com.appgodx.http;

import com.appgodx.retrofit.Converter;
import com.appgodx.retrofit.RequestFactory;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Timeout;

public class OkHttpCall<T> implements Call<T>{
    private final RequestFactory requestFactory;
    private final Object[] args;
    private final okhttp3.Call.Factory callFactory;
    private final Converter<ResponseBody, T> converter;

    private okhttp3.Call rawCall;

    public OkHttpCall(RequestFactory requestFactory, Object[] args, okhttp3.Call.Factory callFactory, Converter<ResponseBody,T> converter){
        this.requestFactory = requestFactory;
        this.args = args;
        this.callFactory = callFactory;
        this.converter = converter;
    }
    @Override
    public Request request() {
        return getRawCall().request();
    }

    private okhttp3.Call getRawCall() {
        if (rawCall != null){
            return rawCall;
        }
        rawCall = createCall();
        return rawCall;
    }

    private okhttp3.Call createCall() {
        okhttp3.Call call = callFactory.newCall(requestFactory.create(args));
        if (call == null){
            throw new NullPointerException("call is null");
        }
        return call;
    }

    @Override
    public Response execute() throws IOException {
        return getRawCall().execute();
    }

    @Override
    public void enqueue(CallBack<T> responseCallback) {
        getRawCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                if (responseCallback != null){
                    responseCallback.onFailure(OkHttpCall.this,e);
                }
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.code() < 200 || response.code()>=300){
                    responseCallback.onFailure(OkHttpCall.this,new IOException("response code="+response.code()));
                    return;
                }

                ResponseBody rawBody = response.body();
                T result = converter.convert(rawBody);
                if (responseCallback != null){
                    responseCallback.onResponse(OkHttpCall.this,result);
                }
            }
        });
    }


    @Override
    public void cancel() {
        getRawCall().cancel();
    }

    @Override
    public boolean isExecuted() {
        return getRawCall().isExecuted();
    }

    @Override
    public boolean isCanceled() {
        return getRawCall().isCanceled();
    }

    @Override
    public Timeout timeout() {
        return getRawCall().timeout();
    }

    @Override
    public Call<T> clone() {
        return new OkHttpCall<T>(requestFactory,args,callFactory,converter);
    }
}
