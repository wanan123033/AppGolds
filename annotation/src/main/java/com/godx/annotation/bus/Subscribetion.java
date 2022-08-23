package com.godx.annotation.bus;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribetion {
    String name();
    ThreadMode mode() default ThreadMode.MAIN;
    enum ThreadMode{
        CURRENT,MAIN,CHILD
    }
}
