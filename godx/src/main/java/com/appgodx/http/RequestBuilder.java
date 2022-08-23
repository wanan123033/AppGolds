package com.appgodx.http;

import java.io.File;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RequestBuilder {
    private HttpUrl url;
    private String relativeUrl;
    private String httpMethod;
    private Headers.Builder headers;
    private RequestBody requestBody;
    private FormBody.Builder formBody;
    private MultipartBody.Builder multipartBody;

    public RequestBuilder(String httpMethod, String relativeUrl,
                          String baseUrl,
                          Headers.Builder headers,
                          boolean isMultiPart,
                          MediaType mediaType,
                          boolean isFormUrlEncoded) {
        this.httpMethod = httpMethod;
        this.headers = headers;
        if (isMultiPart){
            multipartBody = new MultipartBody.Builder();
            multipartBody.setType(mediaType);
        }else if (isFormUrlEncoded){
            formBody = new FormBody.Builder();
        }
        this.relativeUrl = baseUrl + relativeUrl;
        url = HttpUrl.parse(this.relativeUrl);
    }
    public Request build(){
        if (requestBody == null){
            if (formBody != null){
                requestBody = formBody.build();
            }else if (multipartBody != null){
                requestBody = multipartBody.build();
            }
        }
        return new Request.Builder().url(url).headers(headers.build()).method(httpMethod,requestBody).build();
    }

    public void addPathParameter(String name, String arg, boolean encode) {
        this.relativeUrl.replace("{"+name+"}",arg);
        url = HttpUrl.parse(this.relativeUrl);
    }

    public void setUrl(String url) {
        this.url = HttpUrl.parse(url);
    }

    public void addQueryParameter(String name, String value, boolean encode) {
        HttpUrl.Builder builder = url.newBuilder();
        if (encode){
            builder.addEncodedQueryParameter(name,value);
        }else {
            builder.addQueryParameter(name,value);
        }
    }

    public void addFieldParameter(String name, String value, boolean encode) {
        if (formBody != null) {
            if (encode) {
                formBody.addEncoded(name, value);
            } else {
                formBody.add(name, value);
            }
        }else if (multipartBody != null){
            multipartBody.addFormDataPart(name, value);
        }
    }

    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    public void addHeader(String name, String value) {
        headers.add(name,value);
    }

    public void addFile(String name, File file) {
        if (multipartBody != null){
            multipartBody.addFormDataPart(name,file.getName(),RequestBody.create(MediaType.parse("application/octet-stream"),file));
        }
    }
}
