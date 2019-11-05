package com.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.fishyu.fishdemo.R;
import com.example.updater.ApplicationUpdater;
import com.example.updater.InstallApkCompat;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ApplicationUpdater updater = new ApplicationUpdater(this);
//        updater.checkNewVersion();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        InstallApkCompat.onActivityResult(this, requestCode, resultCode, data);
    }
}
