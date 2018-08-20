package com.example.yizhibopage.page.impletment;

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

import com.example.yizhibopage.page.IPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fishyu on 2018/8/10.
 */

public class SimplePageParent extends SimplePage {

    private int mLevel;

    public SimplePageParent(@NonNull Context context) {
        super(context);
    }

    public SimplePageParent(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SimplePageParent(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SimplePageParent(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        for (int i = 0; i < getChildCount(); i++) {
            final View view = getChildAt(i);
            if (view instanceof IPage) {
                IPage page = (IPage) view;
                if (page.getPageManager().isInstalled()) {
                    page.onCreate(page.getArgument());
                }
            }
        }
    }

    @Override
    public View onCreateView(ViewParent viewParent) {
        View v = super.onCreateView(viewParent);
        for (int i = 0; i < getChildCount(); i++) {
            final View view = getChildAt(i);
            if (view instanceof IPage) {
                IPage page = (IPage) view;
                if (page.getPageManager().isInstalled()) {
                    page.onCreateView(SimplePageParent.this);
                }
            }
        }
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        for (int i = 0; i < getChildCount(); i++) {
            final View view = getChildAt(i);
            if (view instanceof IPage) {
                IPage page = (IPage) view;
                if (page.getPageManager().isInstalled()) {
                    page.onStart();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        for (int i = 0; i < getChildCount(); i++) {
            final View view = getChildAt(i);
            if (view instanceof IPage) {
                IPage page = (IPage) view;
                if (page.getPageManager().isInstalled()) {
                    page.onResume();
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        for (int i = 0; i < getChildCount(); i++) {
            final View view = getChildAt(i);
            if (view instanceof IPage) {
                IPage page = (IPage) view;
                if (page.getPageManager().isInstalled()) {
                    page.onPause();
                }
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        for (int i = 0; i < getChildCount(); i++) {
            final View view = getChildAt(i);
            if (view instanceof IPage) {
                IPage page = (IPage) view;
                if (page.getPageManager().isInstalled()) {
                    page.onStop();
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        for (int i = 0; i < getChildCount(); i++) {
            final View view = getChildAt(i);
            if (view instanceof IPage) {
                IPage page = (IPage) view;
                if (page.getPageManager().isInstalled()) {
                    page.onDestroyView();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < getChildCount(); i++) {
            final View view = getChildAt(i);
            if (view instanceof IPage) {
                IPage page = (IPage) view;
                if (page.getPageManager().isInstalled()) {
                    page.onDestroy();
                }
            }
        }
    }

    @Override
    public void setArgument(Bundle bundle) {
        super.setArgument(bundle);
        //child need to reset too
        for (int i = 0; i < getChildCount(); i++) {
            final View view = getChildAt(i);
            if (view instanceof IPage) {
                IPage page = (IPage) view;
                page.setArgument(bundle);
            }
        }
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        int level = 0;
        if (getTag() instanceof String || getTag() instanceof Integer) {
            try {
                level = Integer.parseInt((String) getTag());
            } catch (NumberFormatException e) {
                // ignore
            }
        }
        if (level >= 0 && level < 10) {
            mLevel = level;
        } else {
            throw new RuntimeException("illegal level, current only support level between 0-9 ");
        }
    }


    @Override
    protected IPageManager createPageManager() {
        return new PageManager(this);
    }

    /**
     * Getting current level
     *
     * @return
     */
    public int getLevel() {
        return mLevel;
    }


    /**
     * Simple method.
     *
     * @param policy
     */
    public void install(PageManager.IPolicy policy) {
        if (policy != null) {
            ((PageManager) getPageManager()).mPolicy = policy;
        }
        getPageManager().install(getContext(), null, IPage.NONE);
    }


    /**
     * Page parent manager
     */
    public static class PageManager extends SimplePage.PageManager {

        /**
         * Policy of loading pages
         */
        public interface IPolicy {

            void onPageReady(IPage page, Runnable install);

            void onPageCancel();
        }

        public IPolicy mDefaultPolicy = new IPolicy() {

            Handler handler = new Handler();

            @Override
            public void onPageReady(IPage page, Runnable install) {
                final long delay = page instanceof SimplePageParent ? (((SimplePageParent) page).getLevel() * 1000) : 0;
                handler.postDelayed(install, delay);
            }

            @Override
            public void onPageCancel() {
                handler.removeCallbacksAndMessages(null);
            }
        };

        private IPolicy mPolicy = mDefaultPolicy;


        private SimplePageParent mSimplePageParent;

        public PageManager(SimplePageParent page) {
            super(page);
            mSimplePageParent = page;
        }


        @Override
        public void install(Object container, ViewParent viewParent, int targetStatus) {
            super.install(container, viewParent, targetStatus);
            installChildren();
        }

        /**
         * Install children
         */
        private void installChildren() {
            for (int i = 0; i < mSimplePageParent.getChildCount(); i++) {
                final View view = mSimplePageParent.getChildAt(i);
                if (view instanceof IPage) {
                    final IPage page = (IPage) view;
                    Runnable installation = new Runnable() {
                        @Override
                        public void run() {
                            page.getPageManager().install(getContainer(), mSimplePageParent, getStatus());
                        }
                    };
                    mPolicy.onPageReady(page, installation);
                    Log.d(TAG, "\t install child view -> " + view + ", and scheduled to IPolicy -> " + mPolicy);
                } else {
                    Log.d(TAG, "\t pass child view -> " + view);
                }
            }
        }

        @Override
        public void uninstall() {
            super.uninstall();
            for (int i = 0; i < mSimplePageParent.getChildCount(); i++) {
                final View view = mSimplePageParent.getChildAt(i);
                if (view instanceof IPage) {
                    IPage page = (IPage) view;
                    if (page.getPageManager().isInstalled()) {
                        page.getPageManager().uninstall();
                    }
                }
            }
            if (mPolicy != null) {
                mPolicy.onPageCancel();
            }
        }
    }

}
