package com.liuhai.wcbox.lock.act;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.liuhai.wcbox.R;
import com.liuhai.wcbox.lock.action.WeChat_QunFa2;
import com.liuhai.wcbox.lock.floatview.FloatPrctureCutView;
import com.liuhai.wcbox.lock.floatview.WeChat_FloatScreenRecorder;
import com.liuhai.wcbox.lock.service.MyActionService;
import com.liuhai.wcbox.lock.utils.DeviceUtils;
import com.liuhai.wcbox.lock.utils.MySettings;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class MainTabActivity extends Activity implements View.OnClickListener {

    private Button action;
    protected Button weixin;
    private String from = "";
    private String messages = "最近很火的一款微信插件App(安卓)，检测朋友删除，自动回复等强大功能，推荐给你，试试看吧，下载地址(微信内在浏览器打开)：//m.eqxiu.com/s/eRwskNRe";

    private boolean flag=true;

    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main_menu);
        from = getIntent().getStringExtra("from");

        initView();
        youmeng();

        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermission();
        }
        Log.i("MainTabActivity", DeviceUtils.getIMEI(this));
    }

    public void close(View view) {
        finish();
    }

    public void action01(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void action02(View view) {

        if (checkAlertWindowsPermission()) {
            FloatPrctureCutView view1 = new FloatPrctureCutView(this);
            view1.show();
            MobclickAgent.onEvent(this, "click1");
        } else {
            Toast.makeText(this, "请先开启悬浮窗权限", Toast.LENGTH_SHORT).show();
        }


    }

    public void action03(View view) {

        if (checkAlertWindowsPermission()) {
            WeChat_FloatScreenRecorder floatScreenRecorder = new WeChat_FloatScreenRecorder(this);
            floatScreenRecorder.show();
            MobclickAgent.onEvent(this, "click2");
            finish();
        } else {
            Toast.makeText(this, "请先开启悬浮窗权限", Toast.LENGTH_SHORT).show();
        }
    }

    public void action04(View view) {

        if(flag) {
            if (!MySettings.isShareWeChat(this) && "wechat".equals(from)) {
                WeChat_QunFa2.message = messages;
                MySettings.setQunfa(this, true);
                //   } else {
                //        MobclickAgent.onEvent(this, "click3");
                       Intent textIntent = new Intent(Intent.ACTION_SEND);
                       textIntent.setType("text/plain");
                      textIntent.putExtra(Intent.EXTRA_TEXT, messages);
                       startActivity(Intent.createChooser(textIntent, "分享"));
            }
        }else{

            //传输到webview内页webview显示
                Intent intent=new Intent(this,WebViewActivity.class);
                Log.d("url",url);
                intent.putExtra("url",url);
                startActivity(intent);



        }

    }

    public String getVersion(Context context, String packageName) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(packageName, 0);
            return info.versionName;
        } catch (Exception e) {

        }
        return "";
    }

    public int getVersionCode(Context context, String packageName) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(packageName, 0);
            return info.versionCode;
        } catch (Exception e) {

        }
        return 0;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            Rect rectangle = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
            Log.i("rectangle", "rectangle.top" + rectangle.top);
            if (rectangle.top > 0)
                getSharedPreferences("LockScreed", 0).edit().putInt("actionBarHeight", rectangle.top).commit();

        }
        super.onWindowFocusChanged(hasFocus);
    }


    public static int getTop(Context con) {
        int top = con.getSharedPreferences("LockScreed", 0).getInt("actionBarHeight", 65);
        if (top <= 0)
            top = 65;
        return top;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.weixin) {
            if (checkAlertWindowsPermission() && checkStealFeature1()) {

                // if (getVersion(this, getPackageName()).equals(getVersion(this, "com.tencent.mm"))) {
                MySettings.setWeixin(this, !MySettings.isWeixin(this));
                //     if (MySettings.isWeixin(this)) {
                weixin.setBackgroundResource(R.drawable.weixinlogo_pre);
                Toast.makeText(this, "微信插件已开启", Toast.LENGTH_SHORT).show();
                MobclickAgent.onEvent(this, "开启微信插件");
                //        } else {
                //             weixin.setBackgroundResource(R.drawable.weixinlogo_def);
                //             Toast.makeText(this, "微信插件已关闭", Toast.LENGTH_SHORT).show();
                //ßMobclickAgent.onEvent(this, "关闭微信插件");
            }
//                } else {
//
//                    startActivity(new Intent(this, InfoActivity.class));
//                }


        } else {
            startActivity(new Intent(this, SettingsActivity.class));
            Toast.makeText(this, "请开启必要的权限", Toast.LENGTH_SHORT).show();
        }
        // }\
    }

    private void initView() {
        weixin = (Button) findViewById(R.id.weixin);
        action = (Button) findViewById(R.id.action4);
        weixin.setOnClickListener(MainTabActivity.this);
        loadAd();
    }





    //加载广告
    private void loadAd(){
        OkGo.<String>get("http://hd215.api.okayapi.com/?s=App.Main_Meta.Get&app_key=D61B3F33E3AED3D2EC8267769BA890BF&key=wechat1")
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        action.setText("点击领钱");
                        flag=false;
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                           url= jsonObject.getJSONObject("data").getJSONArray("data").getJSONObject(0).getString("url");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        Log.d("url",response.body());

                    }
                });



    }




    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("主页");


    }

    private void youmeng() {
        MobclickAgent.setDebugMode(true);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

//
//        if (!getVersion(this, getPackageName()).equals(getVersion(this, "com.tencent.mm"))) {
//            MySettings.setWeixin(this, false);
//        } else {
//
//        }


//        if (MySettings.isWeixin(this) && checkAlertWindowsPermission() && checkStealFeature1()) {
//            weixin.setBackgroundResource(R.drawable.weixinlogo_pre);
//        } else {
//            weixin.setBackgroundResource(R.drawable.weixinlogo_def);
//        }


    }


    private boolean checkAlertWindowsPermission() {
        try {
            Object object = getSystemService(Context.APP_OPS_SERVICE);
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
            arrayOfObject1[2] = getPackageName();
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

    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();
        if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (lackedPermission.size() == 0) {

        } else {
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {

        } else {
            Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
            finish();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            finish();
        }
    }


//    @Override
//    public void onBackPressed() {
//        if ("wechat".equals(from) == false) {
//            Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
//            mHomeIntent.addCategory(Intent.CATEGORY_HOME);
//            mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//            startActivity(mHomeIntent);
//
//        }
//    }
}