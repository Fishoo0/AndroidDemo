package com.example.view.viewpager.noflash;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.base.BaseFragment;

import java.util.List;

/**
 * Created by fishyu on 2017/12/18.
 */

public class PagerAdapterNoFlash extends FragmentStatePagerAdapter {

    static final String TAG = PagerAdapterNoFlash.class.getSimpleName();

    private List mList;

    public PagerAdapterNoFlash(FragmentManager fm) {
        super(fm);
    }

    public void setList(List list) {
        if (mList != null) {
            mList.clear();
        }
        mList = list;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        Log.v(TAG, "notifyDataSetChanged");
    }

    @Override
    public Fragment getItem(int position) {
        Log.v(TAG, "getItem -> " + position);
        Object o = mList.get(position);
        if (o instanceof Integer) {
            return MyFragment.newInstance(position);
        } else {
            throw new RuntimeException("Unsupported data in List in position -> " + position + " o -> " + o);
        }
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.v(TAG, "instantiateItem");
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        Log.v(TAG, "destroyItem");
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }


    public static class MyFragment extends BaseFragment {

        public int mPosition;

        public MyFragment(int position) {
            mPosition = position;
            TAG = TAG + " " + mPosition;
        }


        public static MyFragment newInstance(int position) {
            return new MyFragment(position);
        }


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);

            TextView textView = new TextView(getContext());
            textView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            textView.setText(String.valueOf(mPosition));
            return textView;
        }


    }


}
