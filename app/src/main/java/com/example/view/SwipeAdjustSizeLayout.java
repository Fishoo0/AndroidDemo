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

    private Rect temp = new Rect();

    /**
     * Getting
     *
     * @param view
     * @return
     */
    public final Rect getVisibleRect(View view) {
        getGlobalVisibleRect(temp);
        int dy = temp.top;
        view.getGlobalVisibleRect(temp);
        temp.offset(0, -dy);
        return temp;
    }

    /**
     * Adjuster.
     */
    abstract class AViewAdjuster {
        ViewGroup toAdjustView;
        View swipeView;

        float downX;
        float downY;

        InternalHandler handler;

        Rect rect = new Rect();


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
         * <p>
         * //            int x = (int) event2.getX();
         * //            int y = (int) event2.getY();
         * //            handler.removeCallbacksAndMessages(null);
         * //            handler.sendMessage(handler.obtainMessage(0, x, y));
         * //            return true;
         *
         * @param event1
         * @param event2
         * @param arg1
         * @param arg2
         */
        public boolean scroll(MotionEvent event1, MotionEvent event2, float arg1, float arg2) {
            return false;
        }


        /**
         * Click
         *
         * @param event
         */
        public final boolean click(MotionEvent event) {
            if (isClick()) {
                return onClick(event);
            }
            return false;
        }

        public abstract boolean onClick(MotionEvent event);

        /**
         * @return
         */
        public boolean isClick() {
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            int dy = rect.top;
            swipeView.getGlobalVisibleRect(rect);
            rect.offset(0, -dy);
            rect.inset(-20, -20);
            if (rect.contains((int) downX, (int) downY)) {
                return true;
            }
            return false;
        }

        /**
         * Fling
         *
         * @param event1
         * @param event2
         * @param arg1
         * @param arg2
         */
        public final boolean fling(MotionEvent event1, MotionEvent event2, float arg1, float arg2) {
            if (isFling(event1, event2, arg1, arg2)) {
                return onFling(event1, event2, arg1, arg2);
            }
            return false;
        }


        protected boolean isFling(MotionEvent event1, MotionEvent event2, float arg1, float arg2) {
            if (Math.abs(event2.getRawX() - mTouchManager.downX) / Math.abs(event2.getRawY() - mTouchManager.downY) > 1) {
//
//                getGlobalVisibleRect(rect);
//                int dy = rect.top;
//                toAdjustView.getGlobalVisibleRect(rect);
//                rect.offset(0, -dy);
//                rect.inset(-20, -20);
//                if (rect.contains((int) downX, (int) downY)) {
//                    return true;
//                }
                return true;
            }
            return false;
        }

        protected abstract boolean onFling(MotionEvent event1, MotionEvent event2, float arg1, float arg2);
    }


    class LeftSwipeAdjuster extends AViewAdjuster {
        int lastSize;
        int minSize;
        int originalSize;

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
            originalSize = originalRect.width();
        }

        /**
         * Close the
         *
         * @return
         */
        public boolean close() {
            if (animator != null && animator.isRunning()) {
                return false;
            }
            float start = lastSize;
            float end = minSize;
            // start animator
            ObjectAnimator animator = ObjectAnimator.ofFloat(this, "size", start, end);
            animator.setDuration(200);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.start();
            this.animator = animator;
            return true;
        }


        public boolean open() {
            if (animator != null && animator.isRunning()) {
                return false;
            }

            float start = lastSize;
            float end = originalSize;

            // start animator
            ObjectAnimator animator = ObjectAnimator.ofFloat(this, "size", start, end);
            animator.setDuration(200);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.start();
            this.animator = animator;
            return true;
        }

        @Override
        public boolean onClick(MotionEvent event) {
            if (event.getRawX() > originalRect.centerX()) {
                return close();
            } else {
                return open();
            }
        }

        @Override
        protected boolean onFling(MotionEvent event1, MotionEvent event2, float arg1, float arg2) {
            if (event2.getRawX() < event1.getRawX()) {
                return close();
            } else {
                return open();
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

    }


    class RightSwipeAdjuster extends LeftSwipeAdjuster {

        @Override
        public boolean onClick(MotionEvent event) {
            if (event.getRawX() < originalRect.centerX()) {
                return close();
            } else {
                return open();
            }
        }

        @Override
        protected boolean onFling(MotionEvent event1, MotionEvent event2, float arg1, float arg2) {
            if (event2.getRawX() > event1.getRawX()) {
                return close();
            } else {
                return open();
            }
        }
    }


    class TopSwipeAdjuster extends LeftSwipeAdjuster {

        @Override
        public void init(ViewGroup parent, View swipeView, MotionEvent downEvent) {
            super.init(parent, swipeView, downEvent);
            lastSize = parent.getLayoutParams().height;
            minSize = swipeView.getHeight();
            originalSize = originalRect.height();
        }

        @Override
        public boolean onClick(MotionEvent event) {
            if (event.getRawY() > originalRect.centerY()) {
                return close();
            } else {
                return open();
            }
        }


        @Override
        protected boolean onFling(MotionEvent event1, MotionEvent event2, float arg1, float arg2) {
            if (event2.getRawY() < event1.getRawY()) {
                return close();
            } else {
                return open();
            }
        }


        @Override
        public void setSize(float size) {
            ViewGroup.LayoutParams params = toAdjustView.getLayoutParams();
            params.height = (int) size;
            toAdjustView.setLayoutParams(params);
        }

    }


    class BottomSwipeAdjuster extends TopSwipeAdjuster {

        @Override
        public boolean onClick(MotionEvent event) {
            if (event.getRawY() < originalRect.centerY()) {
                return close();
            } else {
                return open();
            }
        }

        @Override
        protected boolean onFling(MotionEvent event1, MotionEvent event2, float arg1, float arg2) {
            if (event2.getRawY() > event1.getRawY()) {
                return close();
            } else {
                return open();
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
            ViewGroup viewParent = mSwipeLayouts[i];
            getGlobalVisibleRect(rect);
            int dy = rect.top;
            viewParent.getGlobalVisibleRect(rect);
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
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mGestureDetector.onTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_MOVE:
                if (mTouchManager != null) {
                    if (mTouchManager.isFling(null, ev, 0, 0)) {
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                // action up
                if (mTouchManager != null) {
                    mTouchManager = null;
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
            return mTouchManager.click(e);
        }
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (mTouchManager != null) {
            return mTouchManager.scroll(e1, e2, distanceX, distanceY);
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (mTouchManager != null) {
            return mTouchManager.fling(e1, e2, velocityX, velocityY);
        }
        return false;
    }

}
