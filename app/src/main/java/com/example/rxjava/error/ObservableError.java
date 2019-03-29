package com.example.rxjava.error;

import com.blankj.utilcode.util.LogUtils;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

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
                .subscribe(x -> LogUtils.e("retry---lambda--相关:" +x));
    }

    public static void retryWhen(){
        PublishSubject<Object> subject = PublishSubject.create();
    }
}
