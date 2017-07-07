package com.github.liangtg.base;

import android.app.Application;
import android.content.Context;


/**
 * Created by dbx on 15/12/23.
 */
public class BaseApplication extends Application {
    private static Context context;

    public static Context context() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

}
