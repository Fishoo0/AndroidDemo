package com.example.page.demo;

import android.app.Activity;
import android.os.Bundle;

import com.example.yizhibopage.page.impletment.SimplePageParent;
import com.example.yizhibopage.page.plugin.ActivityPagePlugin;

public class DemoActivityPagePlugin extends ActivityPagePlugin {

    public DemoActivityPagePlugin(Activity activity, SimplePageParent page) {
        super(activity, page);
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }
}
