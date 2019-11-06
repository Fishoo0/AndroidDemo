package com.example.updater;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import java.io.File;


/**
 * App compat
 */
public class InstallApkCompat {

    public static int REQUEST_CODE = 278;

    private static final Handler UIHandler = new Handler();

    private static File mToInstallFile;

    /**
     * Install application.
     *
     * @param context
     * @param file
     */
    public static void install(Activity context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startInstallO(context, file);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            startInstallN(context, file);
        } else {
            startInstall(context, file);
        }
    }

    /**
     * android1.x-6.x
     */
    private static void startInstall(Activity context, File file) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setDataAndType(Uri.parse("file://" + file.getPath()), "application/vnd.android.package-archive");
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(install);
    }

    /**
     * android7.x
     */
    private static void startInstallN(Activity context, File file) {
        //参数1 上下文, 参数2 在AndroidManifest中的android:authorities值, 参数3  共享的文件
        Uri apkUri = forUri(context, file);
        Intent install = new Intent(Intent.ACTION_VIEW);
        //由于没有在Activity环境下启动Activity,设置下面的标签
        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        install.setDataAndType(apkUri, "application/vnd.android.package-archive");
        context.startActivity(install);
    }

    /**
     * android8.x
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void startInstallO(Activity context, File file) {
        mToInstallFile = file;
        boolean isGranted = checkInstall0Permission(context);
        if (isGranted) {
            startInstallN(context, file);
        }
    }


    public static boolean checkInstall0Permission(final Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean isGranted = context.getPackageManager().canRequestPackageInstalls();
            if (!isGranted) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                intent.setData(Uri.parse("package:" + context.getApplication().getPackageName()));
                context.startActivityForResult(intent, REQUEST_CODE);
                UIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "安装应用需要打开未知来源权限，请同意", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return isGranted;
        }
        return true;
    }

    /**
     * Activity for result.
     *
     * @param context
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public static void onActivityResult(Activity context, int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                boolean isGranted = context.getPackageManager().canRequestPackageInstalls();
                if (!isGranted) {
                    Toast.makeText(context, "安装应用需要打开未知来源权限，请同意", Toast.LENGTH_SHORT).show();
                } else {
                    if (mToInstallFile != null) {
                        install(context, mToInstallFile);
                    }
                }
            }
        }
    }


    /**
     * Build uri from file.
     *
     * @param context
     * @param file
     * @return
     */
    public static Uri forUri(Context context, File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //设置7.0以上共享文件，分享路径定义在xml/file_paths.xml
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".UpdaterFileProvider", file);
        } else {
            // 7.0以下,共享文件
            uri = Uri.fromFile(file);
        }
        return uri;
    }

}
