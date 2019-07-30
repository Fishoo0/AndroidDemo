package com.example.view;

import android.animation.ObjectAnimator;
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
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

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

    AViewAdjuster mTouchManager;

    View mSwipeIcon[];
    private ViewGroup mSwipeLayouts[];
    GestureDetector mGestureDetector;


    /**
     * Adjuster.
     */
    abstract static class AViewAdjuster {
        ViewGroup toAdjustView;
        View swipeView;

        float downX;
        float downY;

        InternalHandler handler;

        /**
         * Scroll Event Handler
         */
        private class InternalHandler extends Handler {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                AViewAdjuster.this.handleMessage(msg);
            }
        }


        protected void handleMessage(Message msg) {

        }


        public AViewAdjuster() {
            handler = new InternalHandler();
        }


        public void init(ViewGroup parent, View swipeView, MotionEvent downEvent) {
            this.swipeView = swipeView;
            this.toAdjustView = parent;
            downX = downEvent.getX();
            downY = downEvent.getY();
        }


        /**
         * Scroll the view
         *
         * @param event1
         * @param event2
         * @param arg1
         * @param arg2
         */
        public boolean scroll(MotionEvent event1, MotionEvent event2, float arg1, float arg2) {
            // do nothing ...
//            int x = (int) event2.getX();
//            int y = (int) event2.getY();
//            handler.removeCallbacksAndMessages(null);
//            handler.sendMessage(handler.obtainMessage(0, x, y));
//            return true;

            return false;
        }


        /**
         * Click
         *
         * @param event
         */
        public abstract boolean click(MotionEvent event);

        /**
         * Fling
         *
         * @param event1
         * @param event2
         * @param arg1
         * @param arg2
         */
        public boolean fling(MotionEvent event1, MotionEvent event2, float arg1, float arg2) {
            if (fling(event1, event2)) {
                return click(event1);
            }
            return false;
        }


        protected boolean fling(MotionEvent event1, MotionEvent event2) {
            return true;
        }

    }


    static class LeftSwipeAdjuster extends AViewAdjuster {
        int lastSize;
        int minSize;

        Rect originalRect;

        private ObjectAnimator animator;


        @Override
        public void init(ViewGroup parent, View swipeView, MotionEvent downEvent) {
            super.init(parent, swipeView, downEvent);
            // init the view
            if (originalRect == null) {
                originalRect = new Rect();
                parent.getGlobalVisibleRect(originalRect);
            }
            lastSize = parent.getLayoutParams().width;
            minSize = swipeView.getWidth();
        }


        @Override
        public boolean click(MotionEvent event) {
            if (animator != null && animator.isRunning()) {
                return false;
            }
            float start = lastSize;
            float end = endSize(event);

            // start animator
            ObjectAnimator animator = ObjectAnimator.ofFloat(this, "size", start, end);
            animator.setDuration(200);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.start();
            this.animator = animator;

            return true;
        }


        protected float endSize(MotionEvent event) {
            if (event.getRawX() > originalRect.centerX()) {
                return minSize;
            } else {
                return originalRect.width();
            }
        }


        /**
         * Set width
         *
         * @param size
         */
        @SuppressWarnings("unused")
        public void setSize(float size) {
            ViewGroup.LayoutParams params = toAdjustView.getLayoutParams();
            params.width = (int) size;
            toAdjustView.setLayoutParams(params);
        }


        @Override
        protected boolean fling(MotionEvent event1, MotionEvent event2) {
            if (event1.getRawX() > originalRect.centerX() && event1.getRawX() > event2.getRawX()) {
                return true;
            } else if (event1.getRawX() < originalRect.centerX() && event1.getRawX() < event2.getRawX()) {
                return true;
            }
            return false;
        }
    }


    static class RightSwipeAdjuster extends LeftSwipeAdjuster {


        protected float endSize(MotionEvent event) {
            if (event.getRawX() < originalRect.centerX()) {
                return minSize;
            } else {
                return originalRect.width();
            }
        }

    }


    static class TopSwipeAdjuster extends LeftSwipeAdjuster {

        @Override
        public void init(ViewGroup parent, View swipeView, MotionEvent downEvent) {
            super.init(parent, swipeView, downEvent);
            lastSize = parent.getLayoutParams().height;
            minSize = swipeView.getHeight();
        }

        @Override
        protected float endSize(MotionEvent event) {
            if (event.getRawY() > originalRect.centerY()) {
                return minSize;
            } else {
                return originalRect.height();
            }
        }

        @Override
        public void setSize(float size) {
            ViewGroup.LayoutParams params = toAdjustView.getLayoutParams();
            params.height = (int) size;
            toAdjustView.setLayoutParams(params);
        }

        @Override
        protected boolean fling(MotionEvent event1, MotionEvent event2) {
            if (event1.getRawY() > originalRect.centerY() && event1.getRawY() > event2.getRawY()) {
                return true;
            } else if (event1.getRawY() < originalRect.centerY() && event1.getRawY() < event2.getRawY()) {
                return true;
            }
            return false;
        }
    }


    static class BottomSwipeAdjuster extends TopSwipeAdjuster {

        @Override
        protected float endSize(MotionEvent event) {
            if (event.getRawY() < originalRect.centerY()) {
                return minSize;
            } else {
                return originalRect.height();
            }
        }
    }


    public SwipeAdjustSizeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    protected void init() {
        mGestureDetector = new GestureDetector(getContext(), this);
        mSupportSwipe.put(VIEW_TAG_LEFT_SWIPE, new LeftSwipeAdjuster());
        mSupportSwipe.put(VIEW_TAG_RIGHT_SWIPE, new RightSwipeAdjuster());
        mSupportSwipe.put(VIEW_TAG_TOP_SWIPE, new TopSwipeAdjuster());
        mSupportSwipe.put(VIEW_TAG_BOTTOM_SWIPE, new BottomSwipeAdjuster());
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
            if (view != null) {
                views.add(view);
                ViewParent viewParent = findViewWithTag(key + VIEW_TAG_SWIPE_PARENT);
                if (viewParent == null) {
                    throw new RuntimeException("You must specify the toAdjustView of view[" + key + "]");
                }
                parents.add(viewParent);
            }
        }
        // set views ...
        if (views.size() > 0) {
            setViews(views.toArray(new View[views.size()]), parents.toArray(new ViewGroup[parents.size()]));
        }
    }

    /**
     * Active this event.
     *
     * @param downEvent
     * @return
     */
    public AViewAdjuster getAdjuster(MotionEvent downEvent) {
        AViewAdjuster viewAdjuster;
        Rect rect = new Rect();
        if (mSwipeIcon == null) {
            return null;
        }
        for (int i = 0; i < mSwipeIcon.length; i++) {
            View view = mSwipeIcon[i];
            getGlobalVisibleRect(rect);
            int dy = rect.top;
            view.getGlobalVisibleRect(rect);
            rect.offset(0, -dy);
            rect.inset(-20, -20);
            if (rect.contains((int) downEvent.getX(), (int) downEvent.getY())) {
                viewAdjuster = mSupportSwipe.get(view.getTag());
                viewAdjuster.init(mSwipeLayouts[i], view, downEvent);
                return viewAdjuster;
            }
        }
        return null;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v(TAG, "onTouchEvent -> " + event);
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }
        // action up
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mTouchManager != null) {
                mTouchManager = null;
                return true;
            }
        }
        return super.onTouchEvent(event);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        mTouchManager = getAdjuster(e);
        if (mTouchManager != null) {
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
        if (mTouchManager != null) {
            mTouchManager.click(e);
            return true;
        }

        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (mTouchManager != null) {
            mTouchManager.scroll(e1, e2, distanceX, distanceY);
            return true;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (mTouchManager != null) {
            mTouchManager.fling(e1, e2, velocityX, velocityY);
            return true;
        }
        return false;
    }

}
