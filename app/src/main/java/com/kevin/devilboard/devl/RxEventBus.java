package com.kevin.devilboard.devl;


import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class RxEventBus {

    private static RxEventBus mInstance;

    private RxEventBus(){}

    public static RxEventBus getInstance(){
        if(mInstance==null){
            mInstance = new RxEventBus();
        }
        return mInstance;
    }

    private PublishSubject<RxEvent> bus = PublishSubject.create();

    public void publish(RxEvent event){
        bus.onNext(event);
    }

    public Observable<RxEvent> listenTo(Class<RxEvent> clazz){
        return bus.ofType(clazz);
    }

    public boolean hasObservers(){
        return bus.hasObservers();
    }
}
