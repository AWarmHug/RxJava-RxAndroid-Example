package me.warm.rxjava_rxandroid_example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import me.warm.rxjava_rxandroid_example.api.Api;
import me.warm.rxjava_rxandroid_example.entity.BaseEntity;
import me.warm.rxjava_rxandroid_example.entity.Data;
import me.warm.rxjava_rxandroid_example.utils.RetrofitHelper;

/**
 * Created by warm on 17/6/23.
 */

public class RetrofitActivity extends AppCompatActivity {
    private static final String TAG = "RetrofitActivity1";
    private Button bt_get;
    private TextView content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        Toolbar tb = (Toolbar) this.findViewById(R.id.tb);
        tb.inflateMenu(R.menu.main);
        tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                content.setText("");
                return true;
            }
        });
        content = (TextView) this.findViewById(R.id.content);
        bt_get = (Button) this.findViewById(R.id.bt_get);

        doGet();

    }


    /**
     * 需要注意：
     * 在进行网络请求时，subscribeOn(Schedulers.io())需要紧跟在网络请求的ObservableSource后面，
     * 如果写在RxBinding上，会导致在子线程中操作Ui的错误
     */
    private void doGet() {

        RxView.clicks(bt_get).throttleFirst(1, TimeUnit.SECONDS)
                .flatMap(new Function<Object, ObservableSource<BaseEntity<List<Data>>>>() {
                    @Override
                    public ObservableSource<BaseEntity<List<Data>>> apply(@NonNull Object o) throws Exception {
                        return RetrofitHelper.getApi(Api.class).getDate("Android")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .filter(new Predicate<BaseEntity<List<Data>>>() {
                    @Override
                    public boolean test(@NonNull BaseEntity<List<Data>> listBaseEntity) throws Exception {
                        return !listBaseEntity.isError();
                    }
                })
                .map(listBaseEntity -> listBaseEntity.getResults())
                .subscribe(datas -> content.append(datas.toString())
                        , throwable -> Log.d(TAG, "accept: "+throwable));
    }


}
