package me.warm.rxjava_rxandroid_example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout;
import com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEvent;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import me.warm.rxjava_rxandroid_example.entity.BaseEntity;

/**
 * Created by warm on 17/6/23.
 */

public class RxBindingRecyclViewActivity extends AppCompatActivity {
    private static final String TAG = "RxBindingRecyclViewActi";
    private SwipeRefreshLayout srl;
    private RecyclerView rv;
    private static final int NUM = 30;

    private boolean loadMore = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxbinding_recycleview);
        srl = (SwipeRefreshLayout) this.findViewById(R.id.srl);
        rv = (RecyclerView) this.findViewById(R.id.rv);
        final List<String> strs = new ArrayList<>();
        for (int i = 0; i < NUM; i++) {
            strs.add("这是----" + i);
        }
        final Adapter adapter = new Adapter(strs);
        rv.setAdapter(adapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);
        RxRecyclerView
                .scrollEvents(rv)
                .filter(new Predicate<RecyclerViewScrollEvent>() {
                    @Override
                    public boolean test(@NonNull RecyclerViewScrollEvent recyclerViewScrollEvent) throws Exception {
                        int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                        int totalItemCount = linearLayoutManager.getItemCount();

                        if (lastVisibleItem >= totalItemCount - 4 && loadMore) {
                            loadMore = false;
                            return true;
                        } else {
                            return false;
                        }
                    }
                })
                .flatMap(new Function<Object, ObservableSource<BaseEntity<List<String>>>>() {
                    @Override
                    public ObservableSource<BaseEntity<List<String>>> apply(@NonNull Object o) throws Exception {
                        final List<String> strs = new ArrayList<>();
                        for (int i = adapter.getItemCount(); i < adapter.getItemCount() + NUM; i++) {
                            strs.add("这是加载的----" + i);
                        }
                        BaseEntity<List<String>> entity = new BaseEntity<>();
                        entity.setError(false);
                        entity.setResults(strs);
                        return Observable.just(entity).delay(3, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .filter(new Predicate<BaseEntity<List<String>>>() {
                    @Override
                    public boolean test(@NonNull BaseEntity<List<String>> listBaseEntity) throws Exception {
                        return !listBaseEntity.isError();
                    }
                })
                .map(new Function<BaseEntity<List<String>>, List<String>>() {
                    @Override
                    public List<String> apply(@NonNull BaseEntity<List<String>> listBaseEntity) throws Exception {
                        return listBaseEntity.getResults();
                    }
                })
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(@NonNull List<String> strings) throws Exception {


                        adapter.addAllData(strings);
                        loadMore = true;
                    }
                });

        RxSwipeRefreshLayout.refreshes(srl)
                .flatMap(new Function<Object, ObservableSource<BaseEntity<List<String>>>>() {
                    @Override
                    public ObservableSource<BaseEntity<List<String>>> apply(@NonNull Object o) throws Exception {
                        final List<String> strs = new ArrayList<>();
                        for (int i = 0; i < NUM; i++) {
                            strs.add("这是新的----" + i);
                        }
                        BaseEntity<List<String>> entity = new BaseEntity<>();
                        entity.setError(false);
                        entity.setResults(strs);
                        return Observable.just(entity).delay(3, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .filter(new Predicate<BaseEntity<List<String>>>() {
                    @Override
                    public boolean test(@NonNull BaseEntity<List<String>> listBaseEntity) throws Exception {
                        return !listBaseEntity.isError();
                    }
                })
                .map(new Function<BaseEntity<List<String>>, List<String>>() {
                    @Override
                    public List<String> apply(@NonNull BaseEntity<List<String>> listBaseEntity) throws Exception {
                        return listBaseEntity.getResults();
                    }
                })
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(@NonNull List<String> strings) throws Exception {
                        srl.setRefreshing(false);

                        adapter.refreshAll(strings);
                    }
                });
    }


    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private List<String> strs;

        public Adapter(List<String> strs) {
            this.strs = strs;
        }


        /**
         * 在尾部增加全部
         *
         * @param tList
         */
        public void addAllData(List<String> tList) {
            strs.addAll(tList);
            notifyDataSetChanged();
        }


        /**
         * 刷新全部
         *
         * @param tList
         */
        public void refreshAll(List<String> tList) {
            strs.clear();
            strs.addAll(tList);
            Log.d(TAG, "refreshAll: " + strs.toString());
            notifyDataSetChanged();
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.text.setText(strs.get(position));

        }

        @Override
        public int getItemCount() {
            return strs.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView text;

            public ViewHolder(View itemView) {
                super(itemView);
                text = (TextView) itemView.findViewById(R.id.text);
            }
        }
    }
}
