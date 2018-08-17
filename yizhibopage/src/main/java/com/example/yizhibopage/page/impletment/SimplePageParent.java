package com.example.yizhibopage.page.impletment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
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

    private List<IPage> mPages;

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
        for (IPage page : mPages) {
            page.onCreate(page.getArgument());
        }
    }

    @Override
    public View onCreateView(ViewParent viewParent) {
        View view = super.onCreateView(viewParent);
        for (IPage page : mPages) {
            page.onCreateView(SimplePageParent.this);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        for (IPage page : mPages) {
            page.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        for (IPage page : mPages) {
            page.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        for (IPage page : mPages) {
            page.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        for (IPage page : mPages) {
            page.onStop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        for (IPage page : mPages) {
            page.onDestroyView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (IPage page : mPages) {
            page.onDestroy();
        }
    }

    @Override
    public void setArgument(Bundle bundle) {
        super.setArgument(bundle);
        //child need to reset too
        for (IPage page : mPages) {
            page.setArgument(bundle);
        }
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        mPages = new ArrayList<>();
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


    /**
     * Getting current level
     *
     * @return
     */
    public int getLevel() {
        return mLevel;
    }

    /**
     * Policy of loading pages
     */
    public interface IPolicy {

        long getDelay(SimplePageParent pageLevel);
    }

    public static IPolicy mDefaultPolicy = new IPolicy() {

        @Override
        public long getDelay(SimplePageParent pageLevel) {
            return pageLevel.getLevel() * 1000;
        }
    };

    private IPolicy mPolicy = mDefaultPolicy;

    /**
     * Can only be used by TOP LEVEL IPage
     */
    public void install(IPolicy policy) {
        mPolicy = policy == null ? mDefaultPolicy : policy;
        getPageManager().install(getContext(), null, IPage.NONE);
    }

    /**
     * Page parent manager
     */
    private final class PageParentManager extends SimplePageManager {

        public PageParentManager(IPage page) {
            super(page);
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
            mPages.clear();
            for (int i = 0; i < getChildCount(); i++) {
                final View view = getChildAt(i);
                if (view instanceof IPage) {
                    final IPage page = (IPage) view;
                    page.setArgument(getArgument());
                    final long delay = page instanceof SimplePageParent ? mPolicy.getDelay((SimplePageParent) page) : 0;
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            page.getPageManager().install(getPageManager().getContainer(), SimplePageParent.this, getPageManager().getStatus());
                            mPages.add(page);
                        }
                    }, delay);
                    Log.d(TAG, "\t install child view -> " + view + ", and scheduled for showing in " + delay + " milliseconds.");
                } else {
                    Log.d(TAG, "\t pass child view -> " + view);
                }
            }
        }
    }

    @Override
    protected IPageManager createPageManager() {
        return new PageParentManager(this);
    }

}
