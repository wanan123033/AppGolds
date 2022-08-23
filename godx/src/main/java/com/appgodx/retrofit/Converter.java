package com.appgodx.retrofit;

import java.io.IOException;
import java.lang.reflect.Type;

public interface Converter<T,R> {
    R convert(T t) throws IOException;
    public interface Factory<T,R>{
        Converter<T,R> get(Type returnType);
    }
}
