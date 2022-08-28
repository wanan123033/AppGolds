package com.appgodx.glide;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.appgodx.AppHandler;

import java.util.HashMap;
import java.util.Map;

public class RequestManagerRetriever implements Handler.Callback {
    private static final int ID_REMOVE_FRAGMENT_MANAGER = 1;
    private RequestManager applicationManager;
    private Map<FragmentManager,GlideFragment> fragmentCache = new HashMap<>();
    private Handler handler;
    public RequestManagerRetriever(){
        handler = new Handler(Looper.getMainLooper(),this);
    }

    public RequestManager get(Context context) {
        if (context == null){
            throw new NullPointerException("You cannot start a load on a null Context");
        }else if (context instanceof FragmentActivity){
            return get((FragmentActivity)context);
        }else if (context instanceof ContextWrapper && ((ContextWrapper) context).getBaseContext().getApplicationContext() != null){
            return get(((ContextWrapper) context).getBaseContext());
        }
        return getApplicationManager(context);
    }

    private RequestManager getApplicationManager(Context context) {
        if (applicationManager == null){
            synchronized (this){
                if (applicationManager == null){
                    Glide glide = Glide.get(context.getApplicationContext());
                    applicationManager = new RequestManager(glide,new ApplicationLifecycle(),context.getApplicationContext());
                }
            }
        }
        return null;
    }

    public RequestManager get(FragmentActivity fragmentActivity) {
        if (Util.isOnMainThread()){
            FragmentManager fm = fragmentActivity.getSupportFragmentManager();
            Glide glide = Glide.get(fragmentActivity.getApplicationContext());
            return fragmentGet(fm,glide,fragmentActivity);
        }else {
            return getApplicationManager(fragmentActivity.getApplicationContext());
        }

    }

    public RequestManager get(Fragment fragment) {
        if (Util.isOnMainThread()){
            FragmentManager fm = fragment.getChildFragmentManager();
            Glide glide = Glide.get(fragment.getActivity());
            return fragmentGet(fm,glide,fragment.getContext().getApplicationContext());
        }else {
            return getApplicationManager(fragment.getContext().getApplicationContext());
        }
    }

    private RequestManager fragmentGet(FragmentManager fm,Glide glide,Context context) {
        GlideFragment glideFragment = (GlideFragment) fm.findFragmentByTag(GlideFragment.TAG);
        if (glideFragment == null){
            glideFragment = fragmentCache.get(fm);
        }
        if (glideFragment == null){
            glideFragment = new GlideFragment(glide,context);
            fragmentCache.put(fm,glideFragment);
            fm.beginTransaction().add(glideFragment,GlideFragment.TAG).commitAllowingStateLoss();
            handler.obtainMessage(ID_REMOVE_FRAGMENT_MANAGER,fm).sendToTarget();
        }

        return glideFragment.getRequestManager();
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        if (msg.what == ID_REMOVE_FRAGMENT_MANAGER){
            FragmentManager fm = (FragmentManager) msg.obj;
            fragmentCache.remove(fm);
        }
        return false;
    }
}
