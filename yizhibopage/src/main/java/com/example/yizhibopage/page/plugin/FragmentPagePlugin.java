package com.example.yizhibopage.page.plugin;


import com.example.yizhibopage.page.IPage;

public class FragmentPagePlugin extends SimplePagePlugin {

    private Object mFragment;

    /**
     * @param fragment either {@link android.app.Fragment} or {@link android.support.v4.app.Fragment}
     * @param page
     */
    public FragmentPagePlugin(Object fragment, IPage page) {
        super(page);
        if (fragment instanceof android.app.Fragment) {
            mFragment = fragment;
        } else if (fragment instanceof android.support.v4.app.Fragment) {
            mFragment = fragment;
        } else {
            throw new IllegalArgumentException("Fragment must be either {@link android.app.Fragment} or {@link android.support.v4.app.Fragment}," +
                    " witch you pass is -> " + fragment);
        }
    }

}
