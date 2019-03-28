package com.example.rxjava.filter;

import com.blankj.utilcode.util.LogUtils;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

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
}
