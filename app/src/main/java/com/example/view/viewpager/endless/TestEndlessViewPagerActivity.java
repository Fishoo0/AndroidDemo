package com.example.view.viewpager.endless;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fishyu on 2017/12/18.
 */

public class TestEndlessViewPagerActivity extends FragmentActivity {


    private EndlessViewPager mViewPager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EndlessViewPager viewPager = new EndlessViewPager(this);
        viewPager.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(viewPager);

        mViewPager = viewPager;

        final MyAdapter adapter = new MyAdapter(getSupportFragmentManager(), mViewPager);
        mViewPager.setAdapter(adapter);

        adapter.mList.clear();
        adapter.mList.add("1");
        adapter.mList.add("2");
        adapter.mList.add("3");
        adapter.mList.add("4");
        adapter.mList.add("5");
        adapter.mList.add("6");
        adapter.notifyDataSetChanged();
        mViewPager.setActualCurrentItem(4);

        mViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.mList.clear();
                adapter.mList.add("1");
                adapter.mList.add("A");
                adapter.mList.add("B");
                adapter.notifyDataSetChanged();

                int currentItem = mViewPager.getActualCurrentItem();

                mViewPager.setActualCurrentItem(currentItem);
            }
        });
    }


    @SuppressLint("ValidFragment")
    public static class MyFragment extends Fragment implements EndlessViewPager.IEndlessFragmentCallback {

        String TAG1;
        String TAG;

        private Object mObject;

        private EndlessViewPager mViewPager;

        public MyFragment(Object object, int position, EndlessViewPager viewPager) {
            mObject = object;
            TAG1 = MyFragment.class.getSimpleName() + " position:" + position + " mObject -> " + mObject;
            TAG = toString();
            mViewPager = viewPager;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            Log.v(TAG, "onCreate");
            super.onCreate(savedInstanceState);
        }

        @Override
        public void onPause() {
            Log.v(TAG, "onPause");
            super.onPause();
        }

        @Override
        public void onResume() {
            Log.v(TAG, "onResume");
            super.onResume();
        }

        @Override
        public void onDestroyView() {
            Log.v(TAG, "onDestroyView");
            super.onDestroyView();
        }

        @Override
        public void onDestroy() {
            Log.v(TAG, "onDestroy");
            super.onDestroy();
        }

        private TextView mTextView;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            Log.v(TAG, "onCreateView");
            final TextView textView = new TextView(getContext());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            textView.setLayoutParams(params);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            textView.setGravity(Gravity.CENTER);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getContext(), "TextView -> " + textView.getText(), Toast.LENGTH_SHORT).show();

                    int currentItem = mViewPager.getActualCurrentItem();
                    Log.e(TAG, "currentItem -> " + currentItem);

                    //using actual-position
//                    mViewPager.setActualCurrentItem((mViewPager.getActualCurrentItem() + 1) % mViewPager.getEndlessAdapter().getCountActually());

                    //using endless-position
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);

                }
            });
            textView.append(String.valueOf(mPosition) + "\n");
            mTextView = textView;
            updateView();
            return textView;
        }

        public void updateView() {
            mTextView.append(mObject.getClass().getSimpleName() + "   " + mObject + "\n");
        }

        @Override
        public String toString() {
            return TAG1 + "   |" + super.toString();
        }


        private int mPosition;

        @Override
        public void setPosition(int position) {
            mPosition = position;
        }

        @Override
        public int getPosition() {
            return mPosition;
        }

        @Override
        public void onDataChanged(Object data) {
            Log.v(TAG, "onDataChanged -> " + data);
            mObject = data;
            updateView();
        }
    }


    public static class MyAdapter extends EndlessViewPager.EndlessPagerAdapter {

        static final String TAG = MyAdapter.class.getSimpleName();

        public List<Object> mList = new ArrayList<>();

        public MyAdapter(FragmentManager fm, EndlessViewPager viewPager) {
            super(fm, viewPager);
        }

        @Override
        public int getCountActually() {
            return mList.size();
        }

        @Override
        public Object getData(int actualPosition) {
            if (actualPosition == -1) {
                return "EMPTY";
            }
            return mList.get(actualPosition);
        }

        @Override
        public Fragment getItem(final int p) {
            Log.v(TAG, "getItem -> " + p);
            int position = convertPositionToActual(p);
            Fragment fragment = new MyFragment(getData(position), p, mViewPager);
            return fragment;
        }

    }
}
