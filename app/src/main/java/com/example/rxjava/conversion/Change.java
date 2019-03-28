package com.example.rxjava.conversion;


import android.util.Log;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;
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

    //buffer(long count, long skip) 把发送过来的数据收集成一个集合, 然后发送一个集合给下游
    //count:表示一个集合中可以收集几个数据 skip:表示每隔第几个Observable 发送过来的 开始收集
    public static void buffer() {
        Observable.just(8, 9, 15, 48, 79, 56, 156).buffer(4, 2).subscribe(new Consumer<List<Integer>>() {
            @Override
            public void accept(List<Integer> integers) throws Exception {
                LogUtils.e("buffer相关:" + integers.toString());
            }
        });

        //lambda表达式
        Observable.just(10, 15, 26, 37, 48, 59, 72).buffer(5, 3).subscribe(x -> LogUtils.e("buffer---lambda相关:" + x.toString()));

    }

    //window(long count, long skip) 类似于buffer缓存一个list集合，区别在于window将这个结果集合封装成了observable
    public static void window() {
        Observable.just("cook", "link", "like", "null", "super", "string").window(2, 3).subscribe(new Consumer<Observable<String>>() {
            @Override
            public void accept(Observable<String> stringObservable) throws Exception {
                LogUtils.e("window相关: " + stringObservable.toString());
                stringObservable.subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        LogUtils.e("window相关---：" + s);
                    }
                });
            }
        });

        //lambda写法
        Observable.just("cooks", "links", "likes", "nulls", "supers", "strings").window(3, 2).subscribe(x -> {
            LogUtils.e("window--lambda相关: " + x.toString());
            x.subscribe(y -> LogUtils.e("window---lambda相关: " + y));
        });
    }

    //groupBy 将Observable分拆为Observable集合，将原始Observable发射的数据按Key分组，每一个Observable发射一组不同的数据
    public static void groupBy() {
        Observable.just(1, 2, 3, 4, 5).groupBy(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return String.valueOf(integer);
            }
        }).subscribe(new Consumer<GroupedObservable<String, Integer>>() {
            @Override
            public void accept(GroupedObservable<String, Integer> stringIntegerGroupedObservable) throws Exception {
                stringIntegerGroupedObservable.subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtils.e("groupBy相关: " + stringIntegerGroupedObservable.getKey() + "-----" + integer);
                    }
                });
            }
        });

        //lambda相关
        Observable.just(9, 10, 11, 12, 13, 14, 15).groupBy(x -> String.valueOf(x))
                .subscribe(y -> y.subscribe(z -> LogUtils.e("groupBy---lambda相关:" + y.getKey() + "-------------" + z)));
    }
}
