package me.warm.rxjava_rxandroid_example;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.warm.rxjava_rxandroid_example.api.Api;
import me.warm.rxjava_rxandroid_example.utils.RetrofitHelper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

/**
 * Created by warm on 17/6/23.
 */

public class RetrofitActivity extends AppCompatActivity {
    private static final String TAG = "RetrofitActivity1";
    private Button bt_get, bt_up,bt_down_load;
    private TextView content;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        Toolbar tb = (Toolbar) this.findViewById(R.id.tb);
        tb.inflateMenu(R.menu.main);
        tb.setOnMenuItemClickListener(item -> {
            content.setText("");
            return true;
        });
        content = (TextView) this.findViewById(R.id.content);
        bt_get = (Button) this.findViewById(R.id.bt_get);
        bt_up= (Button) this.findViewById(R.id.bt_up);
        bt_down_load = (Button) this.findViewById(R.id.bt_down_load);
        doGet();
        doUp();
        doDonwLoad();

    }

    private void doUp() {
        String picPath="/storage/emulated/0/51Drink/image/zip_image/1498273990632.jpg";
//        RequestBody requestBody=RequestBody.create(MediaType.parse("multipart/image"),picPath);
        RequestBody requestBody=RequestBody.create(MediaType.parse("multipart/form-data"),new File(picPath));
        MultipartBody.Part part=MultipartBody.Part.createFormData("hh","hh.jpg",requestBody);

        RxView.clicks(bt_up).throttleFirst(1,TimeUnit.SECONDS)
                .flatMap(o -> RetrofitHelper.getApi(Api.class)
                                .upData(Api.UP_URL,"IXuNz/MuG7VUJJ1Eg4octg==","3ELIRzfBTMDfIddns2W1lVoDW17s5ra1uia+iYkZ/1F7nUIRrnXyBlWn1RSE6SjI",part)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread()))
                .subscribe(upData -> {
                    Log.d(TAG, "doUp: "+upData.toString());
                });



    }

    private void doDonwLoad() {
        RxView.clicks(bt_down_load).throttleFirst(1, TimeUnit.SECONDS)
                .flatMap(o -> RetrofitHelper.getApi(Api.class)
                        .downloadFile(Api.DOWN_LOAD_URL)
                        .filter(responseBody -> responseBody != null)
                        .flatMap(responseBody -> {
                            InputStream in = responseBody.byteStream();
                            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/test");
                            if (!file.exists()) {
                                file.mkdirs();
                            }
                            File apkFile = new File(file, "ff.apk");
                            if (!apkFile.exists()){
                                apkFile.createNewFile();
                            }
                            FileOutputStream fileOutputStream = null;
                            try {
                                fileOutputStream = new FileOutputStream(apkFile);
                                byte[] buffer = new byte[2048];
                                int len = 0;
                                while ((len = in.read(buffer)) != -1) {
                                    fileOutputStream.write(buffer, 0, len);
                                }
                                fileOutputStream.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return Observable.just(apkFile.getPath());
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()))
                .subscribe(s -> {
                    Log.d(TAG, "doDonwLoad: " + s);
                }, throwable -> {
                    Log.d(TAG, "doDonwLoad: " + throwable.getMessage());
                });


    }


    /**
     * 需要注意：
     * 在进行网络请求时，subscribeOn(Schedulers.io())需要紧跟在网络请求的ObservableSource后面，
     * 如果写在RxBinding上，会导致在子线程中操作Ui的错误
     */
    private void doGet() {

        RxView.clicks(bt_get).throttleFirst(1, TimeUnit.SECONDS)
                .flatMap(o -> RetrofitHelper.getApi(Api.class).getDate("Android")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()))
                .filter(listBaseEntity -> !listBaseEntity.isError())
                .map(listBaseEntity -> listBaseEntity.getResults())
                .subscribe(datas -> content.append(datas.toString())
                        , throwable -> Log.d(TAG, "accept: " + throwable));
    }


}
