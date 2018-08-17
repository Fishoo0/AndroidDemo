package com.example.page.demo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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


    public final static String[] URLS = new String[]{
            "http://alcdn.hls.xiaoka.tv/201883/4a0/5a7/9VWy9I9nav-LFu5P/index.m3u8",
            "http://f.us.sinaimg.cn//002rJKiFlx07mNuy0dKg010402004N690k010.mp4?label=mp4_hd&template=480x852.28&Expires=1534497007&ssig=829W2xrzld&KID=unistore,video",
            "http://f.us.sinaimg.cn//002LbGPelx07mLuNwHGw0104020027Sm0k010.mp4?label=mp4_ld&template=360x636.24&Expires=1534497015&ssig=Uv469w5rkQ&KID=unistore,video",
            "http://f.us.sinaimg.cn//002dLpxDlx07mO5wiwBO010402002QZl0k010.mp4?label=mp4_hd&template=544x960.24&Expires=1534497109&ssig=f%2B6d75kN1t&KID=unistore,video",
            "http://f.us.sinaimg.cn//002DyyUVlx07mPuRhs76010402002ohk0k010.mp4?label=mp4_hd&template=480x844.28&Expires=1534497090&ssig=kpNKaieuzu&KID=unistore,video",
            "http://f.us.sinaimg.cn//001Jv4UMlx07mK7RGvsI0104020063rS0k010.mp4?label=mp4_720p&template=720x1280.28&Expires=1534497126&ssig=XXZw%2Fb72pT&KID=unistore,video",
            "http://f.us.sinaimg.cn//002yrF65lx07mKFVn7H2010402003Iow0k010.mp4?label=mp4_hd&template=480x852.28&Expires=1534497144&ssig=raGkw6tWL8&KID=unistore,video",
            "http://f.us.sinaimg.cn//001vXF1Ogx07mIPLdO59010402008BEE0k010.mp4?label=mp4_hd&template=540x960.24&Expires=1534497185&ssig=hT0CQszeLk&KID=unistore,video",
            "http://f.us.sinaimg.cn//000CtKABlx07mP8lPl3q0104020027Qy0k010.mp4?label=mp4_hd&template=480x852.28&Expires=1534497163&ssig=qiNAE0Plwu&KID=unistore,video",
            "http://f.us.sinaimg.cn//000yfmALlx07mIhvZGxq01040200acv30k010.mp4?label=mp4_hd&template=544x960.24&Expires=1534497178&ssig=ANnRyaAv%2Bz&KID=unistore,video",
            "http://f.us.sinaimg.cn//000kzY6Wlx07mJ4Ptaa401040200cwAs0k010.mp4?label=mp4_hd&template=540x960.24&Expires=1534497156&ssig=ngcyFeISQ3&KID=unistore,video",
            "http://alcdn.hls.xiaoka.tv/201883/4a0/5a7/9VWy9I9nav-LFu5P/index.m3u8",
            "http://alcdn.hls.xiaoka.tv/201883/4a0/5a7/9VWy9I9nav-LFu5P/index.m3u8",
            "http://alcdn.hls.xiaoka.tv/201883/4a0/5a7/9VWy9I9nav-LFu5P/index.m3u8",
            "http://alcdn.hls.xiaoka.tv/201883/4a0/5a7/9VWy9I9nav-LFu5P/index.m3u8",
            "http://alcdn.hls.xiaoka.tv/201883/4a0/5a7/9VWy9I9nav-LFu5P/index.m3u8",
            "http://alcdn.hls.xiaoka.tv/201883/4a0/5a7/9VWy9I9nav-LFu5P/index.m3u8",
            "http://alcdn.hls.xiaoka.tv/201883/4a0/5a7/9VWy9I9nav-LFu5P/index.m3u8",
    };


    private VideoView mVideoView;

    public PlayerPage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.player_page, this);
        mVideoView = findViewById(R.id.video_view);
    }


    private String mUrl;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mUrl = bundle.getString("url");
    }

    @Override
    public View onCreateView(ViewParent viewParent) {
        return super.onCreateView(viewParent);
    }

    @Override
    public void onStart() {
        super.onStart();
        mVideoView.setVideoURI(Uri.parse(mUrl));
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


