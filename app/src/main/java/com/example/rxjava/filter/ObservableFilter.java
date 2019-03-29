package com.example.rxjava.filter;

import com.blankj.utilcode.util.LogUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class ObservableFilter {

    public static void filter() {
        //filter()一般返回boolean值，为true会走到accept()方法
        Observable.just(8, 9, 45, 89, 26, 54, 32, 35, 12, 79).filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer % 2 == 0;
            }
        })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtils.e("filter相关---偶数：" + integer);
                    }
                });

        //lambda写法
        Observable.just(4, 89, 52, 62, 48, 19, 265, 92, 6965, 29).filter(x -> x % 2 == 1)
                .subscribe(y -> LogUtils.e("filter---lambda相关,奇数：" + y));
    }

    //distince 去除重复的Observable
    public static void distince() {
        Observable.just(1, 2, 2, 5, 8, 6, 5).distinct(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) throws Exception {
                return integer;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                LogUtils.e("distince--1相关: " + integer);
            }
        });

        Observable.just(4, 5, 2, 4, 6, 2, 3).distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtils.e("distince--2相关: " + integer);
                    }
                });

        //lambda表达式
        Observable.just(2, 2, 6, 8, 4, 5, 3, 2, 4).distinct()
                .subscribe(x -> LogUtils.e("distince--1--lambda相关： " + x));
        Observable.just(4, 89, 54, 54, 21, 89, 3, 89).distinct(x -> x)
                .subscribe(y -> LogUtils.e("distince--2--lambda相关: " + y));
        Observable.just(4, 7, 4, 8, 9, 54, 54).distinct().subscribe(x -> LogUtils.e("distince---3--lambda相关: " + x));
    }

    public static void debounce() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                Thread.sleep(2000);
                emitter.onNext(3);
                emitter.onNext(4);
                emitter.onNext(5);
                emitter.onNext(6);
                emitter.onNext(7);
                emitter.onNext(8);
                emitter.onNext(9);
                emitter.onComplete();
            }
        }).debounce(10000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtils.e("debounce--相关:" + integer);
                    }
                });

        Observable.create(x -> {
            x.onNext(1);
            x.onNext(2);
            x.onNext(3);
            x.onNext(4);
            Thread.sleep(2000);
            x.onNext(5);
            x.onNext(6);
            x.onNext(7);
        }).debounce(1000, TimeUnit.MILLISECONDS)
                .subscribe(y -> LogUtils.e("debounce--lambda相关:" + y));
    }
}
