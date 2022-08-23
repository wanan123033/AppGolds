package com.appgodx.rxmvvm;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import com.appgodx.base.BaseActivity;

public abstract class BaseMvvmActivity <VB extends ViewBinding,VM extends BaseViewModel> extends BaseActivity<VB> {
    protected VM viewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        viewModel = (VM) viewModelProvider.get(getViewModelClass());
        getLifecycle().addObserver(viewModel);

    }

    protected abstract Class<? extends BaseViewModel> getViewModelClass();
}
