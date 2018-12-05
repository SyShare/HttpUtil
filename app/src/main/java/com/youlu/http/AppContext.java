package com.youlu.http;

import android.content.Context;

/**
 * Author by Administrator , Date on 2018/12/5.
 * PS: Not easy to write code, please indicate.
 */
public class AppContext {

    private Context context;
    private boolean isNetWorkValid = true;


    private AppContext() {
    }

    public static AppContext getInstance() {
        return SingleHolder.INSTANCE_;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setNetWorkValid(boolean netWorkValid) {
        isNetWorkValid = netWorkValid;
    }

    public boolean isNetWorkValid() {
        return isNetWorkValid;
    }

    private static class SingleHolder {
        private static final AppContext INSTANCE_ = new AppContext();
    }
}
