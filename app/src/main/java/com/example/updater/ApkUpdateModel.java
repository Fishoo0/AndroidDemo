package com.example.updater;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


class ApkUpdateModel {

    static final String TAG = ApkUpdateModel.class.getSimpleName();

    public interface ICallback {

        void onData(ApkInfoBean bean);

        void onError(Throwable e);
    }

    private static final String API_URL = "http://10.145.110.69:5000/setting/get_latest_version";


    private final static Handler UiHandler = new Handler(Looper.getMainLooper());


    public ApkUpdateModel() {

    }

    /**
     * Checking new versionName of application.
     */
    public void checkNewVersion(final ICallback callback) {
        HttpUrlSimpleImpl.getAync(API_URL, new HttpUrlSimpleImpl.IRequestCallback() {
            @Override
            public void onFinish(String value) {
                Exception exception = null;
                Log.e(TAG, "response -> " + value);
                try {
                    JSONObject jsonObject = new JSONObject(value);
                    JSONObject data = jsonObject.getJSONObject("data");
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        Log.e(TAG, "data -> " + data);
                        if (data != null) {
                            final ApkInfoBean info = ApkInfoBean.fromJsonData(data);
                            UiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onData(info);
                                }
                            });
                            return;
                        }
                    }
                    exception = new Exception("返回错误：" + value);
                } catch (JSONException e) {
                    e.printStackTrace();
                    exception = e;
                }
                onError(exception);
            }

            @Override
            public void onError(final Throwable t) {
                UiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError(t);
                    }
                });
            }
        });
    }


}
