package com.example.rxjava.backPressure;

import com.blankj.utilcode.util.LogUtils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BackPressureObServable {


    /**
     * Bug产生的原因:
     * 被观察者发送事件速度太快，而观察者来不及接收所有事件，从而导致观察者无法及时响应 / 处理所有发送过来事件的问题，最终导致缓存区溢出、事件丢失 & OOM
     */
    public static void fastObserver() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {
                    LogUtils.e("发送了事件:" + i);
                    Thread.sleep(10);
                    emitter.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtils.e("开始采用Subscribe链接");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        try {
                            Thread.sleep(5000);
                            LogUtils.e("接收到了事件:" + integer);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("onError()---响应");
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.e("onComplete()---响应");
                    }
                });
    }

    /**
     * 背压策略
     */
    public static void backPressurce() {
        Flowable.create((FlowableOnSubscribe<Integer>) emitter -> {
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
        }, BackpressureStrategy.ERROR)//需要传入背压参数
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        // 对比Observer传入的Disposable参数，Subscriber此处传入的参数 = Subscription
                        // 相同点：Subscription具备Disposable参数的作用，即Disposable.dispose()切断连接, 同样的调用Subscription.cancel()切断连接
                        // 不同点：Subscription增加了void request(long n)
                        LogUtils.e("onSubscribe()---响应");
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        LogUtils.e("onNext()---响应:" + integer);

                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e("onError()---响应");
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.e("onComplete()---响应");
                    }
                });
    }

}
