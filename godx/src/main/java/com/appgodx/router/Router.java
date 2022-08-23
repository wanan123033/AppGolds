package com.appgodx.router;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.HashMap;
import java.util.Map;

public class Router {
    private Context context;
    private Bundle bundle;
    private String toUrl;
    private static Router aRouter;
    private static final String PACKAGENAME = "com.router.";
    private static Map<String,GroupRouter> groupRouterMap = new HashMap<>();
    private static Map<String,PathRouter> pathRouterMap = new HashMap<>();
    private FragmentManager fm;
    private int resId;

    private Router(Context context){
        bundle = new Bundle();
        this.context = context;
    }
    public static Router with(Context context){
        return new Router(context);
    }

    public Router from(String key, String value){
        bundle.putString(key, value);
        return this;
    }
    public Router from(String key, int value){
        bundle.putInt(key,value);
        return this;
    }
    public Router from(String key, byte value){
        bundle.putByte(key,value);
        return this;
    }
    public Router from(String key, short value){
        bundle.putShort(key,value);
        return this;
    }
    public Router from(String key, long value){
        bundle.putLong(key,value);
        return this;
    }
    public Router from(String key, double value){
        bundle.putDouble(key,value);
        return this;
    }
    public Router from(String key, float value){
        bundle.putFloat(key,value);
        return this;
    }
    public Router from(String key, boolean value){
        bundle.putBoolean(key,value);
        return this;
    }
    public Router fm(FragmentManager fm,int resId){
        this.fm = fm;
        this.resId = resId;
        return this;
    }

    public Router to(String toUrl){
        this.toUrl = toUrl;
        return this;
    }
    public Object router(){
        String groupName = toUrl.substring(1,toUrl.lastIndexOf("/"));
        GroupRouter groupRouter = groupRouterMap.get(groupName);
        try {
            if (groupRouter == null) {
                Class groupClass = Class.forName(PACKAGENAME + cature(groupName) + "$GroupRouter");
                groupRouter = (GroupRouter) groupClass.newInstance();
                groupRouterMap.put(groupName, groupRouter);
            }
            PathRouter pathRouter = pathRouterMap.get(groupName);
            if (pathRouter == null) {
                Map<String, Class<? extends PathRouter>> router = groupRouter.getGroupRouter();
                Class<? extends PathRouter> pathClass = router.get(groupName);
                pathRouter = pathClass.newInstance();
                pathRouterMap.put(groupName,pathRouter);
            }
            RouterBean routerBean = pathRouter.getPathRouter().get(toUrl);
            if (routerBean != null) {
                if (routerBean.getType() == RouterBean.TypeEnum.ACTIVITY) {
                    Class<?> clazz = routerBean.getClazz();
                    if (clazz != null) {
                        Intent intent = new Intent();
                        intent.setClass(context, clazz);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                    return null;
                } else if (routerBean.getType() == RouterBean.TypeEnum.CALL) {
                    Class<?> clazz = routerBean.getClazz();
                    return clazz.newInstance();
                } else if (routerBean.getType() == RouterBean.TypeEnum.FRAGMENT) {
                    Class<?> clazz = routerBean.getClazz();
                    Fragment fragment = (Fragment) clazz.newInstance();
                    fm.beginTransaction().replace(resId, fragment).commitAllowingStateLoss();
                    return null;
                }
            }else {
                throw new RuntimeException("未找到该路由："+toUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private static String cature(String str){
        String cap = str.substring(0, 1).toUpperCase() + str.substring(1);
        return cap;
    }
}
