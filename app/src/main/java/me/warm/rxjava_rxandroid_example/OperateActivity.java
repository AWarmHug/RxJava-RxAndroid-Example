package me.warm.rxjava_rxandroid_example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import me.warm.rxjava_rxandroid_example.entity.Family;
import me.warm.rxjava_rxandroid_example.entity.LoginInfo;
import me.warm.rxjava_rxandroid_example.entity.Parent;
import me.warm.rxjava_rxandroid_example.entity.User;

/**
 * Created by warm on 17/6/20.
 */

public class OperateActivity extends AppCompatActivity implements TextWatcher {
    private static final String TAG = "OperateActivity";

    private TextView content;
    private EditText et_sample;


    LoginInfo u = new LoginInfo(true, new User(10002, 12, "王二"));

    LoginInfo u2 = new LoginInfo(true, new User(10003, 13, "张三"));
    LoginInfo u3 = new LoginInfo(false, new User(10004, 14, "李四"));
    LoginInfo u4 = new LoginInfo(true, new User(10005, 15, "陆五"));

    LoginInfo u5 = new LoginInfo(true, new User(10006, 16, "赵六"));

    LoginInfo u6 = new LoginInfo(true, new User(10007, 17, "冯七"));
    LoginInfo u7 = new LoginInfo(false, new User(10008, 18, "程八"));
    LoginInfo u8 = new LoginInfo(true, new User(10009, 19, "欧阳九"));

    Parent p = new Parent(10002, "王爹", "王妈");
    Parent p2 = new Parent(10003, "张爹", "张妈");
    Parent p3 = new Parent(10004, "李爹", "李妈");
    Parent p4 = new Parent(10005, "陆爹", "陆妈");
    Parent p5 = new Parent(10006, "赵爹", "赵妈");
    Parent p6 = new Parent(10007, "冯爹", "冯妈");
    Parent p7 = new Parent(10008, "程爹", "程妈");
    Parent p8 = new Parent(10008, "欧阳爹", "欧阳妈");


    CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate);
        content = (TextView) this.findViewById(R.id.content);
        et_sample = (EditText) this.findViewById(R.id.et_sample);
        et_sample.addTextChangedListener(this);
        Toolbar tb = (Toolbar) this.findViewById(R.id.tb);
        tb.inflateMenu(R.menu.main);
        tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                content.setText("");
                return true;
            }
        });

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create:
                Observable.create(new ObservableOnSubscribe<LoginInfo>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<LoginInfo> e) throws Exception {

                        e.onNext(u);
                        e.onNext(u2);
                        e.onNext(u3);
                        e.onNext(u4);
                        e.onComplete();

//              e.onError(new Throwable("未登陆"));
//              当onError()调用后，不再调用onComplete()
//              e.onComplete();
                    }
                }).subscribe(new Observer<LoginInfo>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe: " + d.isDisposed());
                    }

                    @Override
                    public void onNext(@NonNull LoginInfo loginInfo) {

                        content.append(loginInfo.toString());

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        content.setText(e.getMessage());

                    }

                    @Override
                    public void onComplete() {
                        content.append("onComplete()");

                    }
                });

                break;
            case R.id.just:

                Observable.just(u, u2, u3, u4).subscribe(new Observer<LoginInfo>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe: " + d.isDisposed());
                    }

                    @Override
                    public void onNext(@NonNull LoginInfo loginInfo) {

                        content.append(loginInfo.toString());

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        content.setText(e.getMessage());

                    }

                    @Override
                    public void onComplete() {
                        content.append("onComplete()");

                    }
                });


                break;

            case R.id.from:
                List<LoginInfo> loginInfos = new ArrayList<>();
                loginInfos.add(u);
                loginInfos.add(u2);
                loginInfos.add(u3);
                loginInfos.add(u4);

                Observable.fromIterable(loginInfos)
                        .subscribe(new Observer<LoginInfo>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                Log.d(TAG, "onSubscribe: " + d.isDisposed());
                            }

                            @Override
                            public void onNext(@NonNull LoginInfo loginInfo) {

                                content.append(loginInfo.toString());

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                content.setText(e.getMessage());

                            }

                            @Override
                            public void onComplete() {
                                content.append("onComplete()");

                            }
                        });


                break;

            case R.id.concat:


                Observable<LoginInfo> observable = Observable.create(new ObservableOnSubscribe<LoginInfo>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<LoginInfo> e) throws Exception {

                        e.onNext(u5);
                        e.onNext(u6);
                        e.onNext(u7);
                        e.onNext(u8);
                        e.onComplete();

//              e.onError(new Throwable("未登陆"));
//              当onError()调用后，不再调用onComplete()
//              e.onComplete();
                    }
                });
                //将两个Obserable连接起来,即使不在一个线程也会按顺序
                Observable.concat(getObservable().subscribeOn(Schedulers.io()), observable.subscribeOn(Schedulers.io()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<LoginInfo>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                Log.d(TAG, "onSubscribe: " + d.isDisposed());
                            }

                            @Override
                            public void onNext(@NonNull LoginInfo loginInfo) {

                                content.append(loginInfo.toString());

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                content.setText(e.getMessage());

                            }

                            @Override
                            public void onComplete() {
                                content.append("onComplete()");

                            }
                        });

                break;
            case R.id.defer:
                //这个会缓存Observable，仍然可以对他进行操作，直到subscribe时，才真正使用
                final List<LoginInfo> deferLoginInfos = new ArrayList<>();
                deferLoginInfos.add(u);
                deferLoginInfos.add(u2);
                deferLoginInfos.add(u3);
                deferLoginInfos.add(u4);

                Observable<LoginInfo> defer = Observable.defer(new Callable<ObservableSource<LoginInfo>>() {
                    @Override
                    public ObservableSource<LoginInfo> call() throws Exception {
                        return Observable.fromIterable(deferLoginInfos);
                    }
                });
                // 在Observable创建好了之后，有添加了一个，结果输出5个。
                deferLoginInfos.add(u5);

                defer.subscribe(new Observer<LoginInfo>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe: " + d.isDisposed());
                    }

                    @Override
                    public void onNext(@NonNull LoginInfo loginInfo) {

                        content.append(loginInfo.toString());

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        content.setText(e.getMessage());

                    }

                    @Override
                    public void onComplete() {
                        content.append("onComplete()");

                    }
                });

                break;


            case R.id.timer:
                Observable.timer(2, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@NonNull Long aLong) {
                                content.append("aLong=" + aLong);


                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                content.append("onComplete");


                            }
                        });


                break;

            case R.id.interval:
                //默认自己开启一个线程，每隔2s,发送一个数字，从0开始到无穷大
                //所以需要在onDestroy中解除绑定
//                disposables.add(Observable.interval(2,TimeUnit.SECONDS)
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeWith(new DisposableObserver<Long>() {
//                            @Override
//                            public void onNext(@NonNull Long aLong) {
//                                Log.d(TAG, "onNext= "+aLong);
//                                content.append( "onNext= "+aLong);
//
//                            }
//
//                            @Override
//                            public void onError(@NonNull Throwable e) {
//                                Log.d(TAG, "onError= "+e.getMessage());
//                                content.append( "onError "+e.getMessage());
//
//                            }
//
//                            @Override
//                            public void onComplete() {
//                                content.append( "onComplete ");
//
//
//                            }
//                        }));
                mDisposable = Observable.interval(2, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(@NonNull Long aLong) throws Exception {
                                Log.d(TAG, "onNext= " + aLong);
                                content.append("onNext= " + aLong);
                            }
                        });

                break;

            case R.id.range:
                //发送3后面10个数
                Observable.range(3, 10).subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        Log.d(TAG, "onNext= " + integer);
                        content.append("onNext= " + integer);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        content.append("onComplete");

                    }
                });
                break;
            case R.id.repeat:
                //重复，使用repeat，会重复两次操作
                Observable.range(3, 5).repeat(2).subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        Log.d(TAG, "onNext= " + integer);
                        content.append("onNext= " + integer);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        content.append("onComplete");

                    }
                });
                break;
            case R.id.repeatWhen:

                break;


            /*******************************************************************************************************************************/


            case R.id.map:
                getObservable().map(new Function<LoginInfo, LoginInfo>() {
                    @Override
                    public LoginInfo apply(@NonNull LoginInfo loginInfo) throws Exception {
                        return goLogin(loginInfo);

                    }
                }).subscribe(new Observer<LoginInfo>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull LoginInfo loginInfo) {

                        content.append(loginInfo.toString());

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        content.append(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        content.append("onComplete");

                    }
                });
                break;
            case R.id.flatMap:
                getObservable().flatMap(new Function<LoginInfo, ObservableSource<User>>() {
                    @Override
                    public ObservableSource<User> apply(@NonNull final LoginInfo loginInfo) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<User>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<User> e) throws Exception {
                                if (loginInfo.isLogin()) {
                                    Log.d(TAG, "subscribe: " + Thread.currentThread().getName());
                                    e.onNext(loginInfo.getUser());
                                    e.onComplete();
                                }
                            }
                        });
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<User>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@NonNull User user) {

                                content.append(user.toString());

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                content.append(e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                content.append("onComplete");

                            }
                        });
                break;
            case R.id.filter:
                //过滤操作符
                getObservable().filter(new Predicate<LoginInfo>() {
                    @Override
                    public boolean test(@NonNull LoginInfo loginInfo) throws Exception {
                        return loginInfo.isLogin();
                    }
                }).subscribe(new Observer<LoginInfo>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull LoginInfo loginInfo) {
                        content.append(loginInfo.toString());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        content.append(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        content.append("onComplete");

                    }
                });
                break;
            case R.id.take:
                //只取前几个
                getObservable().take(3).subscribe(new Observer<LoginInfo>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull LoginInfo loginInfo) {
                        content.append(loginInfo.toString());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        content.append(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        content.append("onComplete");

                    }
                });

                break;
            case R.id.delay:
                //延迟几秒后发送
                getObservable().delay(2, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<LoginInfo>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@NonNull LoginInfo loginInfo) {
                                content.append(loginInfo.toString());
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                content.append(e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                content.append("onComplete");
                            }
                        });
                break;
            case R.id.buffer:
                // TODO: 17/6/21 还需要继续学习
                Observable.just(1, 2, 3, 4, 5)
                        .buffer(3)
                        .subscribe(new Observer<List<Integer>>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@NonNull List<Integer> integers) {
                                content.append(integers.toString());


                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                content.append("onComplete");

                            }
                        });


                break;

            case R.id.merge:
                //和concat类似，但是如果不在同一个线程中不按顺序
                Observable.merge(getObservable().subscribeOn(Schedulers.io()), Observable.just(u5, u6, u7).subscribeOn(Schedulers.io()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<LoginInfo>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@NonNull LoginInfo loginInfo) {
                                content.append(loginInfo.toString());


                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                content.append("onComplete");

                            }
                        });

                break;
            case R.id.zip:

                Observable<User> user = Observable.just(u, u2, u3, u4).filter(new Predicate<LoginInfo>() {
                    @Override
                    public boolean test(@NonNull LoginInfo loginInfo) throws Exception {
                        //检查有没有登录
                        return loginInfo.isLogin();
                    }
                }).map(new Function<LoginInfo, User>() {
                    @Override
                    public User apply(@NonNull LoginInfo loginInfo) throws Exception {
                        //登录的User信息
                        return loginInfo.getUser();
                    }
                });

                Observable<Parent> parent = user.flatMap(new Function<User, ObservableSource<Parent>>() {
                    @Override
                    public ObservableSource<Parent> apply(@NonNull final User user) throws Exception {
                        return Observable.just(p, p2, p3, p4).filter(new Predicate<Parent>() {
                            @Override
                            public boolean test(@NonNull Parent parent) throws Exception {
                                //判断user的id和父母的id是否相同
                                return parent.getId() == user.getId();
                            }
                        });
                    }
                });
                Observable.zip(user, parent, new BiFunction<User, Parent, Family>() {
                    @Override
                    public Family apply(@NonNull User user, @NonNull Parent parent) throws Exception {
                        return new Family(user, parent);
                    }
                }).subscribe(new Observer<Family>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Family family) {
                        content.append(family.toString());

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        content.append("onComplete");

                    }
                });
                break;
            case R.id.first:
                Observable.just(u, u2, u3, u4).first(u).subscribe(new Consumer<LoginInfo>() {
                    @Override
                    public void accept(@NonNull LoginInfo loginInfo) throws Exception {
                        content.append(loginInfo.toString());

                    }
                });

                break;
            case R.id.elementAt:
                Observable.just(u, u2, u3, u4).elementAt(3).subscribe(new Consumer<LoginInfo>() {
                    @Override
                    public void accept(@NonNull LoginInfo loginInfo) throws Exception {
                        content.append(loginInfo.toString());

                    }
                });
                break;
            case R.id.ofType:
                //检查类型
                Observable.just(u, 1, u3, u4).ofType(LoginInfo.class).subscribe(new Consumer<LoginInfo>() {
                    @Override
                    public void accept(@NonNull LoginInfo loginInfo) throws Exception {
                        content.append(loginInfo.toString());

                    }
                });
                break;
            case R.id.exists:


                break;

            case R.id.debounce:

                Observable.create(new ObservableOnSubscribe<LoginInfo>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<LoginInfo> e) throws Exception {
                        e.onNext(u);
                        e.onNext(u2);
                    }
                }).debounce(2, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<LoginInfo>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@NonNull LoginInfo loginInfo) {
                                content.append(loginInfo.toString());

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                break;

            case R.id.sample1:
                //获取登录了的User的父母信息。
                Observable.just(u, u2, u3, u4).filter(new Predicate<LoginInfo>() {
                    @Override
                    public boolean test(@NonNull LoginInfo loginInfo) throws Exception {
                        //检查有没有登录
                        return loginInfo.isLogin();
                    }
                }).map(new Function<LoginInfo, User>() {
                    @Override
                    public User apply(@NonNull LoginInfo loginInfo) throws Exception {
                        //登录的User信息
                        return loginInfo.getUser();
                    }
                }).flatMap(new Function<User, ObservableSource<Parent>>() {
                    @Override
                    public ObservableSource<Parent> apply(@NonNull final User user) throws Exception {
                        return Observable.just(p, p2, p3, p4).filter(new Predicate<Parent>() {
                            @Override
                            public boolean test(@NonNull Parent parent) throws Exception {
                                //判断user的id和父母的id是否相同
                                return parent.getId() == user.getId();
                            }
                        });
                    }
                }).subscribe(new Observer<Parent>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Parent parent) {
                        content.append(parent.toString());

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        content.append("onComplete");

                    }
                });
                break;


        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    private int time;

    @Override
    public void onTextChanged(final CharSequence s, int start, int before, int count) {
        Observable.create(new ObservableOnSubscribe<CharSequence>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<CharSequence> e) throws Exception {
                e.onNext(s);
            }
        }).debounce(2, TimeUnit.SECONDS)
                .switchMap(new Function<CharSequence, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull final CharSequence charSequence) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                                e.onNext(charSequence.toString());
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String ss) {
                        Log.d(TAG, "onTextChanged: " + ss);
                        content.append(ss);

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
    public void afterTextChanged(Editable s) {

    }


    private LoginInfo goLogin(LoginInfo login) {
        login.setLogin(true);
        return login;
    }

    private Observable<LoginInfo> getObservable() {
        return Observable.create(new ObservableOnSubscribe<LoginInfo>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<LoginInfo> e) throws Exception {

                e.onNext(u);
                e.onNext(u2);
                e.onNext(u3);
                e.onNext(u4);
                e.onComplete();

//              e.onError(new Throwable("未登陆"));
//              当onError()调用后，不再调用onComplete()
//              e.onComplete();

            }
        });
    }

    private Disposable mDisposable;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        disposables.clear();

    }
}
