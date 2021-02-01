package com.demo.fishdemoxhb.quick.mvvm;

import android.app.Application;

import androidx.databinding.BaseObservable;

public abstract class QuickBaseModel extends BaseObservable {

    protected String TAG = QuickBaseModel.class.getSimpleName();
    private final Application mApplication;

    public QuickBaseModel(Application application) {
        this.mApplication = application;
    }

}
