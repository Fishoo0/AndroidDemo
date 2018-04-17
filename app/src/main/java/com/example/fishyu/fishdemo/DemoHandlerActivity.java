package com.example.fishyu.fishdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by fishyu on 2018/3/21.
 */

public class DemoHandlerActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textView = new TextView(this);
        textView.setTextSize(100);
        textView.setText(getClass().getSimpleName());

        setContentView(textView);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(handler == null) {
                    test();
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("Fish","post msg");
                        }
                    });
                }
            }
        });

    }

    Handler handler;


    void test() {


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("Fish","run");
                Looper.prepare();

                handler = new Handler();


                Looper.loop();
                Log.e("Fish","run finished");
            }
        });
        thread.start();



    }
}
