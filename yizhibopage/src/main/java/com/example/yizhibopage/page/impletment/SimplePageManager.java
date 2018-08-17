package com.example.yizhibopage.page.impletment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;

import com.example.yizhibopage.page.IPage;

import static com.example.yizhibopage.page.IPage.NONE;
import static com.example.yizhibopage.page.IPage.ON_CREATE;
import static com.example.yizhibopage.page.IPage.ON_CREATE_VIEW;
import static com.example.yizhibopage.page.IPage.ON_DESTROY;
import static com.example.yizhibopage.page.IPage.ON_DESTROY_VIEW;
import static com.example.yizhibopage.page.IPage.ON_PAUSE;
import static com.example.yizhibopage.page.IPage.ON_RESUME;
import static com.example.yizhibopage.page.IPage.ON_START;
import static com.example.yizhibopage.page.IPage.ON_STOP;

/**
 * Created by fishyu on 2018/8/10.
 * <p>
 * 1, Mange LifeCycle
 * 2, Mange internal variability
 * 3, Adding/Removing
 * 4, others
 */

public class SimplePageManager implements IPage.IPageManager {

    final boolean DEBUG = false;

    final String TAG;

    /**
     * Align Life Cycle of Page
     */
    private final class LifeCycleAlignHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int targetStatus = msg.what;
            if (DEBUG) Log.i(TAG, "handleMessage -> " + stateToString(targetStatus));

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
        public void executeLifeCycle(int status) {
            if (DEBUG) Log.e(TAG, "executeLifeCycle -> " + stateToString(status));
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


        protected void setStatus(int targetStatus, boolean align, boolean post) {
            if (align) {
                if (post) {
                    sendEmptyMessage(targetStatus);
                } else {
                    handleMessage(obtainMessage(targetStatus));
                }
            } else {
                mCurrentStatus = targetStatus;
            }
        }

    }

    private LifeCycleAlignHandler mAlignHandler = new LifeCycleAlignHandler();

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


    private IPage mPage;
    private int mCurrentStatus = NONE;

    private Object mContainer;
    private Context mContext;
    private ViewParent mViewParent;


    public SimplePageManager(IPage page) {
        mPage = page;
        TAG = this.toString();
    }


    @Override
    public void install(Object container, ViewParent viewParent, int targetStatus) {
        if (DEBUG)
            Log.e(TAG, "install status -> " + stateToString(targetStatus) + ", container -> " + container.getClass().getSimpleName() + " viewParent -> " + (viewParent != null ? viewParent.getClass().getSimpleName() : viewParent));
        if (mContext != null) {
            throw new IllegalStateException("Page has been added to Context:" + mContext);
        }

        if (mCurrentStatus != NONE && mCurrentStatus != ON_DESTROY) {
            throw new IllegalStateException("Page has been in use! Current status is -> " + stateToString(mCurrentStatus));
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

    /**
     * Remove page
     */
    @Override
    public void uninstall() {
        if (mPage instanceof View) {
            ((View) mPage).setVisibility(View.GONE);
        }
        setStatus(ON_DESTROY, true);
        mContext = null;
        mViewParent = null;
    }

    @Override
    public void setStatus(int targetStatus, boolean align) {
        if (DEBUG) Log.e(TAG, "setStatus -> " + stateToString(targetStatus) + " align -> " + align);

        if (mContext == null) {
            throw new IllegalStateException(mPage + " has not been installed yet, invalid #setStatus status -> " + targetStatus + " align ->  " + align);
        }

        mAlignHandler.setStatus(targetStatus, align, true);

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
