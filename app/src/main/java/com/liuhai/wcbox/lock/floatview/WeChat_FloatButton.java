package com.liuhai.wcbox.lock.floatview;


import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;

import com.liuhai.wcbox.R;
import com.liuhai.wcbox.lock.utils.MySettings;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by chenliangj2ee on 2017/5/14.
 * 快捷入口启动时展示的开屏
 */

public class WeChat_FloatButton {
    public int screenWidth;
    public int screenHeight;
    protected View rootView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private LayoutInflater mLayoutInflater;
    private LinearLayout mFloatView;
    private AccessibilityService con;


    public WeChat_FloatButton(AccessibilityService con1) {
        this.mWindowManager = (WindowManager) con1.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        this.screenHeight = mWindowManager.getDefaultDisplay().getHeight();
        this.screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        con=con1;
        show();
    }

    public void event(AccessibilityService service, AccessibilityEvent event) {

        this.con = service;
        Log.i("WeChat_FloatButton", "event.....");
        if (event.getPackageName() == null) {
            return;
        }

//        if(MainMenu!=null) {
//
//            MainMenu.event(con, event);
//        }
       // if (getLauncherPackageName(con).equals(event.getPackageName().toString())) {
        //    remove();
       // }
        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
        if (nodeInfo == null) {
            return;
        }
        moreMenu();
        List<AccessibilityNodeInfo> lists1 = nodeInfo.findAccessibilityNodeInfosByText("微信");
        List<AccessibilityNodeInfo> lists2 = nodeInfo.findAccessibilityNodeInfosByText("通讯录");
        List<AccessibilityNodeInfo> lists3 = nodeInfo.findAccessibilityNodeInfosByText("发现");
        List<AccessibilityNodeInfo> lists4 = nodeInfo.findAccessibilityNodeInfosByText("我");
        //if (lists1.size() > 0 &&lists2.size()>0&& lists3.size() > 0 && lists4.size() > 0) {
            show();
       // } else {
           // remove();
      //  }
        List<AccessibilityNodeInfo> titles = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/gz");//会话title
        if (titles != null && titles.size() > 0) {
          //  remove();
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


    public void init() {

        mWindowManager = (WindowManager) con.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mLayoutInflater = LayoutInflater.from(con);
        mFloatView = (LinearLayout) mLayoutInflater.inflate(R.layout.float_we_chat_floatbutton, null);
        initView(mFloatView);
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mLayoutParams.format = PixelFormat.RGBA_8888;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.width = 150;
        mLayoutParams.height = 150;
        x = screenWidth - 200;
        y = screenHeight / 2 - 150 / 2;
        mLayoutParams.x = x;
        mLayoutParams.y = y;
        Log.i("WeChat_FloatButton", "init.....");
    }


    boolean add = false;

    private void show() {
        if (MySettings.isWeixin(con) == false)
            return;
        if (add) {
            return;
        }
        if (mFloatView == null)
            init();
        if (mFloatView == null)
            return;
        mWindowManager.addView(mFloatView, mLayoutParams);
        add = true;
        MySettings.setAddFriend(con,false);
//        ObjectAnimator an = ObjectAnimator.ofFloat(mFloatView, "alpha", 0f, 1f);
//        an.setDuration(1000);
//        an.start();
        Log.i("WeChat_FloatButton", "show.....");
    }

    public void remove() {
        if (add == false)
            return;
        try {
            if (mWindowManager != null && mFloatView != null) {
                mWindowManager.removeView(mFloatView);
                add = false;
            }

            if (MainMenu != null)
                MainMenu.remove();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("WeChat_FloatButton", "remove.....");
    }

    private void log(String s) {
        Log.i(getClass().getSimpleName(), s);
    }


    private LinearLayout menu;
    private int lastX;
    private int lastY;
    private int x, y;
    long time2;

    private void initView(View rootView) {
        menu = (LinearLayout) rootView.findViewById(R.id.menu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySettings.setDeleteFriend(con, false);
                MySettings.setDeleteFriendAction(con,false);
                MySettings.setQunfa(con,false);
                MySettings.setAddFriend(con,false);
                MySettings.setZan(con,false);

                showMenu();
            }
        });


        menu.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int rawX = (int) motionEvent.getRawX();
                int rawY = (int) motionEvent.getRawY();
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    lastX = rawX;
                    lastY = rawY;
                    time2 = System.currentTimeMillis();
                }

                int offsetX = rawX - lastX;
                int offsetY = rawY - lastY;

                mLayoutParams.x = (int) (x + offsetX);
                mLayoutParams.y = (int) (y + offsetY);
//                mFloatView.setRotationX(offsetX);
                mWindowManager.updateViewLayout(mFloatView, mLayoutParams);

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    x = mLayoutParams.x;
                    y = mLayoutParams.y;
                    if (System.currentTimeMillis() - time2 < 100) {
                        menu.performClick();
                        MobclickAgent.onEvent(con,"click7");
                    }
                }
                return true;
            }
        });
    }

    WeChat_MainMenu MainMenu;

    public void showMenu() {
        if (MainMenu == null)
            MainMenu = new WeChat_MainMenu(con);

        MainMenu.show();
    }

    /**
     * 如果当前是自动检测删除好友，则自动点击右上角菜单
     */
    public void moreMenu(){
        if(MySettings.isDeleteFriend(con)==false)
            return;
        AccessibilityNodeInfo nodeInfo = con.getRootInActiveWindow();
        if(nodeInfo==null)
            return;
        List<AccessibilityNodeInfo> menus = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/iq");//获得左上角【+】按钮
        if (menus == null || menus.isEmpty())
            return;

        click(menus.get(0));
    }

    private void click(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo != null && nodeInfo.isClickable()) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            click(nodeInfo.getParent());
        }

    }
}
