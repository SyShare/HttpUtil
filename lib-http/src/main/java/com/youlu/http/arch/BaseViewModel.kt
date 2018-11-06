package com.youlu.http.arch

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.youlu.http.util.TypeUtil.findParamsTypeClass
import com.youlu.http.RetrofitManager
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * 拓展dataError和dataEmpty两个字段
 * T 表示对应的retrofit service类型
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseViewModel<T>(application: Application) : AndroidViewModel(application) {

    public val mService: T
    private var disposables: CompositeDisposable? = null

    init {
        mService = RetrofitManager.getService(findParamsTypeClass(javaClass)) as T
    }

    fun add(o: Any) {
        if (o is Disposable) {
            addDisposable(o)
        } else {
            throw IllegalArgumentException("参数有误,请传入可取消的订阅者")
        }
    }

    private fun addDisposable(disposable: Disposable) {
        if (disposables == null) {
            disposables = CompositeDisposable()
        }
        disposables?.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposables?.clear()
    }

}
