package com.appgodx.rxjava;

public class RxJavaPlugin {
   public static <T> Observable<T> onApply(Observable<T> observable){
       return observable;
   }
}
