package com.appgodx.rxjava.observable;

import com.appgodx.rxjava.DisponseHelper;
import com.appgodx.rxjava.Dispose;
import com.appgodx.rxjava.DisposeImpl;
import com.appgodx.rxjava.Function;
import com.appgodx.rxjava.Observable;
import com.appgodx.rxjava.Observer;

public class ObservableMap<T,R> extends Observable<R> {
    private Observable<T> observable;
    private Function<T, R> function;

    public ObservableMap(Observable<T> observable, Function<T, R> function) {
        this.observable = observable;
        this.function = function;
    }

    @Override
    public void subscribeActual(Observer<? super R> observer) {
        MapObserver<T,R> mapObserver = new MapObserver<>(function,observer);
        observable.subscribeActual(mapObserver);
    }
    private static final class MapObserver<T,R> implements Observer<T>, Dispose{

        private final Function<T, R> function;
        private final Observer<? super R> observer;
        private Dispose dispose;

        public MapObserver(Function<T, R> function, Observer<? super R> observer) {
            this.function = function;
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
        public void onNext(T r) throws Exception {
            observer.onNext(function.apply(r));
        }

        @Override
        public void onError(Throwable t) {
            observer.onError(t);
        }

        @Override
        public void onComplete() {
            observer.onComplete();
        }
    }
}
