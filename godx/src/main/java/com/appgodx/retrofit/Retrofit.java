package com.appgodx.retrofit;

import com.google.gson.Gson;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

public class Retrofit {
    public String baseUrl;
    public Call.Factory callFactory;
    private List<CallAdapter.Factory> callAdapterFactories;
    private List<Converter.Factory> converterFactories;
    private Map<Class<?>,Object> proxyCache;
    private Map<Method,ServiceMethod<?>> serviceMethodCache;
    private Retrofit(Build build) {
        baseUrl = build.baseUrl;
        callFactory = build.callFactory;
        callAdapterFactories = build.callAdapterFactories;
        converterFactories = build.converterFactories;

        proxyCache = new HashMap<>();
        serviceMethodCache = new HashMap<>();
    }
    public <T> T create(Class<T> clazz){
        T proxy = (T) proxyCache.get(clazz);
        if (proxy != null){
            return proxy;
        }
        synchronized (this){
            proxy = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return loadServiceMethod(method).invoke(proxy,args);
                }
            });
            proxyCache.put(clazz,proxy);
            return proxy;
        }
    }

    private ServiceMethod<?> loadServiceMethod(Method method) {
        ServiceMethod<?> serviceMethod = serviceMethodCache.get(method);
        if (serviceMethod != null){
            return serviceMethod;
        }
        synchronized (this){
            serviceMethod = ServiceMethod.parserAnnotations(method,this);
            serviceMethodCache.put(method,serviceMethod);
            return serviceMethod;
        }
    }

    public <T> Converter<ResponseBody, T> findConverter(Type returnType) {
        for (Converter.Factory factory : converterFactories){
            Converter converter = factory.get(returnType);
            if (converter != null){
                return converter;
            }
        }
        return new GsonConverterFactory<T>(new Gson()).get(returnType);
    }

    public <R, T> CallAdapter<R, T> findCallAdapter(Type returnType) {
        for (CallAdapter.Factory factory : callAdapterFactories){
            CallAdapter converter = factory.get(returnType);
            if (converter != null){
                return converter;
            }
        }
        return new RetrofitCallAdapterFactory().get(returnType);
    }

    public static class Build{
        private String baseUrl;
        private Call.Factory callFactory;
        private List<CallAdapter.Factory> callAdapterFactories = new ArrayList<>();
        private List<Converter.Factory> converterFactories = new ArrayList<>();
        public Build baseUrl(String baseUrl){
            this.baseUrl = baseUrl;
            return this;
        }
        public Build client(OkHttpClient client){
            return callFactory(client);
        }

        public Build callFactory(Call.Factory factory) {
            this.callFactory = factory;
            return this;
        }
        public Build addConverterFactory(Converter.Factory factory){
            this.converterFactories.add(factory);
            return this;
        }
        public Build addCallAdapterFactory(CallAdapter.Factory factory){
            this.callAdapterFactories.add(factory);
            return this;
        }
        public Retrofit build(){
            return new Retrofit(this);
        }
    }
}
