package com.example.yizhibopage.page.container;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.yizhibopage.page.IPage;
import com.example.yizhibopage.page.plugin.ActivityPagePlugin;

public abstract class PageActivity extends Activity {
    ActivityPagePlugin mPagePlugin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPagePlugin = new ActivityPagePlugin(this, getPage());
        mPagePlugin.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPagePlugin.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPagePlugin.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPagePlugin.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPagePlugin.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPagePlugin.onDestroy();
    }


    public abstract IPage getPage();


}
