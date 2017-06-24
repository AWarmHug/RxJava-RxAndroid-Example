package me.warm.rxjava_rxandroid_example.api;

import java.util.List;

import io.reactivex.Observable;
import me.warm.rxjava_rxandroid_example.entity.BaseEntity;
import me.warm.rxjava_rxandroid_example.entity.Data;
import me.warm.rxjava_rxandroid_example.entity.UpData;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by warm on 17/6/23.
 */

public interface Api {

    /**
     * 必须以/结尾，否则会报错
     */
    public static String BASE_URL = "http://gank.io/api/";

    public static final String DOWN_LOAD_URL = "http://imtt.dd.qq.com/16891/E13D0DA60FBB963D59AEC06D1073DC01.apk?fsname=com.glavesoft.drink_3.0.3_303.apk&csr=1bbd";

    public static final String UP_URL="http://erp.51hs.cn/api/2/backend/web/index.php?r=file/upload-single-file";

    @GET("data/{type}/10/1")
    Observable<BaseEntity<List<Data>>> getDate(@Path("type") String type);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);

    @Multipart
    @POST
    Observable<UpData> upData(@Url String upUrl, @Query("ak") String ak, @Query("sn") String sn, @Part("uploadFile\"; filename=\"test.jpg\"") RequestBody imgs);

    @Multipart
    @POST
    Observable<UpData> upData(@Url String upUrl, @Query("ak") String ak, @Query("sn") String sn, @Part MultipartBody.Part file);

}
