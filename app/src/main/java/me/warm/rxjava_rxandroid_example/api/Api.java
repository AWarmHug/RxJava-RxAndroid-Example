package me.warm.rxjava_rxandroid_example.api;

import java.util.List;

import io.reactivex.Observable;
import me.warm.rxjava_rxandroid_example.entity.BaseEntity;
import me.warm.rxjava_rxandroid_example.entity.Data;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by warm on 17/6/23.
 */

public interface Api {
    public static String BASE_URL="http://gank.io/api/";

    @GET("data/{type}/10/1")
    Observable<BaseEntity<List<Data>>> getDate(@Path("type") String type);


}
