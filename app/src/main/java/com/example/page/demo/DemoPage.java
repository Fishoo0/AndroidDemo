package com.example.page.demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.example.fishyu.fishdemo.R;
import com.example.yizhibopage.page.impletment.SimplePageParent;


/**
 * Demo page for all:
 * 1, Activity
 * 2, Fragment
 * 3, ViewPager
 * 4, ListView
 */
public class DemoPage extends SimplePageParent {

    public DemoPage(@NonNull Context context) {
        super(context);
    }

    public DemoPage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.demo_page_layout, this);
    }

}
