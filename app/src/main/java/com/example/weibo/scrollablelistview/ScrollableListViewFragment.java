package com.example.weibo.scrollablelistview;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fishyu.fishdemo.R;

/**
 * Created by fishyu on 2018/4/11.
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class ScrollableListViewFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scrollable_listview_fragment, null);
//        RecyclerView listView = view.findViewById(R.id.listview);
//        listView.setLayoutManager(new LinearLayoutManager(getContext()));
//        MyRecycleViewAdapter adapter = new MyRecycleViewAdapter();
//        listView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();

        ListView listView = view.findViewById(R.id.listview);
        listView.setAdapter(new MyListAdapter());

        return view;
    }


    class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return createView(parent.getContext(), position);
        }
    }

    static TextView createView(Context context, int position) {
        TextView textView = new TextView(context);
        textView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400));
        textView.setText(String.valueOf(position));
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(30);
        textView.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
        return textView;
    }

    class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.ViewHolder> {


        final String TAG = MyRecycleViewAdapter.class.getSimpleName();


        class ViewHolder extends RecyclerView.ViewHolder {

            TextView textView;

            public ViewHolder(View view) {
                super(view);
                textView = (TextView) view;

            }

            void updateView(int position) {
                Log.v(TAG, "updateView -> " + position);
                textView.setText(String.valueOf(position));
            }
        }

        @Override
        public MyRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(createView(getContext(), -1));
        }

        @Override
        public void onBindViewHolder(MyRecycleViewAdapter.ViewHolder holder, int position) {
            holder.updateView(position);
        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }


}
