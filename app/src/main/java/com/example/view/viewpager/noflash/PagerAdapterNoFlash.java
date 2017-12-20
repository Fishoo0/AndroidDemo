package com.example.view.viewpager.noflash;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fishyu on 2017/12/18.
 */

public class PagerAdapterNoFlash extends SimpleFragmentPagerAdapter {

    static final String TAG = PagerAdapterNoFlash.class.getSimpleName();

    private List mList = new ArrayList();

    public PagerAdapterNoFlash(FragmentManager fm) {
        super(fm);
    }

    public void setList(List list) {
        mList.clear();
        if (list != null) {
            mList.addAll(list);
        }
    }

    @Override
    public Fragment getItem(int position) {
        Log.v(TAG, "getItem -> " + position);
        Object o = mList.get(position);
        if (o instanceof Integer) {
            return new MyFragment(position, null);
        } else if (o instanceof DataWrapper) {
            return new MyFragment(position, (DataWrapper) o);
        } else {
            throw new RuntimeException("Unsupported data in List in position -> " + position + " o -> " + o);
        }
    }

    @Override
    public List getList() {
        return mList;
    }


    @Override
    public int getCount() {
        return mList.size();
    }


    public static class MyFragment extends BaseFragment implements SimpleFragmentPagerAdapter.ISimplePagerFragment {

        public int mPosition;
        public DataWrapper mDataWrapper;

        public MyFragment() {
            super();
        }


        public MyFragment(int position, DataWrapper dataWrapper) {
            mPosition = position;
            mDataWrapper = dataWrapper;

            TAG = this.toString();
        }


        private TextView mTextView;

        private void updateView() {
            if (mTextView != null) {
                mTextView.setText(this.toString());
            }
        }


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);

            TextView textView = new TextView(getContext());
            textView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
            textView.setGravity(Gravity.CENTER);
            mTextView = textView;
            updateView();
            return textView;
        }

        @Override
        public String toString() {
            return MyFragment.class.getSimpleName() + " mDataWrapper: " + mDataWrapper + " mPosition: " + mPosition;
        }

        @Override
        public Object getDataFromAdapter() {
            return mDataWrapper;
        }

        @Override
        public void updatePositionInAdapter(int newPosition) {
            Log.v(TAG, "updatePositionInAdapter -> " + newPosition);

            TAG = this.toString();

            mPosition = newPosition;
            updateView();
        }
    }


    public static class DataWrapper {
        public String mDes;

        public static DataWrapper newInstance(String des) {
            DataWrapper dataWrapper = new DataWrapper();
            dataWrapper.mDes = "Description : " + des;
            return dataWrapper;
        }

        @Override
        public String toString() {
            return "mDes: " + mDes;
        }
    }


}
