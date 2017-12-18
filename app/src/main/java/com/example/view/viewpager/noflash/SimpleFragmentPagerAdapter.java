package com.example.view.viewpager.noflash;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;

import java.util.List;

/**
 * Created by fishyu on 2017/12/18.
 * <p>
 * Functions:
 * 1, No flash when calling {@link PagerAdapter#notifyDataSetChanged()} and with new data set.
 */
public abstract class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    public SimpleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof ISimplePagerFragment) {
            Object data = ((ISimplePagerFragment) object).getDataFromAdapter();
            int p = indexOfDataInList(data);
            if (!isPositionIllegal(p)) {
                ((ISimplePagerFragment) object).updatePositionInAdapter(p);
                return p;
            }
        }
        return POSITION_NONE;
    }


    /**
     * The actual position of the passing data in Adapter's data List
     *
     * @param data The position
     * @return For example, List.indexOf(data); If can not find data, -1 is expected.
     */
    protected int indexOfDataInList(Object data) {
        if (data != null && getList() != null && getList().contains(data)) {
            return getList().indexOf(data);
        }
        return -1;
    }


    /**
     * Check the position
     *
     * @param p
     * @return
     */
    protected boolean isPositionIllegal(int p) {
        if (p < 0 || p >= getCount()) {
            return true;
        }
        return false;
    }


    /**
     * Getting data List of this Adapter
     *
     * @return
     */
    public abstract List getList();


    /**
     * Fragment must implement this callback for complete functions.
     * <p>
     * Functions:
     * 1, Get the position of the Fragment in Adapter's List
     */
    public interface ISimplePagerFragment {


        /**
         * Getting data passed from {@link SimpleFragmentPagerAdapter} which should be in
         * {@link SimpleFragmentPagerAdapter#getList()}
         *
         * @return
         */
        Object getDataFromAdapter();


        /**
         * Updating position when position changed
         *
         * @param newPosition
         */
        void updatePositionInAdapter(int newPosition);

    }


}
