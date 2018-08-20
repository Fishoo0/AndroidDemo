package com.example.yizhibopage.page.impletment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.example.yizhibopage.page.IPage;

public class SimplePage extends FrameLayout implements IPage {

    public String TAG = this.toString();

    public SimplePage(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public SimplePage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SimplePage(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SimplePage(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private IPageManager mPageManager;
    private Bundle mArgument;


    protected void init(Context context, AttributeSet attrs) {
        setVisibility(GONE);
        mPageManager = createPageManager();
    }


    protected IPageManager createPageManager() {
        return new PageManager(this);
    }


    @Override
    public void onCreate(Bundle bundle) {
        Log.v(TAG, "onCreate");
        getPageManager().setStatus(IPage.ON_CREATE, false);
    }

    @Override
    public View onCreateView(ViewParent viewParent) {
        Log.v(TAG, "onCreateView");
        getPageManager().setStatus(IPage.ON_CREATE_VIEW, false);
        return this;
    }

    @Override
    public void onStart() {
        Log.v(TAG, "onStart");
        getPageManager().setStatus(IPage.ON_START, false);
    }

    @Override
    public void onResume() {
        Log.v(TAG, "onResume");
        getPageManager().setStatus(IPage.ON_RESUME, false);
    }

    @Override
    public void onPause() {
        Log.v(TAG, "onPause");
        getPageManager().setStatus(IPage.ON_PAUSE, false);
    }

    @Override
    public void onStop() {
        Log.v(TAG, "onStop");
        getPageManager().setStatus(IPage.ON_STOP, false);
    }

    @Override
    public void onDestroyView() {
        Log.v(TAG, "onDestroyView");
        getPageManager().setStatus(IPage.ON_DESTROY_VIEW, false);
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        getPageManager().setStatus(IPage.ON_DESTROY, false);
    }

    @Override
    public void setArgument(Bundle bundle) {
        Log.v(TAG, "setArgument -> " + bundle);
        mArgument = bundle;
    }

    @Override
    public Bundle getArgument() {
        return mArgument;
    }

    @Override
    public IPageManager getPageManager() {
        return mPageManager;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + hashCode();
    }


    /**
     * Created by fishyu on 2018/8/10.
     * <p>
     * 1, Mange LifeCycle
     * 2, Mange internal variability
     * 3, Adding/Removing
     * 4, others
     */

    public static class PageManager implements IPage.IPageManager {

        final boolean DEBUG = true;

        final String TAG;

        /**
         * Align Life Cycle of Page
         */
        private final class InternalHandler extends Handler {

        }

        private InternalHandler mInternalHandler = new InternalHandler();

        /**
         * Converting state from Integer to String
         *
         * @param state
         * @return
         */
        static String stateToString(int state) {
            switch (state) {
                case NONE:
                    return "NONE";

                case ON_CREATE:
                    return "ON_CREATE";

                case ON_CREATE_VIEW:
                    return "ON_CREATE_VIEW";

                case ON_START:
                    return "ON_START";

                case ON_RESUME:
                    return "ON_RESUME";

                case ON_PAUSE:
                    return "ON_PAUSE";

                case ON_STOP:
                    return "ON_STOP";

                case ON_DESTROY_VIEW:
                    return "ON_DESTROY_VIEW";

                case ON_DESTROY:
                    return "ON_DESTROY";
            }
            return "Unknown state -> " + state;
        }


        protected IPage mPage;
        private int mCurrentStatus = NONE;

        private Object mContainer;
        private Context mContext;
        private ViewParent mViewParent;


        public PageManager(IPage page) {
            mPage = page;
            TAG = this.toString();
        }

        protected Handler getHandler() {
            return mInternalHandler;
        }

        @Override
        public void install(Object container, ViewParent viewParent, int targetStatus) {
            if (DEBUG)
                Log.i(TAG, "install status -> " + stateToString(targetStatus) + ", container -> " + container.getClass().getSimpleName() + " viewParent -> " + (viewParent != null ? viewParent.toString() : viewParent));

            if (mContext != null) {
                Log.w(TAG, mPage + " has been added to Context:" + mContext + ", you may need to call #uninstall first from original Container. Just return.");
                return;
            }

            if (mCurrentStatus != NONE && mCurrentStatus != ON_DESTROY) {
                throw new IllegalStateException(mPage + " has been in use! Current status is -> " + stateToString(mCurrentStatus));
            }

            if (container instanceof Activity) {
                mContext = (Context) container;
            } else if (container instanceof android.support.v4.app.Fragment) {
                mContext = ((android.support.v4.app.Fragment) container).getActivity();
            } else if (container instanceof Fragment) {
                mContext = ((Fragment) container).getActivity();
            } else if (container instanceof View) {
                mContext = ((View) container).getContext();
            } else {
                throw new IllegalArgumentException("Invalid container -> " + container);
            }
            mContainer = container;
            mViewParent = viewParent;
            ((View) mPage).setVisibility(View.VISIBLE);

            setStatus(targetStatus, true);
        }

        @Override
        public boolean isInstalled() {
            return mContext != null;
        }

        /**
         * Remove page
         */
        @Override
        public void uninstall() {
            if (DEBUG) Log.i(TAG, "uninstall");
            if (mContext == null) {
                Log.w(TAG, "Page has not been installed, just return.");
                return;
            }
            if (mPage instanceof View) {
                ((View) mPage).setVisibility(View.GONE);
            }
            setStatus(ON_DESTROY, true);
            mContext = null;
            mViewParent = null;
            mInternalHandler.removeCallbacksAndMessages(null);
        }

        @Override
        public void setStatus(int targetStatus, boolean align) {
            if (DEBUG)
                Log.d(TAG, "setStatus -> " + stateToString(targetStatus) + " align -> " + align);
            // return
            if (targetStatus == mCurrentStatus) {
                return;
            }

            if (mContext == null) {
                throw new IllegalStateException(mPage + " has not been installed yet, invalid #setStatus status -> " + targetStatus + " align ->  " + align);
            }

            if (align) {
                dispatchLifeCycle(targetStatus);
            } else {
                mCurrentStatus = targetStatus;
            }

        }


        private void dispatchLifeCycle(int targetStatus) {
            if (DEBUG)
                Log.d(TAG, "dispatchLifeCycle targetStatus -> " + stateToString(targetStatus) + " currentStatus -> " + stateToString(mCurrentStatus));
            // make reuse possible
            if (mCurrentStatus == ON_DESTROY) {
                mCurrentStatus = NONE;
            }

            if (targetStatus >= ON_CREATE && mCurrentStatus < ON_CREATE) {
                executeLifeCycle(ON_CREATE);
            }

            if (targetStatus >= ON_CREATE_VIEW && mCurrentStatus < ON_CREATE_VIEW) {
                executeLifeCycle(ON_CREATE_VIEW);
            }

            if (targetStatus >= ON_START && (mCurrentStatus < ON_START || mCurrentStatus == ON_STOP)) {
                executeLifeCycle(ON_START);
            }

            if (targetStatus >= ON_RESUME && (mCurrentStatus < ON_RESUME || mCurrentStatus == ON_PAUSE)) {
                executeLifeCycle(ON_RESUME);
            }

            if (targetStatus >= ON_PAUSE && mCurrentStatus < ON_PAUSE) {
                executeLifeCycle(ON_PAUSE);
            }

            if (targetStatus >= ON_STOP && mCurrentStatus < ON_STOP) {
                executeLifeCycle(ON_STOP);
            }

            if (targetStatus >= ON_DESTROY_VIEW && mCurrentStatus < ON_DESTROY_VIEW) {
                executeLifeCycle(ON_DESTROY_VIEW);
            }

            if (targetStatus >= ON_DESTROY && mCurrentStatus < ON_DESTROY) {
                executeLifeCycle(ON_DESTROY);
            }
        }


        /**
         * Execute
         *
         * @param status
         */
        private void executeLifeCycle(int status) {
            if (DEBUG) Log.d(TAG, "executeLifeCycle -> " + stateToString(status));
            switch (status) {
                case ON_CREATE:
                    if (mPage.getContext() == null) {
                        throw new IllegalStateException("IPage#getContext() must not be null when onCreate!");
                    }
                    mPage.onCreate(mPage.getArgument());
                    mCurrentStatus = status;
                    break;
                case ON_CREATE_VIEW:
                    mPage.onCreateView(mViewParent);
                    mCurrentStatus = status;
                    break;
                case ON_START:
                    mPage.onStart();
                    mCurrentStatus = status;
                    break;
                case ON_RESUME:
                    mPage.onResume();
                    mCurrentStatus = status;
                    break;
                case ON_PAUSE:
                    mPage.onPause();
                    mCurrentStatus = status;
                    break;
                case ON_STOP:
                    mPage.onStop();
                    mCurrentStatus = status;
                    break;
                case ON_DESTROY_VIEW:
                    mPage.onDestroyView();
                    mCurrentStatus = status;
                    break;
                case ON_DESTROY:
                    mPage.onDestroy();
                    mCurrentStatus = status;
                    break;
                default:
                    throw new IllegalStateException("Invalid call in #handleMessage(" + stateToString(status) + ")");
            }
        }

        @Override
        public int getStatus() {
            return mCurrentStatus;
        }

        @Override
        public Object getContainer() {
            return mContainer;
        }

        @Override
        public Context getContext() {
            return mContext;
        }

        @Override
        public ViewParent getViewParent() {
            return mViewParent;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "@" + mPage;
        }
    }
}
