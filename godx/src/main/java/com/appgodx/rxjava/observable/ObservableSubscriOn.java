package com.appgodx.rxjava.observable;

import com.appgodx.rxjava.Dispose;
import com.appgodx.rxjava.Observable;
import com.appgodx.rxjava.Observer;
import com.appgodx.rxjava.Scheduler;

public class ObservableSubscriOn<T> extends Observable<T>{
    private final Observable<T> observable;
    private final Scheduler scheduler;

    public ObservableSubscriOn(Observable<T> observable, Scheduler scheduler) {
        this.observable = observable;
        this.scheduler = scheduler;
    }

    @Override
    public void subscribeActual(Observer<? super T> observer) {
        SubscriOnObserver<T> observer1 = new SubscriOnObserver<T>(observable,scheduler,observer);
        observable.subscribeActual(observer1);
    }
    private static final class SubscriOnObserver<T> implements Observer<T>, Runnable{

        private Observable<T> observable;
        private Scheduler scheduler;
        private Observer<? super T> observer;

        public SubscriOnObserver(Observable<T> observable,Scheduler scheduler, Observer<? super T> observer) {
            this.observable = observable;
            this.scheduler = scheduler;
            this.observer = observer;
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
            Scheduler.Worker worker = scheduler.createWorker();
            worker.schedule(new Runnable() {
                @Override
                public void run() {
                    observable.subscribeActual(observer);
                }
            });
        }
    }
}
