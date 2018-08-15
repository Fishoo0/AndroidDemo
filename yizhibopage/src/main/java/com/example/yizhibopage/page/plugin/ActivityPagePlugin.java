package com.example.yizhibopage.page.plugin;

import android.app.Activity;
import android.os.Bundle;

import com.example.yizhibopage.page.IPage;

public class ActivityPagePlugin extends SimplePagePlugin {

    private Activity mActivity;

    public ActivityPagePlugin(Activity activity, IPage page) {
        super(page);
        mActivity = activity;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mActivity.setContentView(mPage.onCreateView(null));
    }


    @Override
    public void onDestroy() {
        mPage.onDestroyView();
        super.onDestroy();
    }
}
