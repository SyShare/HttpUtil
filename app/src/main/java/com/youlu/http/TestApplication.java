package com.youlu.http;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.youlu.http.api.ApiService;

/**
 * Author by Administrator , Date on 2018/9/19.
 * PS: Not easy to write code, please indicate.
 */
public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initNetwork(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    /**
     * 初始化网络库
     */
    private static void initNetwork(Application app) {
        RetrofitManager.okHttpBuilder(app)
                .setLogEnable(!BuildConfig.BUILD_TYPE.equals("release"))
                .setSuccessCode(200)
                .addErrorHandler(throwable -> {
                    if (throwable instanceof ApiException) {
                        ApiException apiException = (ApiException) throwable;

                    }

                })
                .retrofitBuilder("https://www.apiopen.top")
                .build();
    }
}
