package com.example.page.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import com.example.fishyu.fishdemo.R;
import com.example.page.demo.DemoPage;
import com.example.page.demo.PlayerPage;
import com.example.yizhibopage.page.IPage;
import com.example.yizhibopage.page.impletment.SimplePageParent;
import com.example.yizhibopage.page.plugin.ActivityPagePlugin;

public class DemoPageActivity extends Activity {
    ActivityPagePlugin mPagePlugin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DemoPage page = new DemoPage(this);
        Bundle bundle = new Bundle();
        bundle.putString("url", PlayerPage.URLS[1]);
        page.setArgument(bundle);
        page.install(null);

        mPagePlugin = new ActivityPagePlugin(this, page);
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
