package com.demo.fishdemoxhb.quick;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.demo.fishdemoxhb.BR;
import com.demo.fishdemoxhb.R;
import com.demo.fishdemoxhb.databinding.QuickEmptyViewBinding;


/**
 * Recommend using the empty view for catch empty issues.
 * <p>
 * 1, You can use attribute and data binding for easy case, like changing the default empty image or empty text.
 */
public class QuickEmptyView extends FrameLayout {
    private static final String TAG = com.demo.fishdemoxhb.quick.QuickEmptyView.class.getSimpleName();
    public static final String EMPTY_TAG = com.demo.fishdemoxhb.quick.QuickEmptyView.class.getSimpleName();
    private MutableLiveData<Data> mLiveData = new MutableLiveData<>();

    private final QuickEmptyViewBinding mViewBinding;

    private boolean mShowEmptyView = true;

    public QuickEmptyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mViewBinding = QuickEmptyViewBinding.inflate(LayoutInflater.from(context), null, false);
        mViewBinding.setLifecycleOwner((LifecycleOwner) context);
        mViewBinding.setVariable(BR.view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addView(mViewBinding.getRoot(), 0);
    }

    /**
     * Is empty view or not ?
     *
     * @param view
     * @return
     */
    protected boolean isEmptyView(View view) {
        if (view == mViewBinding.getRoot()) {
            return mShowEmptyView;
        } else if (view.getTag(EmptyViewMarkID) instanceof Boolean && (Boolean) view.getTag(EmptyViewMarkID)) {
            return true;
        }
        return false;
    }

    public final void setData(Data data) {
        if (data != null) {
            mLiveData.setValue(data);
            setEmpty(data);
        }
    }

    public final Data getData() {
        return mLiveData.getValue();
    }

    public final LiveData<Data> getLiveData() {
        return mLiveData;
    }

    public void setEmpty(Data data) {
        for (int i = 0; i < getChildCount(); i++) {
            if (!isEmptyView(getChildAt(i))) {
                getChildAt(i).setVisibility(data.isEmpty ? GONE : VISIBLE);
            } else {
                getChildAt(i).setVisibility(data.isEmpty ? VISIBLE : GONE);
            }
        }
    }

    /**
     * The data for view displaying
     */
    public static class Data {
        private boolean isEmpty = false;
        private boolean isLoading = false;

        private Object data = null;
        private Throwable error = null;

        private String emptyTextDes;
        private String emptyButtonDes;

        private String errorTextDes;
        private String errorButtonDes;

        private Object emptyImageData = R.drawable.img_choice;
        private Object errorImageData = R.drawable.img_choice;


        private Data() {
        }

        public static Data build(String emptyTextDes, String emptyButtonDes, Object emptyImageData, String errorTextDes, String errorButtonDes, Object errorImageData) {
            Data data = new Data();
            data.emptyTextDes = emptyTextDes;
            data.emptyButtonDes = emptyButtonDes;
            data.emptyImageData = emptyImageData;
            data.errorTextDes = errorTextDes;
            data.errorButtonDes = errorButtonDes;
            data.errorImageData = errorImageData;
            return data;
        }

        public static Data build(String emptyTextDes, String emptyButtonDes, Object emptyImageData) {
            return build(emptyTextDes, emptyButtonDes, emptyImageData, "发生错误，请重试", "重试", R.drawable.img_choice);
        }

        public static Data build() {
            return build("暂无数据", "重试", R.drawable.img_choice);
        }

        public static Data build(String emptyTextDes, String emptyButtonDes) {
            return build(emptyTextDes, emptyButtonDes, R.drawable.img_choice);
        }

        private String getEmptyTextDes() {
            return emptyTextDes;
        }

        public void setEmptyTextDes(String emptyTextDes) {
            this.emptyTextDes = emptyTextDes;
        }

        private String getErrorTextDes() {
            if (TextUtils.isEmpty(this.errorTextDes)) {
                if (getError() != null) {
                    return getError().getMessage();
                } else {
                    return "未知错误";
                }
            }
            return errorTextDes;
        }

        private void setErrorTextDes(String errorTextDes) {
            this.errorTextDes = errorTextDes;
        }

        private String getEmptyButtonDes() {
            return emptyButtonDes;
        }

        public void setEmptyButtonDes(String emptyButtonDes) {
            this.emptyButtonDes = emptyButtonDes;
        }

        private String getErrorButtonDes() {
            return errorButtonDes;
        }

        public void setErrorButtonDes(String errorButtonDes) {
            this.errorButtonDes = errorButtonDes;
        }

        private Object getEmptyImageData() {
            return emptyImageData;
        }

        public void setEmptyImageData(Object emptyImageData) {
            this.emptyImageData = emptyImageData;
        }

        private Object getErrorImageData() {
            return errorImageData;
        }

        public void setErrorImageData(Object errorImageData) {
            this.errorImageData = errorImageData;
        }

        public String getTextDes() {
            return error != null ? getErrorTextDes() : getEmptyTextDes();
        }

        public String getButtonDes() {
            return error != null ? getErrorButtonDes() : getEmptyButtonDes();
        }

        public Object getImageData() {
            return error != null ? getErrorImageData() : getErrorImageData();
        }


        public boolean isEmpty() {
            return isEmpty;
        }

        public void setEmpty(boolean empty) {
            isEmpty = empty;
        }

        public boolean isLoading() {
            return isLoading;
        }

        public void setLoading(boolean loading) {
            isLoading = loading;
        }

        /**
         * Setting error if any.
         *
         * @param error
         */
        public void setError(Throwable error) {
            this.error = error;
            setLoading(false);
        }

        public Throwable getError() {
            return error;
        }

        public Object getData() {
            return data;
        }

        /**
         * Set data if success
         *
         * @param data
         */
        public void setData(Object data) {
            this.data = data;
            setLoading(false);
            setError(null);
        }

        /**
         * This method only mark data set.
         */
        public void setData() {
            setData(null);
        }


    }


    @androidx.databinding.BindingAdapter({"data"})
    public static void bindData(com.demo.fishdemoxhb.quick.QuickEmptyView view, Data object) {
        view.setData(object);
    }

    @androidx.databinding.BindingAdapter({"buttonListener"})
    public static void bindData(com.demo.fishdemoxhb.quick.QuickEmptyView view, Runnable retry) {
        view.mViewBinding.emptyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                retry.run();
            }
        });
    }


    private static final int EmptyViewMarkID = Integer.MAX_VALUE - 523;


    @androidx.databinding.BindingAdapter({"markAsEmptyView"})
    public static void bindEmptyViewMark(View view, boolean value) {
        view.setTag(EmptyViewMarkID, value);
    }

    @androidx.databinding.BindingAdapter({"showEmptyView"})
    public static void bindEnableCommonEmptyView(com.demo.fishdemoxhb.quick.QuickEmptyView view, boolean value) {
        view.mShowEmptyView = value;
    }

}

