package com.github.liangtg.base.data;

/**
 * Created by liangtg on 16-5-11.
 */
public abstract class BaseResponse {
    public int code = -1;
    public String error;

    public BaseResponse defaultError(Throwable e) {
        error = "请检查网络";
        return this;
    }

    public boolean isSuccess() {
        return code < 300 && code > 99;
    }

    public Object getData() {
        return null;
    }

    public BaseResponse error(int code) {
        this.code = code;
        return this;
    }

}
