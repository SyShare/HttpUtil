package com.youlu.http.arch;

import android.accounts.NetworkErrorException;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.youlu.http.ApiException;
import com.youlu.http.OkHttpWrapper;
import com.youlu.http.bean.BaseResponse;
import com.youlu.http.util.ApiCodeUtil;
import com.youlu.util.NetUtil;
import com.youlu.util.constans.AbAppData;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * description
 *
 * @author SyShare
 */
public abstract class ApiObserver<T> extends DisposableObserver<T> implements LifecycleObserver {

    private boolean toastError;
    private CompositeDisposable disposables = new CompositeDisposable();
    private Lifecycle mLifecycle;

    public ApiObserver() {
    }


    public ApiObserver(boolean toastError) {
        this.toastError = toastError;
    }

    /**
     * 适用于P层页面销毁时，自动释放资源
     * 目前MVP基础架构已做修改，在P层也可以使用ViewModel
     *
     * @param mLifecycle
     */
    public ApiObserver(@NonNull Lifecycle mLifecycle) {
        this(false, mLifecycle);
    }

    /**
     * 适用于ViewMdodel形式
     *
     * @param viewModel
     */
    public ApiObserver(@NonNull BaseViewModel viewModel) {
        this(false, viewModel);
    }

    public ApiObserver(boolean toastError, @NonNull BaseViewModel viewModel) {
        this(toastError);
        viewModel.add(this);
    }

    public ApiObserver(boolean toastError, @NonNull Lifecycle mLifecycle) {
        this(toastError);
        this.mLifecycle = mLifecycle;
        mLifecycle.addObserver(this);
        addDisposable();
    }

    @Override
    public void onStart() {
        if (!NetUtil.isNetworkAvailable(OkHttpWrapper.getContext())
                && !ApiCodeUtil.INSTANCE.getEnableCache()) {
            dispose();
            onError(new NetworkErrorException());
        }
        if (!isDisposed()) {
            onBefore();
        }
    }

    private void addDisposable() {
        if (disposables == null) {
            disposables = new CompositeDisposable();
        }
        disposables.add(this);
    }

    @Override
    public void onNext(T response) {
        try {
            assertSuccessful(response);
        } catch (Exception e) {
            onError(e);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        Log.wtf("ApiObserver", "It is not a crash, just print StackTrace \n" + Log.getStackTraceString(throwable));
        try {
            if (toastError) {
                OkHttpWrapper.toastError(OkHttpWrapper.getContext(), throwable);
            }
            RxJavaPlugins.getErrorHandler().accept(throwable);
        } catch (Exception e) {
            Log.wtf("ApiObserver", "It is not a crash, just print StackTrace \n" + Log.getStackTraceString(e));
        } finally {
            onFail(throwable);
        }
    }

    @Override
    public void onComplete() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDisPose() {
        if (AbAppData.DEBUG) {
            Log.i("HttpCallback", "onDisPose ");
        }

        if (this.mLifecycle != null) {
            this.mLifecycle.removeObserver(this);
        }

        if (disposables != null) {
            disposables.clear();
        }

    }

    private void assertSuccessful(T response) {
        if (response == null) {
            throw new ApiException("response is null");
        }
        if (response instanceof BaseResponse) {
            BaseResponse resp = (BaseResponse) response;

            if (!resp.isSuccessful()) {
                throw new ApiException(resp.getMsg(), resp.getCode());
            }
        }
        onSuccess(response);
    }

    public void onBefore() {
    }

    public abstract void onSuccess(@NonNull T resp);

    public void onFail(Throwable throwable) {
    }
}
