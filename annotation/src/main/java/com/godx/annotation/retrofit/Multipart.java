package com.godx.annotation.retrofit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Multipart {
    MediaType type() default MediaType.FORM;
    enum MediaType{
        MIXED,ALTERNATIVE,DIGEST,PARALLEL,FORM
    }
}
