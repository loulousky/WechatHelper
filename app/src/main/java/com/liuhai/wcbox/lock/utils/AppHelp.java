package com.liuhai.wcbox.lock.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.File;

public class AppHelp {

    public static String GT_CDN_AD_URL = "";


    public static String getAppKey(Context context) {
        String appKey = null;
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            appKey = appInfo.metaData.getString("BONTAI_MOBIADS_APP_KEY");
        } catch (Exception e) {

        }

        return appKey;
    }

    public static String getBontaiMobiAdsUrl(Context context) {

        String bontaiMobiAdsUrl = null;
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            bontaiMobiAdsUrl = appInfo.metaData.getString("BONTAI_MOBIADS_URL");
        } catch (Exception e) {
        }

        return bontaiMobiAdsUrl;
    }

    /**
     * 检测网络是否连接
     *
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        // 得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            return manager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }


    public static void installAPK(Context context, File saveLocation, String filename) {
        Uri uri = Uri.fromFile(new File(saveLocation, filename));
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installIntent.addCategory("android.intent.category.DEFAULT");
        installIntent.setDataAndType(uri, "application/vnd.android.package-archive");

        context.startActivity(installIntent);
    }

    public static void installDialogInfo(final Context context, final File saveLocation, final String filename) {
        //先new出一个监听器，设置好监听
        DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:

                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        AppHelp.installAPK(context, saveLocation, filename);
                        break;

                }
            }
        };
        //dialog参数设置
        AlertDialog.Builder builder = new AlertDialog.Builder(context);  //先得到构造器
        builder.setTitle("安装"); //设置标题
        builder.setMessage("应用已经下载是否现在安装?"); //设置内容

        builder.setPositiveButton("否", dialogOnclicListener);
        builder.setNegativeButton("是", dialogOnclicListener);

        builder.create().show();
    }


    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

}
