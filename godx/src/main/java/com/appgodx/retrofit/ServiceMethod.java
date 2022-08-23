package com.appgodx.retrofit;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class ServiceMethod<T> {
    public static ServiceMethod<?> parserAnnotations(Method method, Retrofit retrofit) {
        Type returnType = method.getGenericReturnType();
        if (returnType == void.class){
            throw new IllegalArgumentException("method return type is void");
        }
        if (!(returnType instanceof ParameterizedType)){
            throw new IllegalArgumentException("method return type is"+returnType);
        }
        RequestFactory requestFactory = RequestFactory.parseAnnotations(method,retrofit);
        return HttpServiceMethod.parseAnnotations(retrofit, method, requestFactory);
    }

    public abstract T invoke(Object proxy, Object[] args);
}
