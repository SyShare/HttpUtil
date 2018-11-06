package com.youlu.http.api;

import com.youlu.http.bean.BaseResponse;
import com.youlu.http.models.JokeBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @Description:
 * @Data：2018/7/16-15:58
 * @author: SyShare
 */
public interface ApiService {


    /**
     * 热门段子
     *
     * @param type
     * @param page
     * @return
     */
    @GET("/satinApi")
    Observable<BaseResponse<List<JokeBean>>> getNovelList(@Query("type") int type, @Query("page") int page);
}
