package com.appgodx.glide;

import android.content.Context;

import androidx.fragment.app.Fragment;

public class GlideFragment extends Fragment {
    public static final String TAG = "Glide_Fragment";
    private RequestManager requestManager;
    private ActivityFragmentLifecycle lifecycle;

    public GlideFragment(Glide glide, Context context) {
        lifecycle = new ActivityFragmentLifecycle();
        requestManager = new RequestManager(glide,lifecycle,context);

        lifecycle.addListener(requestManager);
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycle.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        lifecycle.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecycle.onDestory();
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }
}
