package com.example.yizhibopage.page;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;

/**
 * Created by fishyu on 2018/8/10.
 * <p>
 * 借鉴主端 IPage
 * <p>
 * <p>
 * Concepts:
 * <p>
 * Container 支持容器：
 * 1，Activity/Fragment
 * 2，ViewPager/ListView  等滑动组件
 * 3, Coming soon
 */
public interface IPage {

    int NONE = 0;
    int ON_CREATE = 1;
    int ON_CREATE_VIEW = 2;
    int ON_START = 3;
    int ON_RESUME = 4;
    int ON_PAUSE = 5;
    int ON_STOP = 6;
    int ON_DESTROY_VIEW = 7;
    int ON_DESTROY = 8;

    /**
     * PageManager of this page, including add/remove/data/status & etc.
     */
    interface IPageManager {

        /**
         * Install its Page
         *
         * @param container
         * @param viewParent
         */
        void install(Object container, ViewParent viewParent, int targetStatus);


        /**
         * Installed or not
         *
         * @return
         */
        boolean isInstalled();

        /**
         * Uninstall its page
         */
        void uninstall();

        /**
         * Setting targetStatus
         *
         * @param targetStatus
         */
        void setStatus(int targetStatus, boolean align);

        /**
         * Getting its page's status.
         *
         * @return
         */
        int getStatus();

        /**
         * Getting its page's Container
         *
         * @return
         */
        Object getContainer();

        /**
         * Getting its page's Container's Context
         *
         * @return
         */
        Context getContext();

        /**
         * Getting its page's ViewParent, may be null.
         *
         * @return
         */
        ViewParent getViewParent();
    }


    /**
     * Activity/Fragment #onCreate
     *
     * @param bundle
     */
    void onCreate(Bundle bundle);


    /**
     * Activity/Fragment #onCreateView
     *
     * @param viewParent
     */
    View onCreateView(ViewParent viewParent);


    /**
     * Activity/Fragment #onStart
     */
    void onStart();

    /**
     * Activity/Fragment #onResume
     */
    void onResume();

    /**
     * Activity/Fragment #onPause
     */
    void onPause();

    /**
     * Activity/Fragment #onStop
     */
    void onStop();

    /**
     * Activity/Fragment #onDestroyView
     */
    void onDestroyView();

    /**
     * Activity/Fragment #onDestroy
     */
    void onDestroy();

    /**
     * Getting Context of Container
     *
     * @return
     */
    Context getContext();

    /**
     * Fragment#setArgument
     *
     * @param bundle
     */
    void setArgument(Bundle bundle);

    /**
     * Fragment#getArgument
     *
     * @return
     */
    Bundle getArgument();

    // backward

    /**
     * Getting {@link IPageManager}, each page must have its PageManager
     *
     * @return
     */
    IPageManager getPageManager();
}
