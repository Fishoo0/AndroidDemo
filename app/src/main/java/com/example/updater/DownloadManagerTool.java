package com.example.updater;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * DownloadManager tool ...
 */
public class DownloadManagerTool {


    static final String TAG = DownloadManagerTool.class.getSimpleName();

    private DownloadManager mDownloadManager;
    private Context mContext;
    private long mDownloadId;

    private DownloadManagerTool(Context context) {
        this.mContext = context;
        mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    /**
     * New instance.
     *
     * @param context
     * @return
     */
    public static DownloadManagerTool newInstance(Context context) {
        return new DownloadManagerTool(context);
    }


    public interface ICallback {

        /**
         * Update progress.
         *
         * @param progress 0 -> onStart; progress == total -> onFinished()
         * @param total
         */
        void onProgress(int progress, int total);


        /**
         * On finished.
         *
         * @param file
         */
        void onFinish(File file);

        /**
         * Something happened.
         *
         * @param t
         */
        void onError(Throwable t);
    }

    //下载apk
    public void downloadAPK(String url, final File file, final ICallback callback) {
        if (mDownloadId != 0) {
            throw new IllegalStateException("The DownloadManager has been used! Call #newInstance(Context)");
        }
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedOverRoaming(false);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle("下载更新APK");
        request.setDescription("文件下载中 ...");
        request.setVisibleInDownloadsUi(true);
        request.setDestinationUri(Uri.fromFile(file));
        mDownloadId = mDownloadManager.enqueue(request);
        mContext.registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(mDownloadId);
                Cursor cursor = mDownloadManager.query(query);
                if (cursor.moveToFirst()) {
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    Log.e(TAG, "status -> " + status);
                    switch (status) {
                        case DownloadManager.STATUS_PAUSED:
                            break;
                        case DownloadManager.STATUS_PENDING:
                            break;
                        case DownloadManager.STATUS_RUNNING:
                            break;
                        case DownloadManager.STATUS_SUCCESSFUL:
                            cursor.close();
                            callback.onFinish(file);
                            break;
                        case DownloadManager.STATUS_FAILED:
                            Toast.makeText(mContext, "STATUS_FAILED", Toast.LENGTH_SHORT).show();
                            cursor.close();
                            mContext.unregisterReceiver(this);
                            callback.onError(new Exception("Download Failed."));
                            break;
                    }
                }
            }
        }, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


}
