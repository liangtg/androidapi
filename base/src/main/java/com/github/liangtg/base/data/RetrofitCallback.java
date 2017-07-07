package com.github.liangtg.base.data;

import android.util.Log;

import com.google.gson.Gson;
import com.squareup.otto.Bus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liangtg on 17-3-23.
 */

public class RetrofitCallback<T extends BaseResponse> extends AbstractDataRequest implements Callback<T> {
    private static final String EMPTY_JSON = "{}";
    private Class<T> responseType;

    public RetrofitCallback(Bus bus, Class<T> cls) {
        super(bus);
        this.responseType = cls;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (null != response.body()) {
            T result = response.body();
            setResult(result);
        } else {
            onFailure(call, new RuntimeException());
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        Log.d("net", "failure", throwable);
        T target = new Gson().fromJson(EMPTY_JSON, responseType);
        target.defaultError(throwable);
        setResult(target);
    }
}
