package com.example.updater;

import android.content.pm.PackageInfo;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Managing apk information.
 */
final class ApkInfoBean {

    private static final String TAG = ApkInfoBean.class.getSimpleName();

    public static ApkInfoBean EMPTY = new ApkInfoBean();

    public String packageInfo = "";
    public String versionName = "";
    public long versionCode = 0;
    public String downloadUrl = "";
    public String description = "";
    public boolean forceUpdate = false;

    /**
     * Bigger than info ??
     *
     * @param info
     * @return
     */
    public boolean bigger(ApkInfoBean info) {
        Log.v(TAG, "bigger current -> " + this + ", target -> " + info);
//            if (!packageInfo.equals(info.packageInfo)) {
//                return false;
//            }

        if (!versionName.equals(info.versionName)) {
            return false;
        }
        if (versionCode > info.versionCode) {
            return true;
        }
        return false;
    }


    @Override
    public String toString() {
        return "[packageInfo:" + packageInfo + ", versionName:" + versionName + ", versionCode:" + versionCode + ", downloadUrl:" + downloadUrl + "]";
    }

    public static ApkInfoBean fromPackageInfo(PackageInfo info) {
        ApkInfoBean data = new ApkInfoBean();
        data.packageInfo = info.packageName;
        data.versionName = info.versionName;
        data.versionCode = info.versionCode;
        return data;
    }

    public static ApkInfoBean fromUrl(String url) {
        ApkInfoBean data = new ApkInfoBean();
        return data;
    }

    public static ApkInfoBean fromJsonData(JSONObject jsonObject) {
        ApkInfoBean data = new ApkInfoBean();
        try {
            data.versionName = jsonObject.getString("versionName");
            data.versionCode = jsonObject.getLong("versionCode");
            data.downloadUrl = jsonObject.getString("downloadUrl");
            data.description = jsonObject.getString("description");
            data.forceUpdate = jsonObject.getBoolean("forceUpdate");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

}