package com.liuhai.wcbox.lock.act;

import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

import com.liuhai.wcbox.R;
import com.liuhai.wcbox.lock.service.MyActionService;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Method;


public class SettingsActivity extends PreferenceActivity implements
        Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
    public PreferenceScreen item01;
    public PreferenceScreen item02;
    public PreferenceScreen item03;

    public SwitchPreference item04;
    public SwitchPreference item06;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.activity_settings);

        item01 = (PreferenceScreen) findPreference("第1步：启用桌面悬浮框");
        item02 = (PreferenceScreen) findPreference("第2步：允许后台运行");
        item03 = (PreferenceScreen) findPreference("第3步：开启无障碍服务");

        item04 = (SwitchPreference) findPreference("桌面悬浮框");
        item06 = (SwitchPreference) findPreference("无障碍服务");

        item01.setOnPreferenceClickListener(this);
        item02.setOnPreferenceClickListener(this);
        item03.setOnPreferenceClickListener(this);
        item04.setOnPreferenceClickListener(this);
        item06.setOnPreferenceClickListener(this);

        item04.setOnPreferenceChangeListener(this);
        item06.setOnPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean bool = checkAlertWindowsPermission(this);
        if (bool) {
            item04.setChecked(true);
        } else {
            item04.setChecked(false);
        }

        boolean boo2 = checkStealFeature1();
        if (boo2) {
            item06.setChecked(true);
        } else {
            item06.setChecked(false);
        }



    }

    public String getVersion(Context context,String packageName) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(packageName, 0);
            return   info.versionName;
        } catch (Exception e) {

        }
        return "" ;
    }
    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == item01) {
            Intent in = new Intent(this, YanshiActivity.class);
            in.putExtra("index", 0);
            startActivity(in);
        } else if (preference == item02) {
            Intent in = new Intent(this, YanshiActivity.class);
            in.putExtra("index", 1);
            startActivity(in);
        } else if (preference == item03) {
            Intent in = new Intent(this, YanshiActivity.class);
            in.putExtra("index", 2);
            startActivity(in);
        }

        return false;
    }


    public boolean checkAlertWindowsPermission(Context context) {
        try {
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = 24;
            arrayOfObject1[1] = Binder.getCallingUid();
            arrayOfObject1[2] = context.getPackageName();
            int m = ((Integer) method.invoke(object, arrayOfObject1));
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {

        }
        return false;
    }


    private boolean checkStealFeature1() {
        String service = getPackageName() + "/" + MyActionService.class.getCanonicalName();
        int ok = 0;
        try {
            ok = Settings.Secure.getInt(getApplicationContext().getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
        }
        TextUtils.SimpleStringSplitter ms = new TextUtils.SimpleStringSplitter(':');
        if (ok == 1) {
            String settingValue = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                ms.setString(settingValue);
                while (ms.hasNext()) {
                    String accessibilityService = ms.next();
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }


        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        if (preference == item04) {
            MobclickAgent.onEvent(this,"click4");
            Intent localIntent = null;
            try {
                localIntent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
                localIntent.addCategory(Intent.CATEGORY_DEFAULT);
                localIntent.putExtra("packageName", getPackageName());
                startActivity(localIntent);
            } catch (Exception e1) {


                try {
                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
                    intent.setComponent(comp);
                    startActivity(intent);

                } catch (Exception e2) {
                    try {
                        Intent i = new Intent("miui.intent.action.APP_PERM_EDITOR");
                        ComponentName componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                        i.setComponent(componentName);
                        i.putExtra("extra_pkgname", getPackageName());
                        startActivity(i);

                    } catch (Exception e3) {


                        try {

                            Intent intent = new Intent();
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("packageName", getPackageName());
                            ComponentName comp = new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity");
                            intent.setComponent(comp);
                            startActivity(intent);

                        } catch (Exception e4) {

                            try {

                                Intent intent = new Intent();
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("packageName", getPackageName());
                                ComponentName comp = new ComponentName("com.color.safecenter", "com.color.safecenter.permission.PermissionManagerActivity");
                                intent.setComponent(comp);
                                startActivity(intent);


                            } catch (Exception e5) {
                                try {

                                    Intent intent = new Intent("android.intent.action.MAIN");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("packageName", getPackageName());
                                    ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
                                    intent.setComponent(comp);
                                    startActivity(intent);


                                } catch (Exception e6) {
                                    try {
                                        Intent intent = new Intent();
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("packageName", getPackageName());
                                        ComponentName comp = new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.PermissionAndApps");
                                        intent.setComponent(comp);
                                    } catch (Exception e7) {


                                        try {
                                            Intent intent = new Intent("android.intent.action.MAIN");
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.putExtra("packageName", getPackageName());
                                            ComponentName comp = new ComponentName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity");
                                            intent.setComponent(comp);
                                            startActivity(intent);

                                        } catch (Exception e8) {
                                            try {
                                                if (Build.VERSION.SDK_INT >= 9) {
                                                    localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                                    localIntent.setData(Uri.fromParts("package", getPackageName(), null));
                                                } else if (Build.VERSION.SDK_INT <= 8) {
                                                    localIntent.setAction(Intent.ACTION_VIEW);
                                                    localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                                                    localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
                                                }
                                                startActivity(localIntent);

                                            } catch (Exception e9) {

                                            }
                                        }

                                    }
                                }
                            }
                        }


                    }
                }


            }
            Toast.makeText(this, "找到【桌面悬浮框】，选中启用", Toast.LENGTH_SHORT).show();

        } else if (preference == item06) {
            MobclickAgent.onEvent(this,"click5");
            Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, "第一步:找到【无障碍】", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "第二步:找到【微商盒子】，选中启用", Toast.LENGTH_SHORT).show();
        }

        return false;
    }
}