package com.example.page.demo;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.VideoView;

import com.example.fishyu.fishdemo.R;
import com.example.yizhibopage.page.impletment.SimplePage;

public class PlayerPage extends SimplePage {

    public PlayerPage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private VideoView mVideoView;

    @Override
    public View onCreateView(ViewParent viewParent) {
        LayoutInflater.from(getContext()).inflate(R.layout.player_page, this);
        mVideoView = findViewById(R.id.video_view);
        return super.onCreateView(viewParent);
    }


    @Override
    public void onStart() {
        super.onStart();
        mVideoView.setVideoURI(Uri.parse("http://alcdn.hls.xiaoka.tv/201883/4a0/5a7/9VWy9I9nav-LFu5P/index.m3u8"));
    }

    @Override
    public void onResume() {
        super.onResume();
        mVideoView.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mVideoView.pause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
    }
}


