package viewmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.support.annotation.NonNull
import com.youlu.http.api.ApiService
import com.youlu.http.arch.BaseViewModel
import com.youlu.http.bean.BaseResponse
import com.youlu.http.models.JokeBean
import io.reactivex.Observable

/**
 * @Description:
 * @Data：2018/7/16-15:02
 * @author: SyShare
 */
class HomeViewModel(@NonNull application: Application) : BaseViewModel<ApiService>(application) {


    /**
     * 段子列表请求
     */
    val jokeModel by lazy { MutableLiveData<BaseResponse<List<JokeBean>>>() }

    fun getRecommondJokes(page: Int) : Observable<BaseResponse<List<JokeBean>>> {
        return mService.getNovelList(1, page)
                .doOnSubscribe { this.add(it) }
    }
}