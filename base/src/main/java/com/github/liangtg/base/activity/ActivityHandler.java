package com.github.liangtg.base.activity;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by liangtg on 17-3-24.
 */

public class ActivityHandler extends Handler {
    private WeakReference<Callback> reference;

    public ActivityHandler(Callback callback) {
        super();
        reference = new WeakReference<>(callback);
    }

    @Override
    public void handleMessage(Message msg) {
        Callback callback = reference.get();
        if (null != callback) callback.handleMessage(msg);
    }
}
