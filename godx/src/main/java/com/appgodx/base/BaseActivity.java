package com.appgodx.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewbinding.ViewBinding;

import com.appgodx.AppHandler;
import com.appgodx.LifecycleHandler;
import com.appgodx.router.RouterParameterManager;

public abstract class BaseActivity<VB extends ViewBinding> extends AppCompatActivity implements Handler.Callback {
    private VB viewBind;
    protected AppHandler appHandler;
    private LifecycleHandler lifecycleHandler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBind = (VB) DataBindingUtil.setContentView(this,getLayoutId());
        RouterParameterManager.getInstance().bindParameter(this);
        appHandler = AppHandler.getAppHandler();
        lifecycleHandler = new LifecycleHandler(Looper.getMainLooper(),this,this);
    }

    protected abstract int getLayoutId();

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        return false;
    }
}
