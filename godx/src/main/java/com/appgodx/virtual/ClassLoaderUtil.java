package com.appgodx.virtual;

import android.content.Context;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;

import dalvik.system.DexClassLoader;

public class ClassLoaderUtil {
    /**
     * 加载dex文件到Context的ClassLoader中，
     * @param context
     * @param dexPath dex文件的绝对路径,也可以是APK
     * @param isFirst 是否将加载的dex放在最前面
     *
     *  dx --dex --output=test.dex com/example/plugin/Test.class
     */
    public static void loadDex(Context context,String dexPath,boolean isFirst){
        try {
            Class clazz = Class.forName("dalvik.system.BaseDexClassLoader");
            Field pathListField = clazz.getDeclaredField("pathList");
            pathListField.setAccessible(true);

            Class dexPathListClass = Class.forName("dalvik.system.DexPathList");
            Field dexElements = dexPathListClass.getDeclaredField("dexElements");
            dexElements.setAccessible(true);

            //1.获取宿主的
            ClassLoader pathClassLoader = context.getClassLoader();
            Object hostPathList = pathListField.get(pathClassLoader);
            Object[] hostDexElements = (Object[]) dexElements.get(hostPathList);

            //2.获取插件的
            DexClassLoader dexClassLoader = new DexClassLoader(dexPath,context.getCacheDir().getAbsolutePath(),null,pathClassLoader);
            Object pluginPathList = pathListField.get(dexClassLoader);
            Object[] pluginElements = (Object[]) dexElements.get(pluginPathList);

            //3.合并数组
            Object[] dexElement = (Object[]) Array.newInstance(hostDexElements.getClass().getComponentType(), hostDexElements.length + pluginElements.length);
            if (!isFirst){
                System.arraycopy(hostDexElements,0,dexElement,0,hostDexElements.length);
                System.arraycopy(pluginElements,0,dexElement,hostDexElements.length,pluginElements.length);
            }else {
                System.arraycopy(pluginElements,0,dexElement,0,pluginElements.length);
                System.arraycopy(hostDexElements,0,dexElement,pluginElements.length,hostDexElements.length);
            }
            //4.赋值
            dexElements.set(hostPathList,dexElement);
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
