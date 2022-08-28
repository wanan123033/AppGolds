package com.appgodx.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.appgodx.glide.target.DefaultConnectivity;
import com.appgodx.glide.target.ImageViewTarget;
import com.appgodx.glide.target.TargetTracker;

import java.io.File;

public class RequestManager implements LifecycleListener {
    private Glide glide;
    private Lifecycle lifecycle;
    private Context applicationContext;

    private ImageViewTarget imageViewTarget = new ImageViewTarget();
    private TargetTracker targetTracker = new TargetTracker();
    private DefaultConnectivity defaultConnectivity;

    public RequestManager(Glide glide, Lifecycle lifecycle, Context applicationContext) {

        this.glide = glide;
        this.lifecycle = lifecycle;
        this.applicationContext = applicationContext;
        defaultConnectivity = new DefaultConnectivity(applicationContext);
        this.lifecycle.addListener(defaultConnectivity);
    }

    @Override
    public void onStart() {
        imageViewTarget.onStart();
        targetTracker.onStart();
    }

    @Override
    public void onStop() {
        imageViewTarget.onStop();
        targetTracker.onStop();
    }

    @Override
    public void onDestory() {
        imageViewTarget.onDestory();
        targetTracker.onDestory();
        lifecycle.removeListener(defaultConnectivity);
    }
    public RequestBuilder<Drawable> load(String uri){
        return asDrawable().load(uri);
    }
    public RequestBuilder<Drawable> load(File file){
        return asDrawable().load(file);
    }
    public RequestBuilder<Drawable> load(int resId){
        return asDrawable().load(resId);
    }
    public RequestBuilder<Bitmap> load(Bitmap bitmap){
        return asBitmap().load(bitmap);
    }

    private RequestBuilder<Bitmap> asBitmap() {
        return as(Bitmap.class);
    }

    private RequestBuilder<Drawable> asDrawable() {
        return as(Drawable.class);
    }

    private <T> RequestBuilder<T> as(Class<T> drawableClass) {
        return new RequestBuilder<>(drawableClass);
    }
}
