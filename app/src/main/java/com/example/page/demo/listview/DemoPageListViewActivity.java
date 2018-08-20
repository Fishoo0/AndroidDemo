package com.example.page.demo.listview;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.page.demo.DemoPage;
import com.example.page.demo.PlayerPage;
import com.example.yizhibopage.page.impletment.SimplePageParent;
import com.example.yizhibopage.page.plugin.ListViewPagePlugin;

public class DemoPageListViewActivity extends Activity {

    private ListView mListView;
    private ListViewPagePlugin.ListViewScrollListener mScrollListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListView = new ListView(this);
        mScrollListener = new ListViewPagePlugin.ListViewScrollListener(mListView);
        mListView.setOnScrollListener(mScrollListener);
        setContentView(mListView);

        ListViewAdapter adapter = new ListViewAdapter();
        mListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    class ListViewAdapter extends BaseAdapter {


        public ListViewAdapter() {
        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            //update views
            mScrollListener.notifyDataSetChanged();
        }

        @Override
        public Object getItem(int position) {
            return PlayerPage.URLS[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListViewPagePlugin plugin = null;
            DemoPage page;
            if (convertView instanceof ListViewPagePlugin) {
                plugin = (ListViewPagePlugin) convertView;
                page = (DemoPage) plugin.getPage();
            } else {
                page = new DemoPage(DemoPageListViewActivity.this);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2000);
                page.setLayoutParams(params);
            }

            Bundle bundle = page.getArgument() != null ? page.getArgument() : new Bundle();
            bundle.putString("url", PlayerPage.URLS[position]);
            bundle.putInt("position", position);
            page.setArgument(bundle);
            page.install(null);

            if (plugin == null) {
                plugin = new ListViewPagePlugin(DemoPageListViewActivity.this, page, mScrollListener);
            }
            return plugin;
        }
    }

}
