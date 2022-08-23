package com.appgodx.retrofit;

import com.appgodx.http.RequestBuilder;

import okhttp3.Request;

public class HttpRequestFactory extends RequestFactory {
    private Builder builder;
    public HttpRequestFactory(Builder builder) {
        super(builder);
    }

    @Override
    public Request create(Object[] args) {
        RequestBuilder requestBuilder = getRequestBuilder();
        ParameterHandler[] parameterHandlers = getParameterHandlers();
        for (int i = 0 ; i < args.length ; i++){
            parameterHandlers[i].apply(requestBuilder,args[i]);
        }
        return requestBuilder.build();
    }
}
