package com.example.weibo.scrollablelistview;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.fishyu.fishdemo.R;
import com.example.weibo.scrollablelistview.view.SupportSwipeWidgetScrollView;

/**
 * Created by fishyu on 2018/4/11.
 */

public class ScrollableListViewActivity extends Activity {


    private SupportSwipeWidgetScrollView mScrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrollable_listview_act);
        mScrollView = findViewById(R.id.scrollview);
    }


    @Override
    public void onBackPressed() {
        mScrollView.setToStatus(-1);
    }
}
