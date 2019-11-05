package com.example.updater;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * File downloader.
 */
final class HttpUrlSimpleImpl {

    private static final String TAG = HttpUrlSimpleImpl.class.getSimpleName();

    private static final Handler UiHandler = new Handler(Looper.getMainLooper());


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


    public interface IRequestCallback {

        /**
         * On finished.
         *
         * @param value
         */
        void onFinish(String value);

        /**
         * Something happened.
         *
         * @param t
         */
        void onError(Throwable t);
    }


    /**
     * Get new file.
     *
     * @param context
     * @param url
     * @return
     */
    public static File getNewFile(Context context, String url) {
        @SuppressLint({"NewApi", "LocalSuppress"}) File file = new File(context.getExternalCacheDir(), (url.hashCode()) + ".apk");
        if (!file.isDirectory()) {
            file.deleteOnExit();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    /**
     * Download file
     *
     * @param downloadUrl
     * @param file
     * @param callback
     */
    public static final void downloadFileAsyn(final String downloadUrl, final File file, final ICallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                downloadFileSync(downloadUrl, file, new ICallback() {
                    @Override
                    public void onProgress(final int progress, final int total) {
                        if (callback != null) {
                            UiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onProgress(progress, total);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFinish(final File file) {
                        if (callback != null) {
                            UiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFinish(file);
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(final Throwable t) {
                        if (callback != null) {
                            UiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onError(t);
                                }
                            });
                        }
                    }
                });
            }
        }).start();
    }


    /**
     * Download file.
     *
     * @param downloadUrl
     * @param file
     */
    public static final void downloadFileSync(String downloadUrl, final File file, final ICallback callback) {
        Log.e(TAG, "downloadFile -> " + downloadUrl + ", file -> " + file);
        ICallback internalCallback = new ICallback() {
            @Override
            public void onProgress(int progress, int total) {
                if (callback != null) {
                    callback.onProgress(progress, total);
                }
            }

            @Override
            public void onFinish(File file) {
                if (callback != null) {
                    callback.onFinish(file);
                }
            }

            @Override
            public void onError(Throwable t) {
                file.deleteOnExit();
                if (callback != null) {
                    callback.onError(t);
                }
            }
        };

        FileOutputStream fileOutputStream;//文件输出流
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            internalCallback.onError(e);
            return;
        }

        InputStream inputStream = null;//文件输入流
        try {
            URL url = new URL(downloadUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int fileLength = Integer.valueOf(connection.getHeaderField("Content-Length"));
            inputStream = connection.getInputStream();
            int respondCode = connection.getResponseCode();
            int downloadLength = 0;
            internalCallback.onProgress(0, fileLength);
            if (respondCode == 200) {
                byte[] buffer = new byte[1024 * 8];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, len);
                    downloadLength = downloadLength + len;
                    Log.d(TAG, downloadLength + "/" + fileLength);
                    internalCallback.onProgress(downloadLength, fileLength);
                    if (downloadLength == fileLength) {
                        internalCallback.onFinish(file);
                    }
                }
            } else {
                Log.e(TAG, "respondCode -> " + respondCode);
                internalCallback.onError(new Exception("Invalid responseCode -> " + respondCode));
            }
        } catch (Exception e) {
            e.printStackTrace();
            internalCallback.onError(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void getAync(final String apiUrl, final IRequestCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getSync(apiUrl, new IRequestCallback() {
                    @Override
                    public void onFinish(final String value) {
                        if (callback != null) {
                            UiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFinish(value);
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(final Throwable t) {
                        if (callback != null) {
                            UiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onError(t);
                                }
                            });
                        }
                    }
                });
            }
        }).start();

    }


    /**
     * Getting callback.
     *
     * @param apiUrl
     */
    public static void getSync(String apiUrl, IRequestCallback callback) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                String result = stringBuilder.toString();
                callback.onFinish(result);
            } else {
                callback.onError(new Exception("Invalid responseCode: " + responseCode));
            }
        } catch (IOException e) {
            e.printStackTrace();
            callback.onError(e);
        }
    }


    /**
     * Post data
     *
     * @param apiUrl
     * @param callback
     */
    public static void postAsyn(String apiUrl, IRequestCallback callback) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");//设置请求方式为POST
            connection.setDoOutput(true);//允许写出
            connection.setDoInput(true);//允许读入
            connection.setUseCaches(false);//不使用缓存
            connection.connect();//连接
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                String result = stringBuilder.toString();//将流转换为字符串。
                callback.onFinish(result);
            } else {
                callback.onError(new Exception("Invalid responseCode: " + responseCode));
            }
        } catch (IOException e) {
            e.printStackTrace();
            callback.onError(e);
        }
    }
}