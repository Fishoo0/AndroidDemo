package com.example.yizhibopage.page.plugin;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.yizhibopage.page.IPage;

/**
 * Created by fishyu on 2018/8/10.
 */

public class ListViewPagePlugin extends FrameLayout {

    String TAG;

    private IPage mPage;
    private ListViewScrollListener mScrollListener;

    public ListViewPagePlugin(@NonNull Context context, IPage page, ListViewScrollListener scrollListener) {
        super(context);
        mScrollListener = scrollListener;
        mPage = page;
        addView((View) mPage);
        TAG = this.toString();
    }

    public IPage getPage() {
        return mPage;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e(TAG, "onAttachedToWindow");
        mPage.onCreate(mPage.getArgument());
        mPage.onCreateView(this);
        mPage.onStart();
    }


    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        Log.v(TAG, "onVisibilityChanged");
        doWhenOnVisibilityChanged(visibility);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        Log.v(TAG, "onWindowVisibilityChanged");
        doWhenOnVisibilityChanged(visibility);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void doWhenOnVisibilityChanged(int visibility) {
        if (!isAttachedToWindow()) {
            return;
        }

        // reset visibility
        if (getVisibility() != VISIBLE || getWindowVisibility() != VISIBLE) {
            visibility = GONE;
        }

        final int status = mPage.getPageManager().getStatus();
        if (visibility == VISIBLE) {
            if (mScrollListener.getCurrentVisibleItem() == this) {
                if (status != IPage.ON_RESUME) {  // protection
                    mPage.onResume();
                }
            } else {
                if (status != IPage.ON_PAUSE) {  // protection
                    mPage.onPause();
                }
            }
        } else {
            if (status != IPage.ON_PAUSE) { // protection
                mPage.onPause();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.w(TAG, "onDetachedFromWindow");
        mPage.onStop();
        mPage.onDestroyView();
        mPage.onDestroy();
        // uninstall
        mPage.getPageManager().uninstall();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + hashCode() + "@" + mPage;
    }

    public static class ListViewScrollListener implements ListView.OnScrollListener {

        public static String TAG = ListViewScrollListener.class.getClass().getSimpleName();

        private View mCurrentVisibleItem;
        private ListView mListView;

        public ListViewScrollListener(ListView listView) {
            mListView = listView;
        }

        @Override
        public void onScrollStateChanged(final AbsListView view, int scrollState) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                Log.v(TAG, "onScrollStateChanged -> " + SCROLL_STATE_IDLE);
                mListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        calculateCurrentVisibleItem(view);
                    }
                }, 100);
            } else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                Log.v(TAG, "onScrollStateChanged -> " + SCROLL_STATE_FLING);
                mCurrentVisibleItem = null;
            } else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                Log.v(TAG, "onScrollStateChanged -> " + SCROLL_STATE_TOUCH_SCROLL);
                mCurrentVisibleItem = null;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }


        public void calculateCurrentVisibleItem(AbsListView listView) {
            View biggest = null;
            int biggestHeight = 0;
            Rect rect = new Rect();

            for (int i = 0; i < listView.getChildCount(); i++) {
                View view = listView.getChildAt(i);
                view.getGlobalVisibleRect(rect);
                if (rect.height() > biggestHeight) {
                    biggest = view;
                    biggestHeight = rect.height();
                }
            }

            mCurrentVisibleItem = biggest;

            for (int i = 0; i < listView.getChildCount(); i++) {
                View view = listView.getChildAt(i);
                if (view instanceof ListViewPagePlugin) {
                    ListViewPagePlugin pagePlugin = (ListViewPagePlugin) view;
                    configureView(pagePlugin, view == biggest);
                }
            }

        }


        /**
         * Getting current visible item.
         *
         * @return
         */
        public View getCurrentVisibleItem() {
            Log.v(TAG, "getCurrentVisibleItem");
            return mCurrentVisibleItem;
        }

        /**
         * What to do with the view ???
         *
         * @param plugin
         * @param biggest
         */
        protected void configureView(ListViewPagePlugin plugin, boolean biggest) {
            Log.v(TAG, "configureView");
            plugin.doWhenOnVisibilityChanged(biggest ? View.VISIBLE : View.INVISIBLE);
        }


        public void notifyDataSetChanged() {
            mListView.post(new Runnable() {
                @Override
                public void run() {
                    calculateCurrentVisibleItem(mListView);
                }
            });
        }


    }


}
