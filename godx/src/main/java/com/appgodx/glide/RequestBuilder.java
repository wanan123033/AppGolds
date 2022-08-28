package com.appgodx.glide;

import android.widget.ImageView;

import com.appgodx.glide.target.ImageViewTarget;
import com.bumptech.glide.util.Executors;

import java.io.File;
import java.util.concurrent.Executor;

public class RequestBuilder<RespType> {

    private Class<RespType> resultClass;
    private Object model;

    public RequestBuilder(Class<RespType> resultClass){
        this.resultClass = resultClass;
    }

    public RequestBuilder<RespType> load(String uri) {
        model = uri;
        return this;
    }

    public RequestBuilder<RespType> load(File file) {
        model = file;
        return this;
    }

    public RequestBuilder<RespType> load(int resId) {
        model = resId;
        return this;
    }

    public RequestBuilder<RespType> load(RespType bitmap) {
        model = bitmap;
        return this;
    }

    public void into(ImageView imageView){
        if (!Util.isOnMainThread()){
            throw new IllegalArgumentException("into 不在主线程执行，无法显示");
        }
        into(new ImageViewTarget(imageView),imageView.getScaleType(), Executors.mainThreadExecutor());
    }

    private void into(ImageViewTarget target, ImageView.ScaleType scaleType, Executor mainThreadExecutor) {

    }
}
