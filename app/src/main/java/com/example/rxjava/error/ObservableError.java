package com.example.rxjava.error;

import com.blankj.utilcode.util.LogUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 错误操作符
 */
public class ObservableError {

    //retry： 当原始Observable在遇到错误时进行重试。如果重复过后还是错误,就崩溃
    public static void retry() {
        Observable.just(4, 8, 3, 5, 2)
                .cast(Integer.class)
                .retry(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtils.e("retry---相关:" + integer);
                    }
                });

        //lambda写法
        Observable.just("1", 2, 3, "5", 6).cast(String.class)
                .retry(2)
                .subscribe(x -> LogUtils.e("retry---lambda--相关:" + x));
    }

    public static void retryWhen() {
        Observable.just(1, "0", 5)
                .cast(Integer.class)
                .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                        return Observable.just(4, 5);
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtils.e("------retryWhen相关:" + integer);
                    }
                });

        //lambda写法
        Observable.just("D", "9", 2, "5")
                .cast(String.class)
                .retryWhen(a -> Observable.just("3", "00"))
                .subscribe(x -> LogUtils.e("----retryWhen--lambda相关:" + x));
    }
}
