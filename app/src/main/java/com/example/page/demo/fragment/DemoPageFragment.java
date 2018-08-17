package com.example.page.demo.fragment;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fishyu.fishdemo.R;
import com.example.page.demo.DemoPage;
import com.example.page.demo.PlayerPage;
import com.example.yizhibopage.page.IPage;
import com.example.yizhibopage.page.impletment.SimplePageParent;
import com.example.yizhibopage.page.plugin.ActivityPagePlugin;
import com.example.yizhibopage.page.plugin.FragmentPagePlugin;

public class DemoPageFragment extends Fragment {

    FragmentPagePlugin mPagePlugin;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DemoPage page = new DemoPage(getContext());
        Bundle bundle = new Bundle();
        bundle.putString("url", PlayerPage.URLS[1]);
        page.setArgument(bundle);
        page.install(SimplePageParent.mDefaultPolicy);

        mPagePlugin = new FragmentPagePlugin(this, page);
        mPagePlugin.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return mPagePlugin.onCreateView(container);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPagePlugin.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPagePlugin.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPagePlugin.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPagePlugin.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPagePlugin.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPagePlugin.onDestroy();
    }
}
