package com.appgodx.util;

import android.text.TextUtils;

import java.util.Locale;

public class StringUtil {
    public static String uppercaseFirst(String str){
        if (TextUtils.isEmpty(str)){
            return null;
        }

        return str.substring(0,1).toUpperCase()+str.substring(1);
    }
}
