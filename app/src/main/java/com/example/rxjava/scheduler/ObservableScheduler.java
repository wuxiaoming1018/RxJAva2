package com.example.rxjava.scheduler;


import com.blankj.utilcode.util.LogUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 线程切换相关
 */
public class ObservableScheduler {

    public void scheduler() {
        //Scheduler.newThread() 开启一个新线程
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            emitter.onNext(856);
            LogUtils.e("发布线程:" + Thread.currentThread().getName());
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(integer -> LogUtils.e("订阅线程:" + Thread.currentThread().getName()));

        //lambda写法
        Observable.create(a -> {
            a.onNext(025);
            LogUtils.e("发布线程:" + Thread.currentThread().getName());
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(b -> LogUtils.e("订阅线程:" + Thread.currentThread().getName()));

        //Scheduler.io()
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(859);
                LogUtils.e("发送线程:" + Thread.currentThread().getName());
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtils.e("订阅线程:" + Thread.currentThread().getName());
                    }
                });

        //lambda写法
        Observable.create(a -> {
            a.onNext(5626);
            LogUtils.e("发布线程:" + Thread.currentThread().getName());
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(b -> LogUtils.e("订阅线程:" + Thread.currentThread().getName()));


        //AndroidSchedulers.mainThread()
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(2215);
                LogUtils.e("发布线程:" + Thread.currentThread().getName());
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtils.e("订阅线程:" + Thread.currentThread().getName());
                    }
                });

        //lambda写法
        Observable.create(a -> {
            a.onNext("85962");
            LogUtils.e("发布线程:" + Thread.currentThread().getName());
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(b -> LogUtils.e("订阅线程:" + Thread.currentThread().getName()));

        Observable.just("4","9").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(b -> LogUtils.e("订阅线程:"+Thread.currentThread().getName()));
    }

}
