package me.warm.rxjava_rxandroid_example;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxSeekBar;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者: 51hs_android
 * 时间: 2017/6/22
 * 简介: RxBinding 只需要在使用时进行熟悉就行。
 * 可以学习源码，尝试自定义一个控件，然后仿写
 */

public class RxBindingActivity extends AppCompatActivity {
    private static final String TAG = "RxBindingActivity1";
    private static final int MAX_WAIT_TIME = 10;


    //    private Button bt;
    private Button countDown;
    private EditText et;
    private TextView content;
    private AppCompatSeekBar seekBar;
    private Button bt;
    private Button go_recy;

    /**
     * 添加所有的Disposable，在最后集中处理
     */
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxbinding);
        Toolbar tb = (Toolbar) this.findViewById(R.id.tb);
        tb.inflateMenu(R.menu.main);
        tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                content.setText("");
                return true;
            }
        });
        seekBar = (AppCompatSeekBar) this.findViewById(R.id.seekBar);

        bt = (Button) this.findViewById(R.id.bt);
        et = (EditText) this.findViewById(R.id.et);
        go_recy = (Button) this.findViewById(R.id.go_recy);
        countDown = (Button) this.findViewById(R.id.countDown);


        content = (TextView) this.findViewById(R.id.content);
        compositeDisposable = new CompositeDisposable();

        doClick();

        doEdit();


        doCountDown();

        doSeekBar();
        goRecy();

    }

    private void goRecy() {
        RxView.clicks(go_recy)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        startActivity(new Intent(RxBindingActivity.this, RxBindingRecyclViewActivity.class));
                    }
                });
    }

    private void doSeekBar() {
        RxSeekBar.changes(seekBar).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                content.setText("进度=" + integer);
            }
        });
    }


    private void doClick() {
        RxView.clicks(bt).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Object o) {
                        content.append("点击了,1s后点击才有效");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        content.append("onComplete");

                    }
                });
    }

    /**
     * debounce :延迟一段时间
     * switchMap，以最后一个为准，放置网络请求时，第一个还没返还，第二个已经返回了，数据乱的情况
     */
    private void doEdit() {
        RxTextView.textChanges(et).debounce(1, TimeUnit.SECONDS).switchMap(new Function<CharSequence, ObservableSource<CharSequence>>() {
            @Override
            public ObservableSource<CharSequence> apply(@NonNull CharSequence charSequence) throws Exception {
                return Observable.just(charSequence);
            }
        }).filter(new Predicate<CharSequence>() {
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
                        content.setText(charSequence);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void doCountDown() {
        Disposable s = RxView.clicks(countDown).throttleFirst(MAX_WAIT_TIME, TimeUnit.SECONDS)
                .flatMap(new Function<Object, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(@NonNull Object o) throws Exception {
                        return Observable.intervalRange(0, MAX_WAIT_TIME + 1, 0, 1, TimeUnit.SECONDS);
                    }
                }).map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long aLong) throws Exception {
                        return MAX_WAIT_TIME - aLong;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        if (aLong > 0) {
                            countDown.setText("还需等待" + aLong + "秒");
                        } else {
                            countDown.setText("点击获取");
                        }
                    }
                });
        compositeDisposable.add(s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
        compositeDisposable.clear();
    }
}
