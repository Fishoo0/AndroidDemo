package com.example.weibo.scrollablelistview;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ScrollView;

import com.example.fishyu.fishdemo.R;

/**
 * Created by fishyu on 2018/4/11.
 */

public class ScrollableListViewActivity extends Activity {


    private ScrollView mScrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrollable_listview_act);
        mScrollView = findViewById(R.id.scrollview);
    }


    int i = 0;

    @Override
    public void onBackPressed() {

        if(i++ % 2 == 0 ) {
            mScrollView.setEnabled(true);
        } else {
            //ba ba la
        }

    }
}
