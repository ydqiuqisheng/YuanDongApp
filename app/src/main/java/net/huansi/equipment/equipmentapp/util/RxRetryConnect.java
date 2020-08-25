package net.huansi.equipment.equipmentapp.util;

import android.widget.Toast;

import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

public class RxRetryConnect implements Func1<Observable<? extends Throwable>, Observable<?>> {
    private int retryCount;
    private int startCount=1;
    public RxRetryConnect(int retryCount) {
        this.retryCount = retryCount;
    }

    @Override
    public Observable<?> call(final Observable<? extends Throwable> observable) {
        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
            @Override
            public Observable<?> call(Throwable throwable) {
                return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                    @Override
                    public Observable<?> call(Throwable throwable) {
                        if (startCount++>=retryCount) {
                            return Observable.error(throwable);

                        }else {
                            return Observable.just(throwable).zipWith(Observable.range(startCount, retryCount), new Func2<Throwable, Integer, Integer>() {
                                @Override
                                public Integer call(Throwable throwable, Integer i) {

                                    return i;
                                }
                            }).flatMap(new Func1<Integer, Observable<? extends Long>>() {
                                @Override
                                public Observable<? extends Long> call(Integer retryCount) {

                                    return Observable.timer(2000, TimeUnit.MILLISECONDS);
                                }
                            });
                        }

                    }
                });
            }
        });
    }
}
