package com.example.view.viewpager.noflash;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
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

    /**
     * Swipe to left
     */
    public static final boolean LEFT = true;

    /**
     * Swipe to right
     */
    public static final boolean RIGHT = false;

    private int mCurrentPosition = 0;
    private boolean mDirection = LEFT;

    private List mList = new ArrayList();
    private ViewPager mViewPager;

    public PagerAdapterNoFlash(FragmentManager fm, ViewPager viewPager) {
        super(fm);
        mViewPager = viewPager;
    }

    public void setList(List list) {
        mList.clear();
        if (list != null) {
            mList.addAll(list);
        }
    }


    /**
     * Try to remove a item, and refresh view
     *
     * @param position
     */
    public void remove(final int position) {
        Log.v(TAG, "remove -> " + position);
        if (position < 0 || position >= getCount()) {
            Log.e(TAG, "\t invalid position -> " + position);
            return;
        }

        if (getLastSwipeDirection() == RIGHT && position - 1 >= 0) {
            mViewPager.setCurrentItem(position - 1, false);
        }
        mList.remove(position);
        notifyDataSetChanged();
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



    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (position != mCurrentPosition) {
            mDirection = position - mCurrentPosition > 0;
            mCurrentPosition = position;
        }
    }

    /**
     *
     * Getting last swipe direction
     *
     * @return {@link #LEFT} or {@link #RIGHT}
     */
    public boolean getLastSwipeDirection() {
        return mDirection;
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
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewPagerNoFlashActivity activity = (ViewPagerNoFlashActivity) v.getContext();
                    activity.mAdapter.remove(mPosition);

                }
            });
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
