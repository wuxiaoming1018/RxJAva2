package com.example.rxjava.combination;

import com.blankj.utilcode.BuildConfig;
import com.blankj.utilcode.util.LogUtils;

import java.io.Serializable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

/**
 * 组合操作法
 */
public class ObservableCombination {

    // zip 通过一个函数将多个Observable发送的事件结合到一起，
    // 然后发送这些组合到一起的事件. 它按照严格的顺序应用这个函数。
    // 它只发射与发射数据项最少的那个Observable一样多的数据。
    //使用场景: 实现多个接口数据共同更新UI
    public static void zip() {
        Observable<Integer> just = Observable.just(1, 2, 3, 4, 5, 6, 7);
        Observable<String> just1 = Observable.just("a", "b", "c", "d", "e");
        Observable.zip(just, just1, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String integer2) throws Exception {
                return integer + integer2;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                LogUtils.e("zip相关:" + s);
            }
        });

        //lambda写法
        Observable.zip(just1, just, (x, y) -> x + y).subscribe(z -> LogUtils.e("zip----lambda相关:" + z));
    }

    //concat 是接收若干个Observables，发射数据是有序的，不会交叉 ,
    // 如果第一个发送onNext的话就不会走第二个Observable,
    // 如果第一个发送的是onComplete那么就走第二个Observable
    //使用场景一: 当请求数据的时候,如果有缓存的话先从缓存中读取,如果缓存没有的话在从网络读取
    //使用场景二: 当请求网络数据的时候,如果网络数据有错误的话,就展示缓存的数据
    public static void concat() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                if (!BuildConfig.DEBUG) {
                    //不会走第二个Observable
                    emitter.onNext(90);
                } else {
                    //会走第二个Observable
                    emitter.onComplete();
                }
            }
        });
        Observable<Integer> observable2 = Observable.just(5, 6, 4);
        Observable.concat(observable1, observable2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                LogUtils.e("concat---数据:" + integer);
            }
        });


        //lambda写法
        Observable.concat(observable2, observable1).subscribe(x -> LogUtils.e("concat--lambda数据:" + x));
    }

    //merge 将多个Observable合并为一个。不同于concat，merge不是按照添加顺序连接，
    // 而是按照时间线来连接。把多个被观察者合并到一个被观察者身上一起输出，
    // 但是可能会让合并的被观察者发射的数据交错
    public static void merge() {
        Observable<Integer> just = Observable.just(1, 2, 3, 4, 5);
        Observable<String> just1 = Observable.just("a", "b", "c", "d", "e");
        Observable.merge(just, just1).subscribe(new Consumer<Serializable>() {
            @Override
            public void accept(Serializable serializable) throws Exception {
                LogUtils.e("merge---相关:" + serializable.toString());
            }
        });

        Observable.merge(just1, just).subscribe(x -> LogUtils.e("merge---lambda相关:" + x.toString()));
    }

    //combineLatest用来将多个Observable发射的数据组装起来然后在发射
    //使用场景一: 来做Android表单的校验 ,比如所有的数据不为空的时候,设置提交按钮可以点击
    //combineLatest()验证复杂表单问题实现  https://www.jianshu.com/p/282574438481
    public static void combineLatest() {
        Observable<Integer> observable1 = Observable.just(1, 2, 3);
        Observable<String> observable2 = Observable.just("A", "B", "C");
        Observable.combineLatest(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                LogUtils.e("combineLatest---数据:" + s);
            }
        });

        //lambda写法
        Observable.combineLatest(observable2, observable1, (x, y) -> x + y).subscribe(z -> LogUtils.e("combineLatest---lambda数据:" + z));
    }

    public static void startWitch() {
        Observable.just(1, 2, 3, 5).startWith(9).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                LogUtils.e("startWitch---相关:" + integer);
            }
        });

        //lambda写法
        Observable.just("A", "B", 7, 6).startWith(0).subscribe(x -> LogUtils.e("startWitch---lambda相关:" + x));

        Observable.just("P","K",9,2).startWith(0).subscribe(new Consumer<Serializable>() {
            @Override
            public void accept(Serializable serializable) throws Exception {
                LogUtils.e("startWitch---lambda--2相关:"+serializable.toString());
            }
        });
    }

}
