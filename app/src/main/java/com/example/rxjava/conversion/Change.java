package com.example.rxjava.conversion;


import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 转换操作符
 */
public class Change {
    /**
     * map相关
     */
    public static void map() {

        List<String> list = new ArrayList<>();
        //map  对Observable发射的每一项数据都用一个函数来进行转换
        Observable.just("85", "14", "89", "88").map(new Function<String, List<String>>() {
            @Override
            public List<String> apply(String s) throws Exception {
                list.add(s);
                return list;
            }
        }).subscribe(new Consumer<List<String>>() {
            @Override
            public void accept(List<String> integer) throws Exception {
                if (integer.size() == list.size()) {
                    LogUtils.e("map相关:" + integer.toString());
                }
            }
        });

        //lambda表达式
        Observable.just("77", "96", "32").map(a -> Integer.parseInt(a) * 5).subscribe(c -> LogUtils.e("map--just1相关:" + c));

        Observable.just("12", "89", "22", "89").map(a -> {
            list.add(a);
            return list;
        })
                .subscribe(x -> {
                    if (x.size() == list.size()) LogUtils.e("map--just2相关:" + x.toString());
                });

        Observable.create(a -> a.onNext(89)).map(b -> String.valueOf(b)).subscribe(x -> LogUtils.e("map--create相关:" + x));
    }

    public static void flatMap() {
        Observable.just(1, 5, 9, 4, 6).flatMap(new Function<Integer, ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(Integer integer) throws Exception {
                List<Integer> list = new ArrayList<>();
                for (int i = 0; i < 2; i++) {
                    list.add(i);
                }
                return Observable.fromIterable(list);
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                LogUtils.e("flatMap相关:" + integer);
            }
        });

        //lambda写法
        Observable.just(1, 2, 3, 4).flatMap(a -> {
            List list = new ArrayList();
            for (int i = 0; i < 2; i++) {
                list.add(i);
            }
            return Observable.fromIterable(list);
        }).subscribe(x -> LogUtils.e("flatMap相关2--just: " + x));

        Observable.create(a -> a.onNext("8956")).flatMap(x -> {
            List list = new ArrayList();
            for (int i = 0; i < 2; i++) {
                list.add(i);
            }
            LogUtils.e("发布线程:" + Thread.currentThread().getName());
            return Observable.fromIterable(list);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> LogUtils.e("flapMap相关3--create" + x));
    }
}
