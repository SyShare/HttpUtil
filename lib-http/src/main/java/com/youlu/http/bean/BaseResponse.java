package com.youlu.http.bean;

import com.youlu.http.util.ApiCodeUtil;

/**
 * @Data：2018/7/16-16:09
 * @author: SyShare
 */
public class BaseResponse<T> {

    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int state) {
        this.code = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccessful() {
        return code == ApiCodeUtil.INSTANCE.getSUCCESS_CODE() && data != null;
    }

}
