package com.appgodx.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.appgodx.AppHandler;
import com.appgodx.LifecycleHandler;
import com.appgodx.router.RouterParameterManager;

public abstract class BaseFragment<VB extends ViewBinding> extends Fragment implements Handler.Callback {
    protected VB viewBind;
    protected AppHandler appHandler;
    protected LifecycleHandler lifecycleHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RouterParameterManager.getInstance().bindParameter(this);
        appHandler = AppHandler.getAppHandler();
        lifecycleHandler = new LifecycleHandler(Looper.getMainLooper(),this,this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBind = (VB) DataBindingUtil.inflate(inflater,getLayoutId(),container,false);
        return viewBind.getRoot();
    }

    protected abstract int getLayoutId();

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        return false;
    }
}
