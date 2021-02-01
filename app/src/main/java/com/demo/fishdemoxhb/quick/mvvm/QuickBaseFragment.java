//package com.demo.fishdemoxhb.quick.mvvm;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.databinding.ViewDataBinding;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProviders;
//
//import org.xinkb.blackboard.android.BR;
//import org.xinkb.blackboard.android.quick.utils.reflect.ClassWrapper;
//import org.xinkb.blackboard.android.quick.utils.reflect.Generic;
//import org.xinkb.blackboard.android.quick.utils.reflect.ObjectWrapper;
//
//public class QuickBaseFragment<B extends ViewDataBinding, V extends QuickBaseViewModel<?>> extends Fragment {
//
//    protected final String TAG = getClass().getSimpleName();
//
//    protected B mDataBinding;
//    protected V mViewModel;
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        Log.v(TAG, "onAttach");
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.v(TAG, "onCreate");
//        mViewModel = createViewModel();
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Log.v(TAG, "onCreateView");
//        mDataBinding = createDataBinding(getContext());
//        mDataBinding.setLifecycleOwner(this);
//        mDataBinding.setVariable(BR.view, this);
//        mDataBinding.setVariable(BR.fragment, this);
//        mDataBinding.setVariable(BR.viewModel, mViewModel);
//        mDataBinding.setVariable(BR.model, mViewModel.getModel());
//        // will set something to viewModel
//        if (!mViewModel.isInitialized()) {
//            mViewModel.setArguments(getArguments());
//            mViewModel.init(this, mDataBinding.getRoot());
//            mViewModel.setInitialized();
//        }
//        init(mDataBinding);
//        return mDataBinding.getRoot();
//    }
//
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        Log.v(TAG, "onActivityCreated");
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        Log.v(TAG, "onViewCreated");
//    }
//
//    protected void setViewDataBinding(B dataBinding, V viewModel) {
//    }
//
//    protected B createDataBinding(Context context) {
//        return ClassWrapper.wrap(getDataBindingType()).invoke("inflate",
//            ObjectWrapper.wrap(LayoutInflater.class, LayoutInflater.from(context)));
//    }
//
//    /**
//     * If fragment viewModel's type equals activity viewModel's type, reuse it of activity.
//     *
//     * @return
//     */
//    protected V createViewModel() {
//        Class<V> viewModelType = getViewModelType();
//        if (getActivity() instanceof QuickBaseActivity) {
//            Class viewModelClz = Generic.getParamTypeForSuper(getActivity().getClass(), 1);
//            if (viewModelClz != null && viewModelClz == viewModelType) {
//                return ViewModelProviders.of(getActivity()).get(viewModelType);
//            }
//        }
//
//        if (getParentFragment() instanceof QuickBaseFragment) {
//            Class viewModelClz = Generic.getParamTypeForSuper(getParentFragment().getClass(), 1);
//            if (viewModelClz != null && viewModelClz == viewModelType) {
//                return ViewModelProviders.of(getParentFragment()).get(viewModelType);
//            }
//        }
//        return ViewModelProviders.of(this).get(viewModelType);
//    }
//
//    protected B getDataBinding() {
//        return mDataBinding;
//    }
//
//    public V getViewModel() {
//        return mViewModel;
//    }
//
//    protected void init(B binding) {
//        setViewDataBinding(binding, mViewModel);
//    }
//
//    private Class<B> getDataBindingType() {
//        return Generic.getParamType(getClass(), 0);
//    }
//
//    protected Class<V> getViewModelType() {
//        return Generic.getParamType(getClass(), 1);
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        Log.v(TAG, "onStart");
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.v(TAG, "onResume");
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        Log.v(TAG, "onPause");
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        Log.v(TAG, "onStop");
//    }
//
//    @Override
//    public void onDestroyView() {
//        getDataBinding().unbind();
//        super.onDestroyView();
//        Log.v(TAG, "onDestroyView");
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.v(TAG, "onDestroy");
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        Log.v(TAG, "onDetach");
//    }
//}
