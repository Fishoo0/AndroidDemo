package com.example.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Swipe to adjust view size.
 * <p>
 * Must use in xml.
 */
public class SwipeAdjustSizeLayout extends FrameLayout implements GestureDetector.OnGestureListener {

    public static final String TAG = SwipeAdjustSizeLayout.class.getSimpleName();

    public static final String VIEW_TAG_LEFT_SWIPE = "swipeLeft";
    public static final String VIEW_TAG_RIGHT_SWIPE = "swipeRight";
    public static final String VIEW_TAG_TOP_SWIPE = "swipeTop";
    public static final String VIEW_TAG_BOTTOM_SWIPE = "swipeBottom";
    public static final String VIEW_TAG_SWIPE_PARENT = "Parent";

    private final Map<String, AViewAdjuster> mSupportSwipe = new HashMap();

    TouchManager mTouchManager;

    View mSwipeIcon[];
    private ViewGroup mSwipeLayouts[];
    GestureDetector mGestureDetector;


    /**
     * Adjuster.
     */
    abstract static class AViewAdjuster {

        ViewGroup resizeView;
        int originalWidth;
        int originalHeight;

        float downX;
        float downY;

        Handler handler;


        private class InternalHandler extends Handler {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                onAdjust((int) downX, (int) downY, msg.arg1, msg.arg2);
            }
        }

        AViewAdjuster() {
            handler = new InternalHandler();
        }

        public void init(ViewGroup view, MotionEvent downEvent) {
            resizeView = view;
            downX = downEvent.getX();
            downY = downEvent.getY();
            originalWidth = view.getLayoutParams().width;
            originalHeight = view.getLayoutParams().height;
        }

        /**
         * Adjust the view.
         *
         * @param event
         */
        public void adjust(MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            handler.removeCallbacksAndMessages(null);
            handler.sendMessage(handler.obtainMessage(0, x, y));
        }


        private void adjustMove(MotionEvent event) {

        }


        private void adjustFling(MotionEvent event) {

        }

        private void adjustClick(MotionEvent event) {

        }


        /**
         * Implement your own logic to view adjusting.
         *
         * @param downX
         * @param downY
         */
        abstract void onAdjust(int downX, int downY, int x, int y);

    }


    static class LeftSwipeAdjuster extends AViewAdjuster {

        @Override
        public void init(ViewGroup view, MotionEvent downEvent) {
            super.init(view, downEvent);
        }

        @Override
        void onAdjust(int downX, int downY, int x, int y) {
            int delta = x - downX;

            // resize
            ViewGroup.LayoutParams params = resizeView.getLayoutParams();
            params.width = originalWidth + delta;
            resizeView.setLayoutParams(params);
        }
    }




    public SwipeAdjustSizeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        mGestureDetector = new GestureDetector(getContext(), this);


        mSupportSwipe.put(VIEW_TAG_LEFT_SWIPE, new LeftSwipeAdjuster());
//        mSupportSwipe.put(VIEW_TAG_RIGHT_SWIPE, new RightSwipeAdjuster());
//        mSupportSwipe.put(VIEW_TAG_TOP_SWIPE, new TopSwipeAdjuster());
//        mSupportSwipe.put(VIEW_TAG_BOTTOM_SWIPE, new BottomSwipeAdjuster());
    }

    /**
     * Setting the views.
     *
     * @param swipeIcons
     * @param swipeControlLayouts
     */
    private void setViews(View[] swipeIcons, ViewGroup[] swipeControlLayouts) {
        Log.v(TAG, "setViews swipeIcons -> " + swipeIcons + ", swipeControlLayouts -> " + swipeControlLayouts);
        if (swipeIcons == null || swipeControlLayouts == null || swipeIcons.length != swipeControlLayouts.length) {
            throw new IllegalArgumentException("Invalid params, swipeIcons -> " + swipeIcons + ", swipeControlLayouts -> " + swipeControlLayouts);
        }
        mSwipeIcon = swipeIcons;
        mSwipeLayouts = swipeControlLayouts;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        List<View> views = new ArrayList<>();
        List<ViewParent> parents = new ArrayList<>();
        for (String key : mSupportSwipe.keySet()) {
            View view = findViewWithTag(key);
            if (view instanceof SwipeButton) {
                ((SwipeButton) view).mAdjustLayout = this;
                views.add(view);
                ViewParent viewParent = findViewWithTag(key + VIEW_TAG_SWIPE_PARENT);
                if (viewParent == null) {
                    throw new RuntimeException("You must specify the parent of view[" + key + "]");
                }
                parents.add(viewParent);
            } else if (view != null) {
                throw new RuntimeException("The swipe button must be an instance of SwipeButton.");
            }
        }
        // set views ...
        if (views.size() > 0) {
            setViews(views.toArray(new View[views.size()]), parents.toArray(new ViewGroup[parents.size()]));
        }
    }


    /**
     * Touch Event Data.
     */
    public class TouchManager {
        float x;
        float y;

        View swipeIcon;
        ViewGroup swipeIconParent;

        AViewAdjuster viewAdjuster;

        public TouchManager() {
        }

        @NonNull
        @Override
        public String toString() {
            return "x:" + x + ",y:" + y;
        }

        /**
         * Active this event.
         *
         * @param downEvent
         * @return
         */
        protected boolean active(MotionEvent downEvent) {
            x = downEvent.getX();
            y = downEvent.getY();

            Rect rect = new Rect();
            if (mSwipeIcon == null) {
                return false;
            }
            for (int i = 0; i < mSwipeIcon.length; i++) {
                View view = mSwipeIcon[i];

                getGlobalVisibleRect(rect);
                int dy = rect.top;
                view.getGlobalVisibleRect(rect);
                rect.offset(0, -dy);

                if (rect.contains((int) x, (int) y)) {
                    swipeIcon = view;
                    swipeIconParent = mSwipeLayouts[i];
                    viewAdjuster = mSupportSwipe.get(view.getTag());
                    viewAdjuster.init(swipeIconParent, downEvent);
                    return true;
                }
            }
            return false;
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v(TAG, "onTouchEvent -> " + event);
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                Log.v(TAG, "ACTION_DOWN");
//
//
//            case MotionEvent.ACTION_MOVE:
//                Log.v(TAG, "ACTION_MOVE");
//                if (mTouchManager != null) {
//                    mTouchManager.adjust(event);
//                    return true;
//                }
//                break;
//
//            case MotionEvent.ACTION_UP:
//                Log.v(TAG, "ACTION_UP");
//                break;
//
//            case MotionEvent.ACTION_CANCEL:
//                Log.v(TAG, "ACTION_CANCEL");
//                break;
//
//            case MotionEvent.ACTION_OUTSIDE:
//                Log.v(TAG, "ACTION_OUTSIDE");
//                break;
//        }

        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        mTouchManager = new TouchManager();
        if (mTouchManager.active(e)) {
            return true;
        } else {
            mTouchManager = null;
            return false;
        }
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        mTouchManager.viewAdjuster.adjustClick(e);
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        mTouchManager.viewAdjuster.adjustMove(e2);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        mTouchManager.viewAdjuster.adjustFling(e2);
        return true;
    }


    public static class SwipeButton extends ImageView {

        SwipeAdjustSizeLayout mAdjustLayout;

        public SwipeButton(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public SwipeButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }


        @Override
        public boolean onTouchEvent(MotionEvent event) {
            Log.v(TAG, "onTouchEvent -> " + event);
            return super.onTouchEvent(event);
        }
    }
}
