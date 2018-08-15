package com.example.yizhibopage.page.plugin;


import android.support.v4.app.Fragment;

import com.example.yizhibopage.page.IPage;

public class FragmentPagePlugin extends SimplePagePlugin {

    Fragment mFragment;

    public FragmentPagePlugin(Fragment fragment, IPage page) {
        super(page);
        mFragment = fragment;
    }

}
