package com.example.view.viewpager.noflash;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.ViewGroup;

import com.example.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fishyu on 2017/12/18.
 */

public class ViewPagerNoFlashActivity extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ViewPagerNoFlash viewPager = new ViewPagerNoFlash(this);
        viewPager.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(viewPager);

        PagerAdapterNoFlash adapterNoFlash = new PagerAdapterNoFlash(getSupportFragmentManager());
        List list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        adapterNoFlash.setList(list);

        viewPager.setAdapter(adapterNoFlash);
        adapterNoFlash.notifyDataSetChanged();
    }
}
