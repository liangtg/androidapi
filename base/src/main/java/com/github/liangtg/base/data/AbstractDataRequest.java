package com.github.liangtg.base.data;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

public abstract class AbstractDataRequest<T> implements DataRequest<T> {
    private RuntimeException exception;
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean cancle = false;
    private Bus bus;
    private T result;

    public AbstractDataRequest(Bus bus) {
        this.bus = bus;
    }

    @Override
    public void cancle() {
        cancle = true;
    }

    public boolean isCancle() {
        return cancle;
    }

    @Override
    public T data() {
        return result;
    }

    protected void setResult(T result) {
        if (isCancle()) return;
        exception = new RuntimeException("set result when:");
        this.result = result;
        handler.post(new UIRunnable());
    }

    private class UIRunnable implements Runnable {
        @Override
        public void run() {
            if (!isCancle()) {
                try {
                    bus.post(result);
                } catch (Exception e) {
                    exception.initCause(e);
                    throw exception;
                }
            }
        }
    }


}
