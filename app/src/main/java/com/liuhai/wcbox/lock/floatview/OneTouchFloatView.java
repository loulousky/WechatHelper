package com.liuhai.wcbox.lock.floatview;


import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.liuhai.wcbox.R;
import com.liuhai.wcbox.lock.act.MainTabActivity;

import java.util.ArrayList;

/**
 * Created by chenliangj2ee on 2017/5/14.
 * 快捷入口启动时展示的开屏
 */

public class OneTouchFloatView {
    public int screenWidth;
    public int screenHeight;
    protected View rootView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private WindowManager.LayoutParams mLayoutParams2;
    private WindowManager.LayoutParams mLayoutParams3;
    private LayoutInflater mLayoutInflater;
    private LinearLayout mFloatView;
    private LinearLayout mFloatView2;
    private LinearLayout mFloatView3;
    private AccessibilityService con;

    private boolean more;
    private int locationX, locationY;

    private ArrayList<AccessibilityNodeInfo> nodeInfos = new ArrayList<AccessibilityNodeInfo>();
    private int nodesNum;

    public OneTouchFloatView(AccessibilityService con) {
        this.con = con;
        this.mWindowManager = (WindowManager) con.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        this.screenHeight = mWindowManager.getDefaultDisplay().getHeight();
        this.screenWidth = mWindowManager.getDefaultDisplay().getWidth();
    }

    AccessibilityNodeInfo nodeInfo;

    public void event(AccessibilityService service, AccessibilityEvent event) {

        this.con = service;
        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
        if (nodeInfo == null) {
            return;
        }
        this.nodeInfo = nodeInfo;

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED || event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            nodeInfos.clear();
            nodesNum = 0;
            getClickNode(nodeInfo);
        }


    }

    private void getClickNode(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null)
            return;

        try {

            if (nodeInfo.isClickable() && nodeInfo.toString().contains("ACTION_CLICK")) {
                nodeInfos.add(nodeInfo);
                nodesNum++;
            }
            if (nodeInfo.getChildCount() > 0) {
                for (int i = 0; i < nodeInfo.getChildCount(); i++) {
                    getClickNode(nodeInfo.getChild(i));
                }
            }

        } catch (Exception e) {

        }

    }


    public void init() {

        mWindowManager = (WindowManager) con.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mLayoutInflater = LayoutInflater.from(con);
        mFloatView = (LinearLayout) mLayoutInflater.inflate(R.layout.float_touch_menu, null);
        mFloatView2 = (LinearLayout) mLayoutInflater.inflate(R.layout.float_touch, null);
        mFloatView3 = (LinearLayout) mLayoutInflater.inflate(R.layout.float_rect, null);
        initView(mFloatView);
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mLayoutParams.format = PixelFormat.RGBA_8888;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.width = 200;
        mLayoutParams.height = 200;
        x = screenWidth - 300;
        locationX = x;
        y = screenHeight / 2 + 300;
        mLayoutParams.x = x;
        mLayoutParams.y = y;
        locationY=y;
        mLayoutParams2 = new WindowManager.LayoutParams();
        mLayoutParams2.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams2.type = WindowManager.LayoutParams.TYPE_PHONE;
        mLayoutParams2.format = PixelFormat.RGBA_8888;
        mLayoutParams2.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams2.width = 80;
        mLayoutParams2.height = 80;
        x2 = x;
        y2 = y;
        mLayoutParams2.x = x2;
        mLayoutParams2.y = y2;


        mLayoutParams3 = new WindowManager.LayoutParams();
        mLayoutParams3.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams3.type = WindowManager.LayoutParams.TYPE_PHONE;
        mLayoutParams3.format = PixelFormat.RGBA_8888;
        mLayoutParams3.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams3.width = 0;
        mLayoutParams3.height = 0;
//        mWindowManager.addView(mFloatView3, mLayoutParams3);
        Log.i("OneTouchFloatView", "init.....");
    }


    boolean add = false;

    public void show() {
        if (add) {
            return;
        }
        if (mFloatView == null)
            init();
        if (mFloatView == null)
            return;
        mWindowManager.addView(mFloatView2, mLayoutParams2);
        mWindowManager.addView(mFloatView, mLayoutParams);
        add = true;

    }

    public void remove() {
        if (add == false)
            return;
        try {
            if (mWindowManager != null && mFloatView != null) {
                mWindowManager.removeView(mFloatView);
                mWindowManager.removeView(mFloatView2);
                add = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void log(String s) {
        Log.i(getClass().getSimpleName(), s);
    }


    private LinearLayout menu;
    private int lastX;
    private int lastY;
    private int x, y;
    private int x2, y2;
    long time2;

    private void initView(View rootView) {
        menu = (LinearLayout) rootView.findViewById(R.id.menu);
        menu.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int rawX = (int) motionEvent.getRawX();
                int rawY = (int) motionEvent.getRawY();
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    lastX = rawX;
                    lastY = rawY;
                    time2 = System.currentTimeMillis();
                    mLayoutParams3.width = 1;
                    mLayoutParams3.height = 1;
                    try {
                        mWindowManager.addView(mFloatView3, mLayoutParams3);
                    } catch (Exception e) {

                    }

                }

                int offsetX = rawX - lastX;
                int offsetY = rawY - lastY;

                mLayoutParams.x = (int) (x + offsetX);
                mLayoutParams.y = (int) (y + offsetY);
                mWindowManager.updateViewLayout(mFloatView, mLayoutParams);

                if (more == false) {
                    mLayoutParams2.x = (int) (x2 + offsetX * 5);
                    mLayoutParams2.y = (int) (y2 + offsetY * 5);
                    mLayoutParams2.width = 80;
                    mLayoutParams2.height = 80;
                    mWindowManager.updateViewLayout(mFloatView2, mLayoutParams2);

                    has(mLayoutParams2.x, mLayoutParams2.y);
                }


                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {


                    if (more) {
                        x = mLayoutParams.x;
                        y = mLayoutParams.y;

                        x2 = mLayoutParams.x;
                        ;
                        y2 = mLayoutParams.y;

                        mLayoutParams.x = x;
                        mLayoutParams.y = y;
                        mWindowManager.updateViewLayout(mFloatView, mLayoutParams);

                        mLayoutParams2.x = x2;
                        mLayoutParams2.y = mLayoutParams.y;
                        mLayoutParams2.width = 1;
                        mLayoutParams2.height = 1;
                        mWindowManager.updateViewLayout(mFloatView2, mLayoutParams2);
                        mWindowManager.removeView(mFloatView3);
                        more = false;

                        locationX = x;
                        locationY = y;

                        Toast.makeText(con, "激活模式关闭", Toast.LENGTH_SHORT).show();
                    } else {
                        click(mLayoutParams2.x, mLayoutParams2.y);

                        x = locationX;
                        y = locationY;

                        x2 = locationX;
                        y2 = locationY;

                        mLayoutParams.x = x;
                        mLayoutParams.y = y;
                        mWindowManager.updateViewLayout(mFloatView, mLayoutParams);

                        mLayoutParams2.x = x2;
                        mLayoutParams2.y = y2;
                        mLayoutParams2.width = 1;
                        mLayoutParams2.height = 1;
                        mWindowManager.updateViewLayout(mFloatView2, mLayoutParams2);
                        mWindowManager.removeView(mFloatView3);
                    }


                    if (System.currentTimeMillis() - time2 < 100) {
                        more = true;
                        Toast.makeText(con, "激活移动模式", Toast.LENGTH_SHORT).show();
                    }

                }


                return true;
            }
        });
    }


    public void click(int x, int y) {

//        for (int i = 0; i < nodeInfos.size(); i++) {
//            Rect rect = new Rect();
//            AccessibilityNodeInfo node = nodeInfos.get(i);
//            node.getBoundsInScreen(rect);
//            ;
//            if (rect.contains(x + 40, y + 40)) {
//                clickView(node);
//                log("ACTION_CLICK" + x + "  " + y);
//            }
//        }
        if (clickNode != null)
            clickView(clickNode);
        clickNode = null;
    }

    AccessibilityNodeInfo clickNode;

    public void has(int x, int y) {

        boolean has = false;
        for (int i = 0; i < nodeInfos.size(); i++) {
            Rect rect = new Rect();
            AccessibilityNodeInfo node = nodeInfos.get(i);
            node.getBoundsInScreen(rect);
            if (rect.contains(x + 40, y + 40) && !rect.contains(this.x + 20, this.y + 20)) {
                mLayoutParams3.width = rect.right - rect.left;
                mLayoutParams3.height = rect.bottom - rect.top;
                mLayoutParams3.x = rect.left;
                mLayoutParams3.y = rect.top - MainTabActivity.getTop(con);
                mLayoutParams3.alpha=1;
                mWindowManager.updateViewLayout(mFloatView3, mLayoutParams3);
                clickNode = node;
                has = true;
            } else {
            }
        }
        if (has == false) {
//            mLayoutParams3.width = 1;
//            mLayoutParams3.height = 1;
//            mLayoutParams3.x = mLayoutParams.x;
//            mLayoutParams3.y = mLayoutParams.y;
            mLayoutParams3.alpha=0;
            mWindowManager.updateViewLayout(mFloatView3, mLayoutParams3);
            clickNode = null;
        }
    }

    private void clickView(AccessibilityNodeInfo node) {
        if (node.isClickable()) {
            node.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            clickView(node.getParent());
        }
    }

}
