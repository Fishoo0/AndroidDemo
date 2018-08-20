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
import android.widget.TextView;
import android.widget.VideoView;

import com.example.fishyu.fishdemo.R;
import com.example.yizhibopage.page.impletment.SimplePage;

public class PlayerPage extends SimplePage {


    public final static String[] URLS = new String[]{
            "http://alcdn.hls.xiaoka.tv/201883/4a0/5a7/9VWy9I9nav-LFu5P/index.m3u8",
            "http://mvvideo11.meitudata.com/5b74060ca19334690.mp4?k=15eb8558d0dc7c1974f92833ee011621&t=5b7e0a99",
            "http://mvvideo11.meitudata.com/5b72a13a741bc8960.mp4?k=c10b79daa2ca9a95147a422736320e78&t=5b7e0c86",
            "http://mvvideo10.meitudata.com/5b7450b9b3344803.mp4?k=2bec02d0a09c2ca17cb145a05e9848d4&t=5b7e23bf",
            "http://mvvideo11.meitudata.com/5b766aa2b8b389783_H264_4_208ae9721b8e82.mp4?k=77feae3d58c6aad75de40c253c61b50f&t=5b7e23ff",
            "http://mvvideo11.meitudata.com/5b6bdb8cc164e288.mp4?k=2bf08311d5f32cae7ddc0d80010630e7&t=5b7e2436",
            "http://mvvideo10.meitudata.com/5b7277feec7c32464.mp4?k=dce8b88aff44833f26306127a772aba7&t=5b7e245c",
            "http://mvvideo11.meitudata.com/5b73cf35bba825842.mp4?k=29809bd97a667353bdf029383e63a0b4&t=5b7e2477",
            "http://mvvideo10.meitudata.com/5b63eaeef300b5250.mp4?k=d0b0a3561867aacb0006ac33e54253ed&t=5b7e2495",
            "http://mvvideo10.meitudata.com/5b614fd89d4d41850.mp4?k=3b95e39bb49d0efc37a2e7389da4318e&t=5b7e24bd",
            "http://alcdn.hls.xiaoka.tv/201883/4a0/5a7/9VWy9I9nav-LFu5P/index.m3u8",
            "http://mvvideo11.meitudata.com/5b74060ca19334690.mp4?k=15eb8558d0dc7c1974f92833ee011621&t=5b7e0a99",
            "http://mvvideo11.meitudata.com/5b72a13a741bc8960.mp4?k=c10b79daa2ca9a95147a422736320e78&t=5b7e0c86",
            "http://mvvideo10.meitudata.com/5b7450b9b3344803.mp4?k=2bec02d0a09c2ca17cb145a05e9848d4&t=5b7e23bf",
            "http://mvvideo11.meitudata.com/5b766aa2b8b389783_H264_4_208ae9721b8e82.mp4?k=77feae3d58c6aad75de40c253c61b50f&t=5b7e23ff",
            "http://mvvideo11.meitudata.com/5b6bdb8cc164e288.mp4?k=2bf08311d5f32cae7ddc0d80010630e7&t=5b7e2436",
            "http://mvvideo10.meitudata.com/5b7277feec7c32464.mp4?k=dce8b88aff44833f26306127a772aba7&t=5b7e245c",
            "http://mvvideo11.meitudata.com/5b73cf35bba825842.mp4?k=29809bd97a667353bdf029383e63a0b4&t=5b7e2477",
            "http://mvvideo10.meitudata.com/5b63eaeef300b5250.mp4?k=d0b0a3561867aacb0006ac33e54253ed&t=5b7e2495",
            "http://mvvideo10.meitudata.com/5b614fd89d4d41850.mp4?k=3b95e39bb49d0efc37a2e7389da4318e&t=5b7e24bd",
    };


    private VideoView mVideoView;
    private TextView mTextView;

    public PlayerPage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.player_page, this);
        mVideoView = findViewById(R.id.video_view);
        mTextView = findViewById(R.id.textview);
    }


    private String mUrl;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mUrl = bundle.getString("url");
    }

    @Override
    public View onCreateView(ViewParent viewParent) {
        mTextView.setText(String.valueOf(getArgument().getInt("position")));
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


