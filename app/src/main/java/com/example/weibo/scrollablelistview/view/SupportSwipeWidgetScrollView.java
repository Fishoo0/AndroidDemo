package com.example.weibo.scrollablelistview.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

/**
 * Created by fishyu on 2018/4/13.
 * <p>
 * This view is designed only for AirBubbleActivity
 * <p>
 * Support ListView for RecycleView like swipe-able Widget like {@link android.widget.ListView}
 * with specific restrictions.
 * <p>
 * <p>
 * <p>
 * 支持的布局格式举例：
 * <p>
 * 一级       com.example.weibo.scrollablelistview.view.SupportSwipeWidgetScrollView<br>
 * <p>
 * 二级       view class="com.example.weibo.scrollablelistview.view.SupportSwipeWidgetScrollView$HostLayout"<br>
 * 三级           FrameLayout android:tag="header"<br>
 * <p>
 * 三级           FrameLayout android:tag="swipe"<br>
 * 四级           ListView<br>
 * <p>
 * Note:
 * <p>
 * 1，必须使用{@link HostLayout}做内容的根布局，且应该只有2个View:<br>
 * 一个是存放 header 的 Layout，我们称之为 HeaderLayout;<br>
 * 一个为存放 滑动组件 的 Layout，我们称之为 SwipeWidgetLayout;<br>
 * <p>
 * 2,HeaderLayout 里面应该尽可能简单，不应该存在 可滑动组件
 * <p>
 * 3,SwipeLayout 里面存放 滑动组件
 */
public class SupportSwipeWidgetScrollView extends ScrollView {

    static final String TAG = SupportSwipeWidgetScrollView.class.getSimpleName();

    /**
     * Normal scrolling status
     */
    public final static int STATUS_SCROLL_NORMAL = 1;

    /**
     * ScrollView has been given way to SwipeWidget like {@link android.widget.ListView}
     */
    public final static int STATUS_SCROLL_SWIPE_WIDGET = 2;


    public static final String STATUS_TO_STRING(int status) {
        switch (status) {
            case STATUS_SCROLL_NORMAL:
                return "STATUS_SCROLL_NORMAL";
            case STATUS_SCROLL_SWIPE_WIDGET:
                return "STATUS_SCROLL_SWIPE_WIDGET";
            default:
                return String.valueOf(status);
        }
    }

    private boolean mEnable = true;

    private InternalScrollChangeListener mInternalScrollChangeListener;

    private OnScrollChangeListener mOnScrollChangeListener;
    private IOnScrollStatusChangeListener mOnStatusChangeListener;

    private HostLayout mHostLayout;

    private ScrollAnimator mScrollAnimator = new ScrollAnimator();

    public SupportSwipeWidgetScrollView(Context context) {
        super(context);
        init(context);
    }

    public SupportSwipeWidgetScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SupportSwipeWidgetScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        setOverScrollMode(OVER_SCROLL_NEVER);
        mInternalScrollChangeListener = new InternalScrollChangeListener(new InternalStatusDealer());

        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.v(TAG, "onLayoutChange");
                Log.v(TAG, "SupportSwipeWidgetScrollView -> " + getMeasuredHeight());
                Log.v(TAG, "HostLayout -> " + mHostLayout.getMeasuredHeight());
                Log.v(TAG, "HeaderLayout -> " + mHostLayout.getHeaderLayout().getMeasuredHeight());
                Log.v(TAG, "SwipeWidgetLayout -> " + mHostLayout.getSwipeWidgetLayout().getMeasuredHeight());
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        try {
            mHostLayout = (HostLayout) getChildAt(0);
        } catch (ClassCastException e) {
            throw new RuntimeException("SupportSwipeWidgetScrollView must use SupportSwipeWidgetScrollView$HostLayout for its host Layout");
        }
        smoothScrollTo(0, 0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.v(TAG, "onInterceptTouchEvent");
        if (!mEnable) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.v(TAG, "onTouchEvent");
        int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_UP) {
            if (!mEnable) {
                return false;
            }
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        mInternalScrollChangeListener.onScrollChange(this, l, t, oldl, oldt);

        if (mOnScrollChangeListener != null) {
            mOnScrollChangeListener.onScrollChange(this, l, t, oldl, oldt);
        }
    }

    @Deprecated
    @Override
    public void setOnScrollChangeListener(View.OnScrollChangeListener l) {
        super.setOnScrollChangeListener(l);
    }

    /**
     * Getting internal {@link HostLayout}
     *
     * @return
     */
    public HostLayout getHostLayout() {
        return mHostLayout;
    }


    /**
     * Getting height of HostLayout's HeaderLayout
     *
     * @return
     */
    public int getHeaderLayoutHeight() {
        return getHostLayout() == null || getHostLayout().getHeaderLayout() == null ?
                0 : getHostLayout().getHeaderLayout().getMeasuredHeight();
    }


    /**
     * Enable/Disable scrolling ability of this scrollview
     *
     * @param enable
     */
    public void enableScroll(boolean enable) {
        mEnable = enable;
    }

    /**
     * Setting {@link OnScrollChangeListener}
     *
     * @param listener
     */
    public void setOnScrollChangeListener(OnScrollChangeListener listener) {
        this.mOnScrollChangeListener = listener;
    }

    /**
     * Setting listener for responding to scroll status change
     *
     * @param listener
     */
    public void setOnStatusChangeListener(IOnScrollStatusChangeListener listener) {
        mOnStatusChangeListener = listener;
    }

    /**
     * Setting to target status mode.
     * <p>
     * Changing of status would change scrolling position of this ScrollView
     *
     * @param targetStatus See {@link #STATUS_SCROLL_NORMAL} {@link #STATUS_SCROLL_SWIPE_WIDGET}, any
     *                     other value would set status to current status's opposite side.
     */
    public void setToStatus(int targetStatus) {
        Log.v(TAG, "setToStatus -> " + STATUS_TO_STRING(targetStatus));
        switch (targetStatus) {
            case STATUS_SCROLL_NORMAL:
                Log.e(TAG, "STATUS_SCROLL_NORMAL -> " + 0);
                mScrollAnimator.scrollTo(0);
                break;
            case STATUS_SCROLL_SWIPE_WIDGET:
                Log.e(TAG, "getHeaderLayoutHeight -> " + getHeaderLayoutHeight());
                mScrollAnimator.scrollTo(getHeaderLayoutHeight());
                break;
            default:
                Log.e(TAG, "Setting status to its opposite side ");
                if (mInternalScrollChangeListener.getScrollStatus() == STATUS_SCROLL_NORMAL) {
                    setToStatus(STATUS_SCROLL_SWIPE_WIDGET);
                } else {
                    setToStatus(STATUS_SCROLL_NORMAL);
                }
                break;
        }
    }


    final class ScrollAnimator implements Animator.AnimatorListener {

        final String TAG = ScrollAnimator.class.getSimpleName();

        ObjectAnimator animator;

        public ScrollAnimator() {

        }

        final void initAnimator() {
            if (animator == null) {
                animator = new ObjectAnimator().ofInt(this, "value", 0, 0);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.addListener(this);
                animator.setDuration(300);
            }
        }

        public final void scrollTo(int target) {
            Log.v(TAG, "scrollTo -> " + target);
            initAnimator();

            int current = getScrollY();
            if (current != target && !animator.isRunning()) {
                animator.setIntValues(current, target);
                animator.start();
            }
        }

        @SuppressWarnings("unused")
        public void setValue(int value) {
            SupportSwipeWidgetScrollView.this.scrollTo(0, value);
        }

        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }


    /**
     * Dealing with all the scroll change staff
     */
    class InternalScrollChangeListener implements OnScrollChangeListener {

        int lastStatus = STATUS_SCROLL_NORMAL;
        IOnScrollStatusChangeListener statusChangeListener;


        public InternalScrollChangeListener(IOnScrollStatusChangeListener statusChangeListener) {
            InternalScrollChangeListener.this.statusChangeListener = statusChangeListener;
        }

        @Override
        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            Log.e(TAG, "onScrollChanged scrollY -> " + scrollY + " oldScrollY -> " + oldScrollY + " headerHeight -> " + getHeaderLayoutHeight());

            // calculate status of current scroll status
            if (getHeaderLayoutHeight() == 0 || scrollY == 0 || scrollY < getHeaderLayoutHeight()) {
                onStatusUpdate(STATUS_SCROLL_NORMAL);
            } else {
                onStatusUpdate(STATUS_SCROLL_SWIPE_WIDGET);
            }
        }

        void onStatusUpdate(int newStatus) {
            if (newStatus != lastStatus) {
                statusChangeListener.onStatusChanged(newStatus, lastStatus);

                if (mOnStatusChangeListener != null) {
                    mOnStatusChangeListener.onStatusChanged(newStatus, lastStatus);
                }
                lastStatus = newStatus;
            }
        }

        public int getScrollStatus() {
            return lastStatus;
        }
    }

    /**
     * What actually to do when status change
     */
    class InternalStatusDealer implements IOnScrollStatusChangeListener {

        @Override
        public void onStatusChanged(int newStatus, int oldStatus) {
            Log.e(TAG, "onStatusChanged -> " + STATUS_TO_STRING(newStatus));
            Toast.makeText(getContext(), STATUS_TO_STRING(newStatus), Toast.LENGTH_SHORT).show();
            if (newStatus == STATUS_SCROLL_NORMAL) {
                enableScroll(true);
            } else {
                enableScroll(false);
            }
        }
    }

    /**
     * Compatible
     * <p>
     * Same as {@link View.android.view.View.OnScrollChangeListener}.
     */
    @SuppressWarnings("JavadocReference")
    public interface OnScrollChangeListener {

        /**
         * Same as {@link android.view.View.OnScrollChangeListener#onScrollChange(View, int, int, int, int)}.
         *
         * @param v
         * @param scrollX
         * @param scrollY
         * @param oldScrollX
         * @param oldScrollY
         */
        void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY);

    }


    /**
     * Listener for notify scroll status change
     */
    public interface IOnScrollStatusChangeListener {

        /**
         * Status has been changed
         *
         * @param newStatus See {@link #STATUS_SCROLL_NORMAL} {@link #STATUS_SCROLL_SWIPE_WIDGET}
         * @param oldStatus See {@link #STATUS_SCROLL_NORMAL} {@link #STATUS_SCROLL_SWIPE_WIDGET}
         */
        void onStatusChanged(int newStatus, int oldStatus);
    }


    /**
     * View extends {@link LinearLayout}
     * <p>
     * Internal ViewGroup for this scroll view.
     */
    public static class HostLayout extends LinearLayout {

        static final String TAG = HostLayout.class.getSimpleName();

        private View mHeaderLayout;
        private View mSwipeWidgetLayout;

        public HostLayout(Context context) {
            super(context);
            init(context);
        }

        public HostLayout(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        public HostLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init(context);
        }

        private void init(Context context) {
            setOrientation(LinearLayout.VERTICAL);
        }

        @Override
        protected void onFinishInflate() {
            super.onFinishInflate();
            //must be HeaderLayout
            if (getChildCount() != 2) {
                throw new RuntimeException("SupportSwipeWidgetScrollView$ContainerLayout must have " +
                        "two child, first one is header, and the next is swipe-able view(ListView/RecycleView)'s" +
                        " parent Layout.");
            }

            mHeaderLayout = getChildAt(0);
            if (hasSwipeWidget(mHeaderLayout)) {
                throw new RuntimeException("unsupported view hierarchy, HeaderLayout must not have a swipe-able widget.");
            }

            mSwipeWidgetLayout = getChildAt(1);
            if (!hasSwipeWidget(mSwipeWidgetLayout)) {
                throw new RuntimeException("unsupported view hierarchy, SwipeWidgetLayout must have a swipe-able widget.");
            }
        }

        /**
         * 获取头部Layout
         *
         * @return
         */
        public final View getHeaderLayout() {
            return mHeaderLayout;
        }

        /**
         * 获取包含 滑动控件 的Layout
         *
         * @return
         */
        public final View getSwipeWidgetLayout() {
            return mSwipeWidgetLayout;
        }

        @Override
        protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
            Log.v(TAG, "measureChildWithMargins -> " + child.getClass().getName() + " heightUsed -> " + heightUsed);
            int mode = MeasureSpec.getMode(parentHeightMeasureSpec);
            int size = MeasureSpec.getSize(parentHeightMeasureSpec);

            switch (mode) {
                case MeasureSpec.UNSPECIFIED:
                    Log.v(TAG, mode + " UNSPECIFIED -> " + size + " heightUsed -> " + heightUsed);
                    break;
                case MeasureSpec.AT_MOST:
                    Log.v(TAG, mode + " AT_MOST -> " + size + " heightUsed -> " + heightUsed);
                    break;
                case MeasureSpec.EXACTLY:
                    Log.v(TAG, mode + " EXACTLY -> " + size + " heightUsed -> " + heightUsed);
                    break;
                default:
                    Log.v(TAG, "" + mode + " " + size + " heightUsed -> " + heightUsed);
                    break;
            }
            // the key trick is here, let SwipeLayout has ScrollView's height
            if (child == getSwipeWidgetLayout()) {
                heightUsed = 0;
                parentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(parentHeightMeasureSpec), MeasureSpec.EXACTLY);
            }
            super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
        }


        /**
         * The given view has swipe-able widget or not
         *
         * @param view
         * @return
         */
        static final boolean hasSwipeWidget(View view) {
            if (view instanceof RecyclerView || view instanceof ListView || view instanceof ScrollView) {
                return true;
            } else if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    View v = ((ViewGroup) view).getChildAt(i);
                    if (hasSwipeWidget(v)) {
                        return true;
                    } else {
                        continue;
                    }
                }
                return false;
            } else {
                return false;
            }
        }
    }

}
