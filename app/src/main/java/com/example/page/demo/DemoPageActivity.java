package com.example.page.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import com.example.fishyu.fishdemo.R;
import com.example.yizhibopage.page.IPage;
import com.example.yizhibopage.page.impletment.SimplePageParent;
import com.example.yizhibopage.page.plugin.ActivityPagePlugin;

public class DemoPageActivity extends Activity {
    ActivityPagePlugin mPagePlugin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SimplePageParent pageManager = (SimplePageParent) LayoutInflater.from(this).inflate(R.layout.demo_activity, null);
        pageManager.setPolicy(SimplePageParent.mDefaultPolicy);
        pageManager.getPageManager().install(this, null, IPage.NONE);

        mPagePlugin = new ActivityPagePlugin(this, pageManager);
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

}
