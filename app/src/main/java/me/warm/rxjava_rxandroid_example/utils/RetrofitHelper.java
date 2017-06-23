package me.warm.rxjava_rxandroid_example.utils;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import me.warm.rxjava_rxandroid_example.App;
import me.warm.rxjava_rxandroid_example.api.Api;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 51hs_android on 2017/2/7.
 */

public class RetrofitHelper {

    static {
        initOkHttpClient();
    }

    private static OkHttpClient mOkHttpClient;

    private static Retrofit retrofit;

    private static void initOkHttpClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        CacheInterceptor cacheInterceptor = new CacheInterceptor();
        if (mOkHttpClient == null || retrofit == null) {
            synchronized (RetrofitHelper.class) {
                if (mOkHttpClient == null) {
                    //设置Http缓存
                    Cache cache = new Cache(new File(App.getInstance()
                            .getExternalCacheDir(), "HttpCache"), 1024 * 1024 * 10);


                    mOkHttpClient = new OkHttpClient.Builder()
                            .cache(cache)
                            .addInterceptor(interceptor)
                            .addNetworkInterceptor(cacheInterceptor)
                            .retryOnConnectionFailure(true)
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .build();
                }
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(Api.BASE_URL)
                            .client(mOkHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                }
            }
        }
    }

    /**
     * 为okhttp添加缓存，这里是考虑到服务器不支持缓存时，从而让okhttp支持缓存
     */
    private static class CacheInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            // 有网络时 设置缓存超时时间1个小时
            int maxAge = 60*60;
            // 无网络时，设置超时为1天
            int maxStale = 60 * 60 * 24;

            Request request = chain.request();

            if (NetUtil.isNetworkAvailable(App.getInstance())) {
                //有网络时只从网络获取
                request = request.newBuilder()
                        .header("User-Agent","YIXIProject/1.2 ( picsize=iphone6+ ; android 6.0; Scale/2.625)")
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build();
                Log.d("okhttp", "intercept: 有网络时");
            } else {
                //无网络时只从缓存中读取
                request = request.newBuilder()
                        .header("User-Agent","YIXIProject/1.2 ( picsize=iphone6+ ; android 6.0; Scale/2.625)")
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                Log.d("okhttp", "intercept: 无网络时");
//                Toast.makeText(App.getInstance(), "没有网络", Toast.LENGTH_SHORT).show();
            }

            Response response = chain.proceed(request);


            if (NetUtil.isNetworkAvailable(App.getInstance())) {
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public ,max-age=" + maxAge)
                        .build();
            } else {
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();

            }
            return response;
        }
    }


    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static <T> T getApi(Class<T> cla) {


        return retrofit.create(cla);
    }



}
