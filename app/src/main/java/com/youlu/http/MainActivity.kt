package com.youlu.http

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.youlu.http.api.ApiService
import com.youlu.http.arch.ApiObserver
import com.youlu.http.bean.BaseResponse
import com.youlu.http.models.JokeBean
import viewmodel.HomeViewModel

class MainActivity : AppCompatActivity() {

    private var homeViewModel: HomeViewModel? = null
    private lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        text = findViewById(R.id.test)
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        testHttp()
    }

    private fun testHttp() {



        homeViewModel?.getRecommondJokes(1)
                ?.subscribe(object : ApiObserver<BaseResponse<List<JokeBean>>>(lifecycle) {
                    override fun onSuccess(resp: BaseResponse<List<JokeBean>>) {
                        Log.e("TAG", "返回数据......" + resp?.data)
                        text.text = resp?.data.toString()
                    }

                    override fun onFail(throwable: Throwable?) {
                        super.onFail(throwable)
                        Log.e("TAG", "返回数据......" + throwable?.message)
                    }
                })
        RetrofitManager.getService(ApiService::class.java)
                .getNovelList(1, 1)
                .subscribe(object : ApiObserver<BaseResponse<List<JokeBean>>>(lifecycle) {
                    override fun onSuccess(resp: BaseResponse<List<JokeBean>>) {
                        Log.e("TAG", "返回数据......" + resp?.data)
                        text.text = resp?.data.toString()
                    }

                    override fun onFail(throwable: Throwable?) {
                        super.onFail(throwable)

                    }
                })

    }
}
