package com.example.page.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.fishyu.fishdemo.R;
import com.example.page.demo.activity.DemoPageActivity;
import com.example.page.demo.fragment.DemoPageFragmentActivity;
import com.example.page.demo.listview.DemoPageListViewActivity;
import com.example.page.demo.viewpager.DemoPageViewPagerActivity;

public class EntranceActivity extends Activity implements OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrance_act);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_activity_page:

                startActivity(new Intent(this, DemoPageActivity.class));
                break;

            case R.id.button_fragment_page:
                startActivity(new Intent(this, DemoPageFragmentActivity.class));

                break;

            case R.id.button_viewpager_page:
                startActivity(new Intent(this, DemoPageViewPagerActivity.class));

                break;

            case R.id.button_listview_page:
                startActivity(new Intent(this, DemoPageListViewActivity.class));

                break;
        }
    }
}
