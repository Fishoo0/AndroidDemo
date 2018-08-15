package com.example.application;

import android.annotation.TargetApi;
import android.app.Application;
import android.os.Build;
import android.os.Trace;

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        problemMethod();
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void problemMethod() {
        try {
            Thread.sleep(2000);
        } catch (Throwable t) {

        }
    }

}
