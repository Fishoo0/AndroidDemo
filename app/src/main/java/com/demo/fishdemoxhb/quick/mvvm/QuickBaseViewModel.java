package com.demo.fishdemoxhb.quick.mvvm;

import android.app.Application;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;

import com.demo.fishdemoxhb.quick.utils.reflect.Generic;


public class QuickBaseViewModel<M extends QuickBaseModel> extends AndroidViewModel implements Observable {

    protected final String TAG = this.getClass().getSimpleName();
    private static Bundle EMPTY = new Bundle();

    private boolean mInitialized = false;
    protected Bundle mArguments = EMPTY;
    protected M mModel;
    private PropertyChangeRegistry mObservable = new PropertyChangeRegistry();

    public QuickBaseViewModel(Application app) {
        super(app);
        mModel = createModel(app);
    }

    protected M createModel(Application app) {
        return QuickModelManager.getTalModel(getModelType(), app);
    }

    public void setArguments(Bundle arguments) {
        if (arguments == null)
            return;
        mArguments = arguments;
//        ARouter.getInstance().inject(this);
    }

    public void init(Fragment fragment, View view) {
        init(view);
    }

    public void init(View view) {
    }

    public final void setInitialized() {
        mInitialized = true;
    }

    /**
     * Whether we have initialized or not, if we have initialized, we do nothing because
     * this is a re-used viewModel.
     *
     * @return
     */
    public boolean isInitialized() {
        return mInitialized;
    }

    public M getModel() {
        return mModel;
    }

    private Class<M> getModelType() {
        return Generic.getParamType(getClass(), 0);
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        mObservable.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        mObservable.remove(callback);
    }

    public void notifyChange() {
        mObservable.notify();
    }

    public void notifyPropertyChanged(int fieldId) {
        mObservable.notifyChange(this, fieldId);
    }


}
