package com.liuhai.wcbox.lock.service.utils;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.liuhai.wcbox.lock.BeanApp;
import com.liuhai.wcbox.lock.FloatSplashActivity;
import com.liuhai.wcbox.lock.ads.AdsSplash_YouMi;
import com.liuhai.wcbox.lock.utils.MySettings;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chenliangj2ee on 2017/5/21.
 * 第三方app开屏行为控制器
 */

public class SplashUtils {
    AccessibilityService service;
    AdsSplash_YouMi splash;
    HashMap<String, BeanApp> appsClassNames = new HashMap<>();
    private String filterAppName = "微信铁路12306微信";

    private String launcher = "";

    public void event(AccessibilityService service, AccessibilityEvent event) {
        if (appsClassNames.isEmpty()) {
            queryAppInfo(service);
        }

        if ("".equals(launcher)) {
            launcher = getLauncherPackageName(service);
        }
        this.service = service;

        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {//监听桌面APP点击事件
            if (launcher.equals(event.getPackageName().toString())) {
                BeanApp app = appsClassNames.get(event.getText().toString());
                if (app != null) {
                    openSplash(service, app);
                }
            }
        }
    }


    private void openSplash(AccessibilityService service, BeanApp app) {
//        if (splash == null) {
//            splash = new AdsSplash_YouMi(service);
//        }
//
//        splash.show(app);
        if (MySettings.isAdsShowTimeOut(service)) {
            Intent intent = new Intent(service, FloatSplashActivity.class);
            intent.setFlags(Intent. FLAG_ACTIVITY_NEW_TASK);
            service.startActivity(intent);
        }



    }

    private void closeSplash() {
//        splash.remove();
    }

    private void log(String msg) {
        Log.i("SplashUtils", msg);
    }


    // 获得所有启动Activity的信息，类似于Launch界面
    public void queryAppInfo(AccessibilityService service) {
        appsClassNames.clear();
        PackageManager pm = service.getPackageManager(); // 获得PackageManager对象
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 通过查询，获得所有ResolveInfo对象.
        List<ResolveInfo> resolveInfos = pm
                .queryIntentActivities(mainIntent, PackageManager.MATCH_DISABLED_UNTIL_USED_COMPONENTS);
        // 调用系统排序 ， 根据name排序
        // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));
        for (ResolveInfo reInfo : resolveInfos) {

            String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
            String appLabel = "[" + (String) reInfo.loadLabel(pm) + "]"; // 获得应用程序的Label
            String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名

            if (filterAppName.contains(appLabel.replace("[", "").replace("]", "")))
                continue;

            Drawable icon = reInfo.loadIcon(pm); // 获得应用程序图标
            BeanApp app = new BeanApp();
            app.setIcon(icon);
            app.setTitle("----" + appLabel.replace("[", "").replace("]", "") + "----");

            ApplicationInfo appinfo = null;
            try {
                appinfo = pm.getApplicationInfo(pkgName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if ((appinfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                appsClassNames.put(appLabel, app);
            }


        }

    }

    public String getLauncherPackageName(Context context) {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            return "";
        }
        //如果是不同桌面主题，可能会出现某些问题，这部分暂未处理
        if (res.activityInfo.packageName.equals("android")) {
            return "";
        } else {
            return res.activityInfo.packageName;
        }


    }

}
