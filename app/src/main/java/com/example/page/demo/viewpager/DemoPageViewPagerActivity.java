package com.example.page.demo.viewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.example.fishyu.fishdemo.R;
import com.example.page.demo.DemoPage;
import com.example.page.demo.PlayerPage;
import com.example.yizhibopage.page.IPage;
import com.example.yizhibopage.page.impletment.SimplePageParent;
import com.example.yizhibopage.page.plugin.ViewPagerAdapterPagePlugin;

public class DemoPageViewPagerActivity extends FragmentActivity {

    ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init viewpager
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewpager);
        setContentView(mViewPager);

        PagerAdapter adapter = new PagerAdapter(mViewPager, getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    /**
     * Test
     */
    public class PagerAdapter extends ViewPagerAdapterPagePlugin {

        public PagerAdapter(ViewPager viewPager, FragmentManager fm) {
            super(viewPager, fm);
        }


        @Override
        public IPage getPage(int i) {
            DemoPage page = new DemoPage(DemoPageViewPagerActivity.this);
            Bundle bundle = new Bundle();
            bundle.putString("url", PlayerPage.URLS[i]);
            page.setArgument(bundle);
            page.install(null);
            return page;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }


}
