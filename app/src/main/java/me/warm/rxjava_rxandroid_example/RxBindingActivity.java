package me.warm.rxjava_rxandroid_example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者: 51hs_android
 * 时间: 2017/6/22
 * 简介:
 */

public class RxBindingActivity extends AppCompatActivity {
    private static final String TAG = "RxBindingActivity1";
    private Button bt;
    private EditText et;
    private Disposable subscribe;

    /**
     * 添加所有的Disposable，在最后集中处理
     */
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxbinding);
        bt = (Button) this.findViewById(R.id.bt);
        et = (EditText) this.findViewById(R.id.et);
        compositeDisposable = new CompositeDisposable();
        subscribe = RxView.clicks(bt).debounce(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Log.d(TAG, "accept: ");
                        et.setText("哈哈");

                    }
                });
        compositeDisposable.add(subscribe);
        RxTextView.textChanges(et).debounce(2, TimeUnit.SECONDS).filter(new Predicate<CharSequence>() {
            @Override
            public boolean test(@NonNull CharSequence charSequence) throws Exception {
                return !TextUtils.isEmpty(charSequence);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CharSequence>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull CharSequence charSequence) {
                        Log.d(TAG, "onNext: "+charSequence);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
        compositeDisposable.clear();
    }
}
