package com.example.rxjava.help;

import com.blankj.utilcode.util.LogUtils;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class ObservableHelper {

    public static void doNext() {
        Observable.just(5, 8, 6, 3)
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtils.e("---doOnNext接受之前:" + integer);
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                LogUtils.e("---doOnNext接收到的数据:" + integer);
            }
        });

        //lambda写法
        Observable.just("A", "m", "P", "y")
                .doOnNext(x -> LogUtils.e("--doOnNext--lambda--接受之前:" + x))
                .subscribe(y -> LogUtils.e("--doOnNext---lambda--接收到的数据:" + y));
    }
}
