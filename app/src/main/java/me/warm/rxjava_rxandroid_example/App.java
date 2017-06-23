package me.warm.rxjava_rxandroid_example;

import android.app.Application;

/**
 * 作者: 51hs_android
 * 时间: 2017/5/2
 * 简介:
 */

public class App extends Application {

    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app=this;
    }


    public static App getInstance(){
        return app;
    }

}
