package com.demo.fishdemoxhb.quick.mvvm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.demo.fishdemoxhb.BR;
import com.demo.fishdemoxhb.quick.utils.reflect.ClassWrapper;
import com.demo.fishdemoxhb.quick.utils.reflect.Generic;
import com.demo.fishdemoxhb.quick.utils.reflect.ObjectWrapper;


public class QuickBaseActivity<B extends ViewDataBinding, V extends QuickBaseViewModel<?>> extends AppCompatActivity {

    public final String TAG = getClass().getSimpleName() + this;

    private final Handler mHandler = new Handler();

    protected B mDataBinding;
    protected V mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        parseIntent(savedInstanceState);
        mViewModel = createViewModel();
        mDataBinding = createDataBinding();
        if (mDataBinding != null) {
            setContentView(mDataBinding.getRoot());
            mDataBinding.setLifecycleOwner(this);
            mDataBinding.setVariable(BR.view, this);
            mDataBinding.setVariable(BR.activity, this);
            if (mViewModel != null) {
                mDataBinding.setVariable(BR.viewModel, mViewModel);
                mDataBinding.setVariable(BR.model, mViewModel.getModel());
                // will set something to viewModel
                if (!mViewModel.isInitialized()) {
                    mViewModel.setArguments(getIntent().getExtras());
                    mViewModel.setInitialized();
                }
            }
            init(mDataBinding);
        }
    }


    /**
     * Data should be parse before UI.
     *
     * @param savedInstanceState
     */
    protected void parseIntent(Bundle savedInstanceState) {
//        ARouter.getInstance().inject(this);
    }

    protected void setViewDataBinding(B dataBinding, V viewModel) {
    }

    protected B createDataBinding() {
        Class<B> clzB = getDataBindingType();
        if (clzB == ViewDataBinding.class) {
            return null;
        }
        return ClassWrapper.wrap(getDataBindingType()).invoke("inflate",
                ObjectWrapper.wrap(LayoutInflater.class, LayoutInflater.from(this)));
    }

    protected V createViewModel() {
        try {
            Class<V> clz = getViewModelType();
            Log.e(TAG,"" + clz);
            return ViewModelProviders.of(this).get(clz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected B getDataBinding() {
        return mDataBinding;
    }

    public V getViewModel() {
        return mViewModel;
    }

    protected void init(B binding) {
        setViewDataBinding(binding, mViewModel);
    }

    private Class<B> getDataBindingType() {
        return Generic.getParamTypeForSuper(getClass(), 0);
    }

    private Class<V> getViewModelType() {
        return Generic.getParamTypeForSuper(getClass(), 1);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.v(TAG, "onNewIntent");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v(TAG, "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
    }

    /**
     * Getting handler attached to this Controller
     *
     * @return
     */
    public final Handler getHandler() {
        return mHandler;
    }

}
