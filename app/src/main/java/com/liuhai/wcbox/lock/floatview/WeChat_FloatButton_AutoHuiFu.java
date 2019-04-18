package com.liuhai.wcbox.lock.floatview;


import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liuhai.wcbox.R;
import com.liuhai.wcbox.lock.utils.MySettings;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by chenliangj2ee on 2017/5/14.
 * 快捷入口启动时展示的开屏
 */

public class WeChat_FloatButton_AutoHuiFu {
    public int screenWidth;
    public int screenHeight;
    protected View rootView;
    protected TextView background;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private LayoutInflater mLayoutInflater;
    private LinearLayout mFloatView;
    private AccessibilityService con;
    private String title;
    private String type = "";


    public WeChat_FloatButton_AutoHuiFu(AccessibilityService con) {
        this.mWindowManager = (WindowManager) con.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        this.screenHeight = mWindowManager.getDefaultDisplay().getHeight();
        this.screenWidth = mWindowManager.getDefaultDisplay().getWidth();
    }

    public void event(AccessibilityService service, AccessibilityEvent event) {

        this.con = service;
        if (event.getPackageName() == null) {
            return;
        }

        if (getLauncherPackageName(con).equals(event.getPackageName().toString())) {
            remove();
        }
        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
        if (nodeInfo == null) {
            return;
        }

        List<AccessibilityNodeInfo> titles = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/gz");//会话title
        if (titles != null && titles.size() > 0 && !titles.get(0).getText().toString().contains("(") && !titles.get(0).getText().toString().contains(")")) {
            title = titles.get(0).getText().toString();
            show();
            initBg();

        } else {
            remove();

        }


    }

    public String getLauncherPackageName(Context context) {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            return "";
        }
        if (res.activityInfo.packageName.equals("android")) {
            return "";
        } else {
            return res.activityInfo.packageName;
        }


    }

    int TabHeight = -1;
    Rect rect;

    public void init() {

        if (rect == null && con != null) {
            List<AccessibilityNodeInfo> lists = con.getRootInActiveWindow().findAccessibilityNodeInfosByText("通讯录");
            if (lists.isEmpty())
                return;
            AccessibilityNodeInfo bottominfo = lists.get(0).getParent().getParent().getParent();//.getParent();
            rect = new Rect();
            bottominfo.getBoundsInScreen(rect);
            TabHeight = rect.bottom - rect.top;//微信底部Tab高度
        }
        mWindowManager = (WindowManager) con.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mLayoutInflater = LayoutInflater.from(con);
        mFloatView = (LinearLayout) mLayoutInflater.inflate(R.layout.float_we_chat_floatbutton_jiqiren, null);
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mLayoutParams.format = PixelFormat.RGBA_8888;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.width = 95;
        mLayoutParams.height = 95;
        mLayoutParams.x = screenWidth - 250;
        mLayoutParams.y = 25;
        initView(mFloatView);
        Log.i("AutoHuiFu", "init.....");
    }


    boolean add = false;

    private void show() {
        if (MySettings.isWeixin(con) == false)
            return;

        long start = System.currentTimeMillis();
        if (add)
            return;
        if (mFloatView == null)
            init();
        if (mFloatView == null)
            return;
        mWindowManager.addView(mFloatView, mLayoutParams);
        add = true;
        mFloatView.postInvalidate();


        long end = System.currentTimeMillis();
        Log.i("AutoHuiFu", "show....." + (end - start));
    }

    public void remove() {
        if (add == false)
            return;
        try {
            if (mWindowManager != null && mFloatView != null) {
                mWindowManager.removeView(mFloatView);
                add = false;
            }
        } catch (Exception e) {

        }
        Log.i("AutoHuiFu", "remove.....");
    }

    private void log(String s) {
        Log.i(getClass().getSimpleName(), s);
    }


    private LinearLayout menu;

    private void initView(View rootView) {
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySettings.setJiQiRen(con, title, !MySettings.isJiQiRen(con, title));

                if (MySettings.isJiQiRen(con, title)) {
                    Toast.makeText(con, "已开启机器人回复", Toast.LENGTH_SHORT).show();
                    Toast.makeText(con, "机器人回复，仅在手机不锁屏情况下生效", Toast.LENGTH_LONG).show();
                    Toast.makeText(con, "机器人回复，仅在手机不锁屏情况下生效", Toast.LENGTH_LONG).show();
                    Toast.makeText(con, "机器人回复，仅在手机不锁屏情况下生效", Toast.LENGTH_LONG).show();
                    MobclickAgent.onEvent(con,"click8");
                } else {
                    Toast.makeText(con, "已关闭机器人回复", Toast.LENGTH_SHORT).show();
                    MobclickAgent.onEvent(con,"click9");
                }
                initBg();
            }
        });
        background = (TextView) rootView.findViewById(R.id.background);
        initBg();
    }


    private void initBg() {
        if (background == null)
            return;
        if (MySettings.isJiQiRen(con, title)) {
            background.setBackgroundResource(R.drawable.jiqiren_pre);
        } else {
            background.setBackgroundResource(R.drawable.jiqiren_def);
        }
    }

    public void showMenu() {

    }
}
