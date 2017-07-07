package com.github.liangtg.base.data;

/**
 * Created by liangtg on 17-3-23.
 */
public interface DataRequest<T> {

    T data();

    void cancle();

    boolean isCancle();
}
