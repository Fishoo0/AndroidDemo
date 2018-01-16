package com.example.view.viewpager.noflash;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.example.base.BaseActivity;
import com.example.fishyu.fishdemo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by fishyu on 2017/12/18.
 */

public class ViewPagerNoFlashActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    ViewPager mViewPager;
    PagerAdapterNoFlash mAdapter;

    List mList = new ArrayList();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noflash_activity);

        mViewPager = findViewById(R.id.viewpager);
        mViewPager.addOnPageChangeListener(this);


        mAdapter = new PagerAdapterNoFlash(getSupportFragmentManager(), mViewPager);
        mAdapter.setList(null);

        mViewPager.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


    private PagerAdapterNoFlash.DataWrapper mDataInit = PagerAdapterNoFlash.DataWrapper.newInstance("INIT");

    private PagerAdapterNoFlash.DataWrapper[] mDataListInit = new PagerAdapterNoFlash.DataWrapper[]{
            PagerAdapterNoFlash.DataWrapper.newInstance("A"),
            PagerAdapterNoFlash.DataWrapper.newInstance("B"),
            PagerAdapterNoFlash.DataWrapper.newInstance("C"),
            PagerAdapterNoFlash.DataWrapper.newInstance("E"),
            PagerAdapterNoFlash.DataWrapper.newInstance("F"),
            PagerAdapterNoFlash.DataWrapper.newInstance("G"),
            PagerAdapterNoFlash.DataWrapper.newInstance("H"),
    };

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
                mList.addAll(Arrays.asList(mDataListInit));
                mAdapter.setList(mList);
                mAdapter.notifyDataSetChanged();
                break;
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.v(TAG, "onPageScrolled position -> " + position + " positionOffset -> " + position + " positionOffsetPixels -> " + positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        Log.v(TAG, "onPageSelected -> " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.v(TAG, "onPageScrollStateChanged -> " + state);
    }
}
