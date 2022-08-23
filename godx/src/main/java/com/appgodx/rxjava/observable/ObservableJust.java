package com.appgodx.rxjava.observable;

import com.appgodx.rxjava.DisponseHelper;
import com.appgodx.rxjava.Dispose;
import com.appgodx.rxjava.DisposeImpl;
import com.appgodx.rxjava.Observable;
import com.appgodx.rxjava.Observer;

public class ObservableJust<T> extends Observable<T> {
    private T t;

    public ObservableJust(T t) {
        this.t = t;
    }

    @Override
    public void subscribeActual(Observer<? super T> observer) {
        JustObserver<T> just = new JustObserver<>(t,observer);
        just.run();
    }
    private static final class JustObserver<T> implements Observer<T>, Dispose,Runnable{

        private T t;
        private Observer<? super T> observer;
        private DisposeImpl dispose;

        public JustObserver(T t, Observer<? super T> observer) {
            this.t = t;
            this.observer = observer;
            dispose = new DisposeImpl();
        }

        @Override
        public void dispose() {
            DisponseHelper.dispose(dispose);
        }

        @Override
        public boolean isDispose() {
            return DisponseHelper.isDispose(dispose);
        }

        @Override
        public void onSubscribe(Dispose dispose) {
            observer.onSubscribe(dispose);
        }

        @Override
        public void onNext(T t) throws Exception {
            observer.onNext(t);
        }

        @Override
        public void onError(Throwable t) {
            observer.onError(t);
        }

        @Override
        public void onComplete() {
            observer.onComplete();
        }

        @Override
        public void run() {
            onSubscribe(dispose);
            if (isDispose()){
                try {
                    onNext(t);
                } catch (Exception e) {
                    e.printStackTrace();
                    onError(e);
                }
            }
            onComplete();
        }
    }
}
