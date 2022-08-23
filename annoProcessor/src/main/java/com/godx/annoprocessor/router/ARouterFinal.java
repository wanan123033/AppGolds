package com.godx.annoprocessor.router;

import com.squareup.javapoet.ClassName;

public class ARouterFinal {
    public static final String GET_GROUP_ROUTER_METHOD_NAME = "getGroupRouter";
    public static final String GROUPMAP = "groupMap";
    public static final String MOUDLENAME = "moudleName";
    public static final String GET_PATH_ROUTER_METHOD_NAME = "getPathRouter";
    public static final String PATHMAP = "pathMap";
    public static final String PATHROUTER = "com.appgodx.router.PathRouter";
    public static final String GOROUPROUTER = "com.appgodx.router.GroupRouter";
    public static final String ACTIVITY = "android.app.Activity";
    public static final String FRAGMENT = "androidx.fragment.app.Fragment";
    public static final ClassName ROUTERBEAN = ClassName.get("com.appgodx.router","RouterBean");

    public static final String GETPARAMETER_METHOD_NAME = "getParameter";
    public static final String ROUTERPARAMETER ="com.appgodx.router.RouterParameter";
    public static final String PACKAGENAME = "packageName";
    public static final CharSequence CALLTYPENAME = "com.appgodx.router.Call";
}
