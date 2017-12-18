package com.example.view.viewpager.noflash;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by fishyu on 2017/12/18.
 * <p>
 * Modified from {@link android.support.v4.app.FragmentStatePagerAdapter} and removing state management.
 */
public abstract class FragmentPagerAdapter extends PagerAdapter {

    private static final boolean DEBUG = false;
    private static final String TAG = FragmentPagerAdapter.class.getSimpleName();

    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;

    public FragmentPagerAdapter(FragmentManager fm) {
        mFragmentManager = fm;
    }

    @Override
    public void startUpdate(ViewGroup container) {
        if (container.getId() == View.NO_ID) {
            throw new IllegalStateException("ViewPager with adapter " + this
                    + " requires a view id");
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitNowAllowingStateLoss();
            mCurTransaction = null;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment) object).getView() == view;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        Fragment fragment = getItem(position);
        if (DEBUG) Log.v(TAG, "Adding item #" + position + ": f=" + fragment);

        mCurTransaction.add(container.getId(), fragment);

        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment) object;
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        if (DEBUG) Log.v(TAG, "Removing item #" + position + ": f=" + object
                + " v=" + ((Fragment) object).getView());

        mCurTransaction.remove(fragment);
    }


    /**
     * Return the Fragment associated with a specified position.
     */
    public abstract Fragment getItem(int position);

}
