package com.youlu.http

import android.Manifest
import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.permission.PermissionCallback
import com.permission.PermissionHelper
import com.youlu.http.arch.ApiObserver
import com.youlu.http.bean.BaseResponse
import com.youlu.http.models.JokeBean
import kotlinx.android.synthetic.main.activity_main.*
import viewmodel.HomeViewModel

class MainActivity : AppCompatActivity() {

    private var homeViewModel: HomeViewModel? = null
    private lateinit var text: TextView


    companion object {
        const val TAG = "MainActivity_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        text = findViewById(R.id.test)
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)


        val map = mapOf<String, String>(
                Manifest.permission.CHANGE_NETWORK_STATE to "需要网络权限",
                Manifest.permission.WRITE_EXTERNAL_STORAGE to "读写权限",
                Manifest.permission.READ_EXTERNAL_STORAGE to "读写权限"
        )

        permissionHelper.request(map, object : PermissionCallback() {
            override fun onGranted() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    initView()
                }
            }

        })




        testHttp()
    }

    val permissionHelper by lazy {
        PermissionHelper(this)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initView() {

        again_btn.setOnClickListener {
            testHttp()
        }

        NetStatusMonitor.setNetStatusListener(object : NetStatusMonitor.Listener {

            override fun onLost() {
                Log.e(TAG, "onLost")
                AppContext.getInstance().isNetWorkValid = false
            }

            override fun onAvailable() {
                Log.e(TAG, "onAvailable")
                AppContext.getInstance().isNetWorkValid = true
                testHttp()
            }

            override fun onNetStateChange(oldState: Int, newState: Int) {
                Log.e(TAG, "onNetStateChange-------$newState")
            }

        })
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

    }
}
