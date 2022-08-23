package com.appgodx.retrofit;

import com.appgodx.http.RequestBuilder;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public abstract class ParameterHandler<T> {
    protected String name;
    protected boolean encode;

    public ParameterHandler(String name, boolean encoded) {

        this.name = name;
        this.encode = encoded;
    }

    abstract void apply(RequestBuilder requestBuilder, T arg);
    public static class Path extends ParameterHandler<String> {
        public Path(String name, boolean encoded) {
            super(name,encoded);
        }

        @Override
        void apply(RequestBuilder requestBuilder, String arg) {
            requestBuilder.addPathParameter(name,arg,encode);
        }
    }

    public static class Url extends ParameterHandler<String> {
        public Url() {
            super(null, false);
        }

        @Override
        void apply(RequestBuilder requestBuilder, String arg) {
            requestBuilder.setUrl(arg);
        }
    }

    public static class Query extends ParameterHandler<String> {
        public Query(String name, boolean encoded) {
            super(name,encoded);
        }

        @Override
        void apply(RequestBuilder requestBuilder, String arg) {
            requestBuilder.addQueryParameter(name,arg,encode);
        }
    }

    public static class Field extends ParameterHandler<String> {
        public Field(String name, boolean encoded) {
            super(name,encoded);
        }

        @Override
        void apply(RequestBuilder requestBuilder, String arg) {
            requestBuilder.addFieldParameter(name,arg,encode);
        }
    }

    public static class JSON extends ParameterHandler<String> {
        public JSON() {
            super(null, false);
        }

        @Override
        void apply(RequestBuilder requestBuilder, String arg) {
            requestBuilder.setRequestBody(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),arg));
        }
    }

    public static class QueryMap extends ParameterHandler<Map<String,String>> {
        public QueryMap(boolean encoded) {
            super(null,encoded);
        }

        @Override
        void apply(RequestBuilder requestBuilder, Map<String, String> arg) {
            Set<String> keySet = arg.keySet();
            for (String name : keySet){
                requestBuilder.addQueryParameter(name,arg.get(name),encode);
            }
        }
    }

    public static class FieldMap extends ParameterHandler<Map<String,String>> {
        public FieldMap(boolean encoded) {
            super(null,encoded);
        }

        @Override
        void apply(RequestBuilder requestBuilder, Map<String, String> arg) {
            Set<String> keySet = arg.keySet();
            for (String name : keySet){
                requestBuilder.addFieldParameter(name,arg.get(name),encode);
            }
        }
    }

    public static class HeaderString extends ParameterHandler<String> {
        public HeaderString(String name) {
            super(name,false);
        }

        @Override
        void apply(RequestBuilder requestBuilder, String arg) {
            requestBuilder.addHeader(name,arg);
        }
    }

    public static class HeaderMap extends ParameterHandler<Map<String,String>> {
        public HeaderMap() {
            super(null, false);
        }

        @Override
        void apply(RequestBuilder requestBuilder, Map<String, String> arg) {
            Set<String> keySet = arg.keySet();
            for (String name : keySet){
                requestBuilder.addHeader(name,arg.get(name));
            }
        }
    }

    public static class Files extends ParameterHandler<List<File>> {
        public Files(String name) {
            super(name,false);
        }

        @Override
        void apply(RequestBuilder requestBuilder, List<File> arg) {
            for (File file : arg){
                requestBuilder.addFile(name,file);
            }
        }
    }

    public static class FileUpload extends ParameterHandler<File> {
        public FileUpload(String name) {
            super(name,false);
        }

        @Override
        void apply(RequestBuilder requestBuilder, File arg) {
            requestBuilder.addFile(name,arg);
        }
    }

    public static class REQUESTBODY extends ParameterHandler<RequestBody> {
        public REQUESTBODY() {
            super(null, false);
        }

        @Override
        void apply(RequestBuilder requestBuilder, RequestBody arg) {
            requestBuilder.setRequestBody(arg);
        }
    }
}
