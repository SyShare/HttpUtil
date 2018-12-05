package com.youlu.http;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;

import com.youlu.http.interceptor.CacheInterceptor;
import com.youlu.http.interceptor.CacheNetworkInterceptor;
import com.youlu.http.interceptor.LoggingInterceptor;
import com.youlu.logger.LoggerUtil;

import java.io.File;

import okhttp3.Cache;

/**
 * Author by Administrator , Date on 2018/9/19.
 * PS: Not easy to write code, please indicate.
 */
public class TestApplication extends Application {

    /**
     * 初始化网络库
     */
    private static void initNetwork(Application app) {
        RetrofitManager.okHttpBuilder(app)
                .setLogEnable(!BuildConfig.BUILD_TYPE.equals("release"))
                .setSuccessCode(200)
                .enableCache(true)
                .cacheSize(new Cache(new File(app.getExternalCacheDir(), "ok-cache"), 1024 * 1024 * 30L))
                .addNormalInterceptor(new CacheInterceptor())
                .addInterceptor(new CacheNetworkInterceptor())
                .addInterceptor(new LoggingInterceptor())
                .addErrorHandler(throwable -> {
                    if (throwable instanceof ApiException) {
                        ApiException apiException = (ApiException) throwable;

                    }

                })
                .retrofitBuilder("https://www.apiopen.top")
                .build();
    }

    /**
     * 日志工具初始化
     */
    private static void initLogger() {
        LoggerUtil.LogConfig logConfig = new LoggerUtil.LogConfig() {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return BuildConfig.DEBUG;
            }

            @Override
            public boolean isSaveToFile(int priority, @Nullable String tag, @NonNull String message) {
                if (BuildConfig.DEBUG && priority >= LoggerUtil.INFO) {
                    return true;
                }

                if (priority >= LoggerUtil.WARN) {
                    return true;
                }
                return false;
            }
        };
        logConfig.headTag = BuildConfig.APPLICATION_ID + (BuildConfig.DEBUG ? "_debug" : "");
        logConfig.logDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "hd_log";
        LoggerUtil.init(logConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext.getInstance().setContext(this);
        initLogger();
        initNetwork(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
