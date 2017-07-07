package com.github.liangtg.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.squareup.otto.Bus;

import java.util.ArrayList;

/**
 * Created by dbx on 15/12/2.
 */
public class BaseActivity extends AppCompatActivity {

    private static int count = 0;
    protected final String TAG = getClass().getSimpleName();
    protected Toast toast;
    int aid;
    private ArrayList<Pair<Bus, Object>> manageBus = new ArrayList<>();

    public BaseActivity() {
        aid = count++;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logLife(1);
    }

    protected void initAppBar() {
        Toolbar appBar = (Toolbar) findViewById(R.id.toolbar);
        if (null != appBar) {
            setSupportActionBar(appBar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(displayUp());
            appBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTitleBackPressed();
                }
            });
            if (!TextUtils.isEmpty(getTitle())) appBar.setTitle(getTitle());
        }
    }

    protected void onTitleBackPressed() {
        onBackPressed();
    }

    protected boolean displayUp() {
        return true;
    }

    protected void gotoActivity(Class<? extends Activity> cls) {
        Intent i = new Intent(this, cls);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }

    public void showToast(String msg) {
        if (!isFinishing()) {
            if (null == toast) {
                toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            }
            toast.setText(msg);
            toast.show();
        }
    }

    public void showToast(int resId) {
        showToast(getString(resId));
    }

    public void startManageBus(Bus bus, Object register) {
        bus.register(register);
        manageBus.add(Pair.create(bus, register));
    }

    public void stopManageBus(Bus bus, Object register) {
        bus.unregister(register);
        manageBus.remove(Pair.create(bus, register));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        logLife(1);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        logLife(1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        logLife(2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        logLife(3);
    }

    @Override
    protected void onPause() {
        super.onPause();
        logLife(3);
    }

    @Override
    protected void onStop() {
        super.onStop();
        logLife(2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logLife(1);
        if (null != toast) toast.cancel();
        if (!manageBus.isEmpty()) {
            for (Pair<Bus, Object> pair : manageBus) {
                pair.first.unregister(pair.second);
            }
            manageBus.clear();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        logLife(1);
    }

    public BaseActivity getBaseActivity() {
        return this;
    }

    private void logLife(int p) {
        StackTraceElement stack = Thread.currentThread().getStackTrace()[3];
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            sb.append(i < p ? "|" : " ");
        }
        Log.d("alife", sb.toString() + String.format("%02d %s\t%s", aid, TAG, stack.getMethodName()));
    }

}
