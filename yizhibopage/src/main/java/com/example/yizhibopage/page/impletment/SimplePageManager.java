package com.example.yizhibopage.page.impletment;


import android.app.Activity;
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
            if (DEBUG) Log.e(TAG, "handleMessage -> " + stateToString(msg.what));
            switch (msg.what) {
                case ON_CREATE:
                    if (mPage.getContext() == null) {
                        throw new IllegalStateException("IPage#getContext() must not be null when onCreate!");
                    }
                    mPage.onCreate(mPage.getArgument());
                    break;
                case ON_CREATE_VIEW:
                    mPage.onCreateView(mViewParent);
                    break;
                case ON_START:
                    mPage.onStart();
                    break;
                case ON_RESUME:
                    mPage.onResume();
                    break;
                case ON_PAUSE:
                    mPage.onPause();
                    break;
                case ON_STOP:
                    mPage.onStop();
                    break;
                case ON_DESTROY_VIEW:
                    mPage.onDestroyView();
                    break;
                case ON_DESTROY:
                    mPage.onDestroy();
                    break;
                default:
                    throw new IllegalStateException("Invalid call in #handleMessage(" + stateToString(msg.what) + ")");
            }
            mCurrentStatus = msg.what;
        }


        public void execute(int status, boolean post) {
            if (post) {
                sendEmptyMessage(status);
            } else {
                handleMessage(obtainMessage(status));
            }
        }

        /**
         * Align IPage's Life Cycle
         *
         * @param currentStatus
         * @param targetStatus
         * @param post
         */
        protected void alignLifeCycle(int currentStatus, int targetStatus, boolean post) {
            // onCreate can not be auto aligned
            if (targetStatus >= ON_CREATE && currentStatus < ON_CREATE) {
                execute(ON_CREATE, post);
            }

            if (targetStatus >= ON_CREATE_VIEW && currentStatus < ON_CREATE_VIEW) {
                execute(ON_CREATE_VIEW, post);
            }

            if (targetStatus >= ON_START && (currentStatus < ON_START || currentStatus == ON_STOP)) {
                execute(ON_START, post);
            }

            if (targetStatus >= ON_RESUME && (currentStatus < ON_RESUME || currentStatus == ON_PAUSE)) {
                execute(ON_RESUME, post);
            }

            if (targetStatus >= ON_PAUSE && currentStatus < ON_PAUSE) {
                execute(ON_PAUSE, post);
            }

            if (targetStatus >= ON_STOP && currentStatus < ON_STOP) {
                execute(ON_STOP, post);
            }

            if (targetStatus >= ON_DESTROY_VIEW && currentStatus < ON_DESTROY_VIEW) {
                execute(ON_DESTROY_VIEW, post);
            }

            if (targetStatus >= ON_DESTROY && currentStatus < ON_DESTROY) {
                execute(ON_DESTROY, post);
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
    public void setStatus(int status, boolean align) {
        if (DEBUG) Log.e(TAG, "setStatus -> " + stateToString(status) + " align -> " + align);
        if (align) {
            mAlignHandler.alignLifeCycle(mCurrentStatus, status, true);
        } else {
            mCurrentStatus = status;
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
