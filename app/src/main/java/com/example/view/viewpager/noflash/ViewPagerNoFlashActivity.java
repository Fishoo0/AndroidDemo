package com.example.view.viewpager.noflash;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.base.BaseActivity;
import com.example.fishyu.fishdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fishyu on 2017/12/18.
 */

public class ViewPagerNoFlashActivity extends BaseActivity implements View.OnClickListener {

    ViewPager mViewPager;
    PagerAdapterNoFlash mAdapter;

    List mList = new ArrayList();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noflash_activity);

        mViewPager = findViewById(R.id.viewpager);

        mAdapter = new PagerAdapterNoFlash(getSupportFragmentManager());
        mAdapter.setList(null);

        mViewPager.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


    private PagerAdapterNoFlash.DataWrapper mDataInit = PagerAdapterNoFlash.DataWrapper.newInstance("INIT");

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.append_left:
                for (int i = 0; i < 5; i++) {
                    PagerAdapterNoFlash.DataWrapper data = PagerAdapterNoFlash.DataWrapper.newInstance("LEFT" + (4 - i));
                    mList.add(0, data);
                }
                mAdapter.setList(mList);
                mAdapter.notifyDataSetChanged();
                break;

            case R.id.append_right:
                for (int i = 0; i < 5; i++) {
                    PagerAdapterNoFlash.DataWrapper data = PagerAdapterNoFlash.DataWrapper.newInstance("RIGHT" + i);
                    mList.add(data);
                }
                mAdapter.setList(mList);
                mAdapter.notifyDataSetChanged();
                break;

            case R.id.clear:
                mList.clear();
                mList.add(mDataInit);
                mAdapter.setList(mList);
                mAdapter.notifyDataSetChanged();
                break;
        }

    }
}
