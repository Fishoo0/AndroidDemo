package com.demo.fishdemoxhb.quick.bindingadapter;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class QuickBindingAdapter {

    @androidx.databinding.BindingAdapter(value = {"android:src"})
    public static <T> void bindAndroidSrc(ImageView imageView, T data) {
        if (data instanceof String) {
        } else if (data instanceof Integer) {
            imageView.setImageResource((Integer) data);
        } else if (data instanceof Drawable) {
            imageView.setImageDrawable((Drawable) data);
        }
    }
}
