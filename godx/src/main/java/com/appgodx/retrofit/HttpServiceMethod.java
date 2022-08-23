package com.appgodx.retrofit;

import com.appgodx.http.OkHttpCall;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.ResponseBody;

public class HttpServiceMethod<ResponseT, ReturnT> extends ServiceMethod<ReturnT> {
    private CallAdapter<ResponseT, ReturnT> callAdapter;
    private RequestFactory requestFactory;
    private Call.Factory callFactory;
    Converter<ResponseBody, ResponseT> converter;

    public HttpServiceMethod(CallAdapter<ResponseT, ReturnT> callAdapter, Converter<ResponseBody, ResponseT> converter, RequestFactory requestFactory, Call.Factory callFactory) {
        this.callAdapter = callAdapter;
        this.converter = converter;
        this.requestFactory = requestFactory;
        this.callFactory = callFactory;
    }

    public static <ResponseT, ReturnT> HttpServiceMethod<ResponseT, ReturnT> parseAnnotations(Retrofit retrofit, Method method, RequestFactory requestFactory) {
        Type returnType = method.getGenericReturnType();
        CallAdapter<ResponseT, ReturnT> callAdapter = retrofit.findCallAdapter(returnType);
        Converter<ResponseBody, ResponseT> converter = retrofit.findConverter(returnType);
        okhttp3.Call.Factory callFactory = retrofit.callFactory;
        return new HttpServiceMethod<>(callAdapter,converter,requestFactory,callFactory);
    }

    @Override
    public ReturnT invoke(Object proxy, Object[] args) {
        return callAdapter.adap(new OkHttpCall<>(requestFactory,args,callFactory,converter));
    }
}
