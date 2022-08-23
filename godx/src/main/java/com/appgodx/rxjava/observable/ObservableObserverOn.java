package com.appgodx.rxjava.observable;

import com.appgodx.rxjava.Dispose;
import com.appgodx.rxjava.Observable;
import com.appgodx.rxjava.Observer;
import com.appgodx.rxjava.Scheduler;

public class ObservableObserverOn<T> extends Observable<T>{
    private final Observable<T> observable;
    private final Scheduler scheduler;

    public ObservableObserverOn(Observable<T> observable, Scheduler scheduler) {
        this.observable = observable;
        this.scheduler = scheduler;
    }

    @Override
    public void subscribeActual(Observer<? super T> observer) {
        Scheduler.Worker worker = scheduler.createWorker();
        ObserverOnObserver<T> onObserver = new ObserverOnObserver<T>(observable,observer);
        worker.schedule(onObserver);

    }
    private static final class ObserverOnObserver<T> implements Observer<T>,Runnable{

        private final Observable<T> observable;
        private final Observer<? super T> observer;

        public ObserverOnObserver(Observable<T> observable, Observer<? super T> observer) {
            this.observable = observable;
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
            observable.subscribeActual(this);
        }
    }
}
