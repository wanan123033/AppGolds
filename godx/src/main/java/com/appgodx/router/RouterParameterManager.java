package com.appgodx.router;

import java.util.HashMap;
import java.util.Map;

public class RouterParameterManager {
    private static RouterParameterManager rpm;
    private Map<Object,RouterParameter> parameterMap;
    private static final String CLASSNAMEPIX = "$RouterParameter";
    private RouterParameterManager(){
        parameterMap = new HashMap<>();
    }
    public static RouterParameterManager getInstance(){
        if (rpm == null){
            synchronized (RouterParameterManager.class){
                if (rpm == null){
                    rpm = new RouterParameterManager();
                }
            }
        }
        return rpm;
    }

    public void bindParameter(Object obj) {
        RouterParameter routerParameter = parameterMap.get(obj);
        if (routerParameter != null){
            routerParameter.getParameter(obj);
            return;
        }
        try {
            Class routerParameterClass = Class.forName(obj.getClass().getName()+CLASSNAMEPIX);
            routerParameter = (RouterParameter) routerParameterClass.newInstance();
            if (routerParameter != null){
                parameterMap.put(obj,routerParameter);
                routerParameter.getParameter(obj);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
