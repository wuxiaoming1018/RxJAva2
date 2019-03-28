package com.example.rxjava.create;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * 创建型操作符
 */
public class ObservableCreate {

    public void create() {
        //create--表示只发送OnNext()方法
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(9);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                LogUtils.e("create:" + integer);
            }
        });
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> emitter.onNext(9)).subscribe(integer -> LogUtils.e("create:" + integer));

        //lambda写法
        Observable.create(a -> a.onNext(77)).subscribe(b -> LogUtils.e(b));

        //just--将传入的参数依次发送出来
        Observable.just(7, 8, 9, 48, 58).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                LogUtils.e("just:" + integer);
            }
        });

        //fromIterable--将Iterable中的对象依次发送出去
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i + "");
        }
        Observable.fromIterable(list).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                LogUtils.e("fromIterable :" + s);
            }
        });

        //lambda写法
        Observable.fromIterable(list).subscribe(s -> LogUtils.e("fromIterable :" + s));

        //timer--类似于Handler中的postDelay
        final long l = System.currentTimeMillis();
        Observable.timer(1000, TimeUnit.MILLISECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                long l1 = System.currentTimeMillis();
                LogUtils.e("timer时间差:" + (l1 - l) + "ms");
            }
        });

        //lambda写法
        Observable.timer(1000, TimeUnit.MILLISECONDS).subscribe(aLong -> {
            long l1 = System.currentTimeMillis();
            LogUtils.e("timer时间差:" + (l1 - l) + "ms");
        });


        //interval--创建一个固定时间间隔发射请求  可以当定时器，间歇性网络请求
        Observable.interval(1000, TimeUnit.MILLISECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                LogUtils.e("interval:" + aLong);
            }
        });

        //lambda写法
        Observable.interval(1000, TimeUnit.MILLISECONDS).subscribe(mop -> LogUtils.e("interval:" + mop));

        //repeat--创建一个重复发送特定数据  可以当做计数器或者间歇性网络请求
        Observable.just(9).repeat(5).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                LogUtils.e("repeat:" + integer);
            }
        });
        //lambda写法
        Observable.just(80).repeat().subscribe(ko -> LogUtils.e("repeat:" + ko));
    }
}
