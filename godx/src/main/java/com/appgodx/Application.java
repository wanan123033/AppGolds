package com.appgodx;

import com.appgodx.appdelegate.AppDelegate;
import com.appgodx.base.BaseApplication;

public final class Application extends BaseApplication {
    @Override
    protected AppDelegate createAppDelegate() {
        return new MergaAppDelegate();
    }
}
