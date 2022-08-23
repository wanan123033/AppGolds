package com.appgodx.retrofit;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;

public class GsonConverterFactory<T> implements Converter.Factory<ResponseBody,T> {
    private final Gson gson;

    public GsonConverterFactory(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, T> get(Type returnType) {
        TypeAdapter<T> adapter = (TypeAdapter<T>) gson.getAdapter(TypeToken.get(returnType));
        return new GsonResponseBodyConverter<T>(gson, adapter);
    }
    static class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

        private final Gson gson;
        private final TypeAdapter<T> adapter;

        public GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override
        public T convert(ResponseBody value) throws IOException {
            JsonReader jsonReader = gson.newJsonReader(value.charStream());
            try {
                T result = adapter.read(jsonReader);
                if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                    throw new JsonIOException("JSON document was not fully consumed.");
                }
                return result;
            } finally {
                value.close();
            }
        }
    }
}
