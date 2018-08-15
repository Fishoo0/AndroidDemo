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
        return new SimplePageManager(this);
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
}
