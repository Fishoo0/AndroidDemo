package com.example.yizhibopage.page.plugin;

import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;

import com.example.yizhibopage.page.IPage;


/**
 * Created by fishyu on 2018/8/10.
 */

class SimplePagePlugin {

    protected IPage mPage;

    public SimplePagePlugin(IPage page) {
        mPage = page;
    }

    public void onCreate(Bundle bundle) {
        mPage.onCreate(bundle);
    }

    public View onCreateView(ViewParent viewParent) {
        return mPage.onCreateView(viewParent);
    }

    public void onStart() {
        mPage.onStart();
    }

    public void onResume() {
        mPage.onResume();
    }

    public void onPause() {
        mPage.onPause();
    }

    public void onStop() {
        mPage.onStop();
    }

    public void onDestroyView() {
        mPage.onDestroyView();
    }

    public void onDestroy() {
        mPage.onDestroy();
    }

    public IPage getPage() {
        return mPage;
    }
}
