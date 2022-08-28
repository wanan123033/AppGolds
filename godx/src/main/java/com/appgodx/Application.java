package com.appgodx;

import android.net.Uri;
import android.widget.ImageView;

import com.appgodx.appdelegate.AppDelegate;
import com.appgodx.base.BaseApplication;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;

public final class Application extends BaseApplication {
    @Override
    protected AppDelegate createAppDelegate() {

        RequestManager requestManager = Glide.with(this);
        RequestBuilder builder = requestManager.load(Uri.EMPTY);
        builder.into(new ImageView(this));
        return new MergaAppDelegate();
    }
}
