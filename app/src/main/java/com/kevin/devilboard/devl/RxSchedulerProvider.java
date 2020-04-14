package com.kevin.devilboard.devl;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class RxSchedulerProvider {

    private static ObservableTransformer mObservableTransformer;

    public static Scheduler ui(){
        return AndroidSchedulers.mainThread();
    }

    public static Scheduler computation(){
        return Schedulers.computation();
    }

    public static Scheduler io(){
        return Schedulers.io();
    }

    public static <T> ObservableTransformer<T, T> applyObservableSchedulers()
    {
        if(mObservableTransformer == null)
        {
            mObservableTransformer = observable -> observable.subscribeOn(io()).
                    observeOn(ui());
        }
        return (ObservableTransformer<T, T>) mObservableTransformer;
    }

}
