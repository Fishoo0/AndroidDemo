package com.example.yizhibopage.page.plugin;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yizhibopage.page.IPage;
import com.example.yizhibopage.page.container.PageFragmentV4;

public abstract class ViewPagerAdapterPagePlugin extends FragmentPagerAdapter {

    private ViewPager mViewPager;
    private Fragment mCurrentFragment;
    private int mCurrentPosition;

    public ViewPagerAdapterPagePlugin(ViewPager viewPager, FragmentManager fm) {
        super(fm);
        mViewPager = viewPager;
    }

    @Override
    public Fragment getItem(int i) {
        return new PageViewPagerFragment(i, this, getPage(i));
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        Fragment fragment = (Fragment) object;
        mCurrentFragment = fragment;
        mCurrentPosition = position;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PageViewPagerFragment fragment = (PageViewPagerFragment) super.instantiateItem(container, position);
        mViewPager.addOnPageChangeListener(fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mViewPager.removeOnPageChangeListener((ViewPager.OnPageChangeListener) object);
    }

    /**
     * The fragment is the current fragment or not.
     *
     * @return
     */
    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    /**
     * Getting its viewpager
     *
     * @return
     */
    public ViewPager getViewPager() {
        return mViewPager;
    }


    /**
     * Getting page of this viewpager.
     *
     * @param i
     * @return
     */
    public abstract IPage getPage(int i);


    /**
     * Internal managing Fragment
     */
    public static class PageViewPagerFragment extends Fragment implements ViewPager.OnPageChangeListener {

        private ViewPagerAdapterPagePlugin mAdapter;
        private IPage mPage;
        private int mPosition;

        public PageViewPagerFragment(int position, ViewPagerAdapterPagePlugin adapter, IPage page) {
            super();
            mPosition = position;
            mAdapter = adapter;
            mPage = page;
        }

        FragmentPagePlugin mPagePlugin;

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            mPagePlugin = new FragmentPagePlugin(this, mPage);
            mPagePlugin.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return mPagePlugin.onCreateView(container);
        }

        @Override
        public void onStart() {
            super.onStart();
            mPagePlugin.onStart();
        }

        @Override
        public void onStop() {
            super.onStop();
            mPagePlugin.onStop();
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            mPagePlugin.onDestroyView();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mPagePlugin.onDestroy();
        }

        @Override
        public void onResume() {
            super.onResume();
            if (isCurrentFragment()) {
                pageOnResume();
            }
        }

        @Override
        public void onPause() {
            super.onPause();
            pageOnPause();
        }


        /**
         * Is the current fragment.
         *
         * @return
         */
        public boolean isCurrentFragment() {
            return mPosition == mAdapter.getCurrentPosition();
        }


        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

        }

        @Override
        public void onPageScrollStateChanged(int i) {
            if (i == ViewPager.SCROLL_STATE_IDLE) {
                mAdapter.getViewPager().post(new Runnable() {
                    @Override
                    public void run() {
                        checkIsCurrent();
                    }
                });
            }
        }

        private void pageOnResume() {
            mPage.onResume();
        }

        private void pageOnPause() {
            mPage.onPause();
        }

        private void checkIsCurrent() {
            if (isCurrentFragment()) {
                pageOnResume();
            } else {
                pageOnPause();
            }
        }
    }


}
