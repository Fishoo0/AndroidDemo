package com.demo.fishdemoxhb.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.demo.fishdemoxhb.R;

/**
 * Seek Bar of SelfPractice
 */
public class SelfPracticeSeekBar extends FrameLayout {

    private SeekBar mSeekBar;

    private IOnLevelChanged mCallback;

    public SelfPracticeSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        LayoutInflater.from(context).inflate(R.layout.selfpractice_level_bar, this, true);
        mSeekBar = findViewById(R.id.seekbar);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setLevel(getLevel(), false);
            }
        });
    }

    /**
     * Setting the level of progress bar.
     *
     * @param level 0,1,2 0->0%; 1->50%; 2->100%
     */
    public void setLevel(int level, boolean fromUser) {
        switch (level) {
            case 0: {
                mSeekBar.setProgress(0);
                break;
            }
            case 1: {
                mSeekBar.setProgress(50);
                break;
            }
            case 2: {
                mSeekBar.setProgress(100);
                break;
            }
        }
        if (!fromUser && mCallback != null) {
            mCallback.onLevelChanged(level);
        }
    }

    /**
     * Getting current level
     *
     * @return
     */
    public int getLevel() {
        if (mSeekBar.getProgress() >= 0 && mSeekBar.getProgress() <= 100 / 3) {
            return 0;
        } else if (mSeekBar.getProgress() >= 100 / 3 && mSeekBar.getProgress() <= 2 * 100 / 3) {
            return 1;
        } else {
            return 2;
        }
    }


    public interface IOnLevelChanged {

        void onLevelChanged(int level);
    }
}
