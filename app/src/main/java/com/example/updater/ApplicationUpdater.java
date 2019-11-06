package com.example.updater;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Downloading new versionName of application.
 * <p>
 * 1, require okhttp
 * 2, okhttp need http configuration.
 */
public class ApplicationUpdater {

    private static final String TAG = ApplicationUpdater.class.getSimpleName();

    public Activity mActivity;

    private ApkInfoBean mApkInfo = ApkInfoBean.EMPTY;

    private ApkUpdateModel mModel;

    public ApplicationUpdater(Activity activity) {
        mActivity = activity;
        mModel = new ApkUpdateModel();
        try {
            PackageManager manager = mActivity.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mActivity.getPackageName(), 0);
            mApkInfo = ApkInfoBean.fromPackageInfo(info);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Checking new versionName of application.
     */
    public void checkNewVersion() {
        mModel.checkNewVersion(new ApkUpdateModel.ICallback() {
            @Override
            public void onData(final ApkInfoBean info) {
                Log.v(TAG, "onData -> " + info);
                if (info.bigger(mApkInfo)) {

                    UpdateConfirmDialog.showDialog(mActivity, info, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.e(TAG, "found new version, start download file ...");
                            File file = HttpUrlSimpleImpl.getNewFile(mActivity, info.downloadUrl);
                            boolean useDownloader = false;
                            if (useDownloader) {
                                DownloadManagerTool.newInstance(mActivity).downloadAPK(info.downloadUrl, file, new DownloadManagerTool.ICallback() {
                                    @Override
                                    public void onProgress(int progress, int total) {
                                    }

                                    @Override
                                    public void onFinish(File file) {
                                        InstallApkCompat.install(mActivity, file);
                                    }

                                    @Override
                                    public void onError(Throwable t) {

                                    }
                                });
                            } else {
                                HttpUrlSimpleImpl.downloadFileAsyn(info.downloadUrl, file, new HttpUrlSimpleImpl.ICallback() {
                                    @Override
                                    public void onProgress(int progress, int total) {
                                        Log.v(TAG, "onProgress -> " + progress + " total -> " + total);
                                    }

                                    @Override
                                    public void onFinish(File file) {
                                        Log.e(TAG, "onFinish");
                                        InstallApkCompat.install(mActivity, file);
                                    }

                                    @Override
                                    public void onError(Throwable t) {

                                    }
                                });
                            }
                        }
                    });
                } else {
                    Log.e(TAG, "no need to update ...");
                }
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
