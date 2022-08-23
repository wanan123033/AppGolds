package com.appgodx.retrofit;

import com.appgodx.http.RequestBuilder;
import com.godx.annotation.retrofit.CustomRequestBody;
import com.godx.annotation.retrofit.Field;
import com.godx.annotation.retrofit.FieldMap;
import com.godx.annotation.retrofit.FileUpload;
import com.godx.annotation.retrofit.Files;
import com.godx.annotation.retrofit.FormUrlEncoded;
import com.godx.annotation.retrofit.HTTP;
import com.godx.annotation.retrofit.HeaderMap;
import com.godx.annotation.retrofit.HeaderString;
import com.godx.annotation.retrofit.Headers;
import com.godx.annotation.retrofit.JSON;
import com.godx.annotation.retrofit.JSONRequest;
import com.godx.annotation.retrofit.Multipart;
import com.godx.annotation.retrofit.Path;
import com.godx.annotation.retrofit.Query;
import com.godx.annotation.retrofit.QueryMap;
import com.godx.annotation.retrofit.REQUESTBODY;
import com.godx.annotation.retrofit.Url;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;

public abstract class RequestFactory {
    private static final String PARAM = "[a-zA-Z][a-zA-Z0-9_-]*";
    private static final Pattern PARAM_URL_REGEX = Pattern.compile("\\{(" + PARAM + ")\\}");
    private static final Pattern PARAM_NAME_REGEX = Pattern.compile(PARAM);
    private final Builder builder;

    private RequestBuilder requestBuilder;
    private ParameterHandler[] parameterHandlers;

    public RequestFactory(Builder builder) {
        this.builder = builder;
        parameterHandlers = builder.parameterHandlers;
    }

    public static RequestFactory parseAnnotations(Method method, Retrofit retrofit) {
        return new Builder(method,retrofit).build();
    }
    protected RequestBuilder getRequestBuilder(){
        if (requestBuilder == null){
            requestBuilder = createRequestBuilder(builder);
        }
        return requestBuilder;
    }

    private RequestBuilder createRequestBuilder(Builder builder) {
        return new RequestBuilder(builder.httpMethod,builder.relativeUrl,
                builder.retrofit.baseUrl,
                builder.headers,
                builder.isMultiPart,
                builder.mediaType,
                builder.isFormUrlEncoded);
    }

    public abstract Request create(Object[] args);

    public ParameterHandler[] getParameterHandlers() {
        return parameterHandlers;
    }

    static final class Builder{

        private Method method;
        private Retrofit retrofit;
        private Annotation[] methodAnnotations;
        private Type[] parameterTypes;
        private Annotation[][] parameterAnnotations;
        private okhttp3.Headers.Builder headers;
        private boolean isMultiPart;
        private boolean isFormUrlEncoded;
        private boolean isCustomRequestBody;
        private boolean isJson;
        private String httpMethod;
        private boolean hasBody;
        private String relativeUrl;
        private MediaType mediaType;
        private ParameterHandler[] parameterHandlers;

        public Builder(Method method, Retrofit retrofit) {
            this.method = method;
            this.retrofit = retrofit;

            methodAnnotations = method.getAnnotations();
            parameterTypes = method.getGenericParameterTypes();
            parameterAnnotations = method.getParameterAnnotations();
        }

        public RequestFactory build() {
            for (Annotation methodAnno : methodAnnotations){
                parserMethodAnnotation(methodAnno);
            }
            parameterHandlers = new ParameterHandler[parameterAnnotations.length];
            for (int i = 0 ; i < parameterAnnotations.length ; i++){
                parameterHandlers[i] = parserParameterHandler(parameterAnnotations[i]);
            }
            return new HttpRequestFactory(this);
        }

        private ParameterHandler parserParameterHandler(Annotation[] parameterAnnotation) {
            if (parameterAnnotation.length != 1){
                throw new IllegalArgumentException("parameter annotation length is 1."+method.getName());
            }
            if (parameterAnnotation[0] instanceof Path){
                return new ParameterHandler.Path(((Path) parameterAnnotation[0]).value(),((Path) parameterAnnotation[0]).encoded());
            }else if (parameterAnnotation[0] instanceof Url){
                return new ParameterHandler.Url();
            }else if (parameterAnnotation[0] instanceof Query){
                return new ParameterHandler.Query(((Query) parameterAnnotation[0]).value(),((Query) parameterAnnotation[0]).encoded());
            }else if (parameterAnnotation[0] instanceof Field){
                return new ParameterHandler.Field(((Field) parameterAnnotation[0]).value(),((Field) parameterAnnotation[0]).encoded());
            }else if (parameterAnnotation[0] instanceof JSON){
                return new ParameterHandler.JSON();
            }else if (parameterAnnotation[0] instanceof QueryMap){
                return new ParameterHandler.QueryMap(((QueryMap) parameterAnnotation[0]).encoded());
            }else if (parameterAnnotation[0] instanceof FieldMap){
                return new ParameterHandler.FieldMap(((FieldMap) parameterAnnotation[0]).encoded());
            }else if (parameterAnnotation[0] instanceof HeaderString){
                return new ParameterHandler.HeaderString(((HeaderString) parameterAnnotation[0]).value());
            }else if (parameterAnnotation[0] instanceof HeaderMap){
                return new ParameterHandler.HeaderMap();
            }else if (parameterAnnotation[0] instanceof Files){
                return new ParameterHandler.Files(((Files) parameterAnnotation[0]).value());
            }else if (parameterAnnotation[0] instanceof FileUpload){
                return new ParameterHandler.FileUpload(((FileUpload) parameterAnnotation[0]).value());
            }else if (parameterAnnotation[0] instanceof REQUESTBODY){
                return new ParameterHandler.REQUESTBODY();
            }
            return null;
        }

        private void parserMethodAnnotation(Annotation methodAnno) {
            if (methodAnno instanceof HTTP){
                HTTP http = (HTTP) methodAnno;
                if (http.method() == HTTP.way.POST){
                    parserHttpMethodAndPath("POST",http.url(),true);
                }else if (http.method() == HTTP.way.DELETE){
                    parserHttpMethodAndPath("DELETE",http.url(),false);
                }else if (http.method() == HTTP.way.GET){
                    parserHttpMethodAndPath("GET",http.url(),false);
                }else if (http.method() == HTTP.way.HEAD){
                    parserHttpMethodAndPath("HEAD",http.url(),false);
                }else if (http.method() == HTTP.way.OPTIONS){
                    parserHttpMethodAndPath("OPTIONS",http.url(),false);
                }else if (http.method() == HTTP.way.PATCH){
                    parserHttpMethodAndPath("PATCH",http.url(),true);
                }else if (http.method() == HTTP.way.PUT){
                    parserHttpMethodAndPath("PUT",http.url(),true);
                }
            }else if (methodAnno instanceof Headers){
                String[] headers = ((Headers) methodAnno).value();
                this.headers = parserHeaders(headers);
            }else if (methodAnno instanceof Multipart){
                if (isJson || isCustomRequestBody || isFormUrlEncoded){
                    throw new IllegalArgumentException("Only one encoding annotation is allowed."+method.getName());
                }
                isMultiPart = true;
                Multipart.MediaType mediaType = ((Multipart) methodAnno).type();
                if (mediaType == Multipart.MediaType.FORM){
                    this.mediaType = MultipartBody.FORM;
                }else if (mediaType == Multipart.MediaType.ALTERNATIVE){
                    this.mediaType = MultipartBody.ALTERNATIVE;
                }else if (mediaType == Multipart.MediaType.DIGEST){
                    this.mediaType = MultipartBody.DIGEST;
                }else if (mediaType == Multipart.MediaType.MIXED){
                    this.mediaType = MultipartBody.MIXED;
                }else if (mediaType == Multipart.MediaType.PARALLEL){
                    this.mediaType = MultipartBody.PARALLEL;
                }
            }else if (methodAnno instanceof FormUrlEncoded){
                if (isJson || isCustomRequestBody || isMultiPart){
                    throw new IllegalArgumentException("Only one encoding annotation is allowed."+method.getName());
                }
                isFormUrlEncoded = true;
            }else if (methodAnno instanceof CustomRequestBody){
                if (isJson || isMultiPart || isFormUrlEncoded){
                    throw new IllegalArgumentException("Only one encoding annotation is allowed."+method.getName());
                }
                isCustomRequestBody = true;
            }else if (methodAnno instanceof JSONRequest){
                if (isMultiPart || isCustomRequestBody || isFormUrlEncoded){
                    throw new IllegalArgumentException("Only one encoding annotation is allowed."+method.getName());
                }
                isJson = true;
            }
        }

        private okhttp3.Headers.Builder parserHeaders(String[] headers) {
            okhttp3.Headers.Builder headersBuilder = new okhttp3.Headers.Builder();
            for (String header : headers){
                int index = header.indexOf(":");
                if (index == -1 || index == 0 || index == header.length() - 1){
                    throw new IllegalArgumentException("@Headers value must be in the form \"Name: Value\".");
                }
                String name = header.substring(0,index);
                String value = header.substring(index + 1);
                headersBuilder.add(name,value);
            }
            return headersBuilder;
        }

        private void parserHttpMethodAndPath(String method, String value, boolean isBody) {
            if (this.httpMethod != null){
                throw new IllegalArgumentException("Only one HTTP method is allowed. Found: "+this.httpMethod+" and "+method);
            }
            httpMethod = method;
            hasBody = isBody;
            int question = value.indexOf('?');
            if (question != -1 && question < value.length() - 1) {
                // Ensure the query string does not have any named parameters.
                String queryParams = value.substring(question + 1);
                Matcher queryParamMatcher = PARAM_URL_REGEX.matcher(queryParams);
                if (queryParamMatcher.find()) {
                    throw new IllegalArgumentException(
                            "URL query string"+this.method.getName()+" must not have replace block. "
                                    + "For dynamic query parameters use @Query.");
                }
            }
            this.relativeUrl = value;
        }
    }
}
