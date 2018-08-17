package com.example.fishyu.fishdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Trace;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.page.demo.EntranceActivity;

public class SplashActivity extends Activity {


    private static final String TAG = SplashActivity.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_activity);

        findViewById(R.id.cover).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                JumpActivity.jumpToThis(SplashActivity.this);
                startActivity(new Intent(SplashActivity.this, EntranceActivity.class));
                return true;
            }
        });

//        reportFullyDrawn();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onResume() {
        super.onResume();

//        testTraceAgain();
    }

    class MyTread extends Thread {


        public MyTread() {
            super("FUCK");
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void run() {
            super.run();
            Trace.beginSection("fish_test");

            Log.v(TAG, "testTrace");
            try {
                Thread.sleep(2000);
            } catch (Throwable t) {

            }
            Trace.endSection();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SplashActivity.this, "YEAH", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public void testTrace() {
        new MyTread().start();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void testTraceAgain() {
        Trace.beginSection("renren");

        Log.v(TAG, "testTrace");
        try {
            Thread.sleep(2000);
        } catch (Throwable t) {

        } finally {
            Trace.endSection();
        }
    }
}
