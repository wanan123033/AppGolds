package com.appgodx.glide;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class Glide {
    private static Glide glide;
    private RequestManagerRetriever requestManagerRetriever;
    public Glide(Context context) {
        requestManagerRetriever = new RequestManagerRetriever();
    }

    public static RequestManager with(Context context){
        return getRetriever(context).get(context);
    }
    public static RequestManager with(FragmentActivity fragmentActivity){
        return getRetriever(fragmentActivity).get(fragmentActivity);
    }
    public static RequestManager with(Fragment fragment){
        return getRetriever(fragment.getActivity()).get(fragment);
    }
    private static RequestManagerRetriever getRetriever(@Nullable Context context) {
        return Glide.get(context).getRequestManagerRetriever();
    }

    private RequestManagerRetriever getRequestManagerRetriever() {
        return requestManagerRetriever;
    }

    public static Glide get(Context context) {
        if (glide == null){
            synchronized (Glide.class){
                if (glide == null){
                    glide = new Glide(context);
                }
            }
        }
        return glide;
    }
}
