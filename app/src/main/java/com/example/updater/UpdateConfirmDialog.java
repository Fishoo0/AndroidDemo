package com.example.updater;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class UpdateConfirmDialog {

    /**
     * Show update dialog.
     *
     * @param bean
     */
    public static final void showDialog(Context context, ApkInfoBean bean, final DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("监测到更新" + (bean.forceUpdate ? "(本版本强制更新)" : ""))
                .setMessage(bean.description);
        if (!bean.forceUpdate) {
            builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.setNegativeButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onClick(dialog, which);
            }
        });
        Dialog dialog = builder.show();
        dialog.setCanceledOnTouchOutside(false);//可选
        dialog.setCancelable(false);//可选
    }
}
