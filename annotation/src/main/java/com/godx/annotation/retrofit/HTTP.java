package com.godx.annotation.retrofit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HTTP {
    String url();
    way method() default way.POST;
    enum way{
        GET,POST,PUT,DELETE,OPTIONS,HEAD,PATCH
    }
}
