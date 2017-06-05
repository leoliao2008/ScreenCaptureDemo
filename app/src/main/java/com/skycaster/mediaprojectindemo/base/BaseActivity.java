package com.skycaster.mediaprojectindemo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * Created by 廖华凯 on 2017/6/2.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected String Tag;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tag=getClass().getSimpleName();
        setContentView(setRootView());
        initView();
        initData();
        initListener();
    }

    protected abstract int setRootView();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();

    protected void onClick(int id, View.OnClickListener listener){
        findViewById(id).setOnClickListener(listener);
    }

    protected void showLog(String msg){
        Log.e(Tag,msg);
    }
}
