package com.github.liangtg.base.data;

import android.util.Log;

import com.github.liangtg.base.util.Logger;
import com.google.gson.Gson;
import com.squareup.otto.Bus;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GsonCallback<T extends BaseResponse> extends AbstractDataRequest implements Callback {
    private static final String EMPTY_JSON = "{}";
    private Class<T> responseType;

    public GsonCallback(Bus bus, Class<T> responseType) {
        super(bus);
        this.responseType = responseType;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        T target = new Gson().fromJson(EMPTY_JSON, responseType);
        target.defaultError(e);
        setResult(target);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        try {
            T result = processResponse(response);
            setResult(result);
        } catch (Exception e) {
            BaseResponse target = new Gson().fromJson(EMPTY_JSON, this.responseType);
            Logger.e(e, "");
            setResult(target.error(-1));
        }
    }

    protected T processResponse(Response response) throws IOException {
        T target;
        String body = response.body().string();
        Log.d("response", body);
        target = new Gson().fromJson(body, this.responseType);
        if (!response.isSuccessful()) target.error(response.code());
        return target;
    }

}
