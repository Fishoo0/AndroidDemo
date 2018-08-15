package com.example.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.fishyu.fishdemo.R;

public class JumpActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jump_activity);

        TextView textView = findViewById(R.id.textview);
        textView.setText(JumpActivity.class.getSimpleName() + " " + System.currentTimeMillis() + " " + this);

    }

    @Override
    public void onClick(View v) {
        jumpToThis(this);
    }

    public static final void jumpToThis(Context context) {
        context.startActivity(new Intent(context, JumpActivity.class));
    }
}
