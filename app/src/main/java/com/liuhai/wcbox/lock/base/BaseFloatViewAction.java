package com.liuhai.wcbox.lock.base;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import com.liuhai.wcbox.R;


/**
 * Created by chenliangj2ee on 2017/5/21.
 */

public abstract class BaseFloatViewAction {

    public int screenHeight;
    public WindowManager mWindowManager;
    public WindowManager.LayoutParams mLayoutParams;
    public LayoutInflater mLayoutInflater;
    public View mFloatView;
    public View close;
    public int myWidth, myHeight, myX, myY;
    public AccessibilityService service;
    public AccessibilityEvent event;
    public int gravity;

    public void event(AccessibilityService service, AccessibilityEvent event) {
        this.service = service;
        this.event = event;
        this.mWindowManager = (WindowManager) service.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        this.mLayoutInflater = LayoutInflater.from(service);
        this.screenHeight = mWindowManager.getDefaultDisplay().getHeight();
        gravity = Gravity.LEFT | Gravity.TOP;
    }


    public void setLocation(int width, int height, int x, int y) {
        this.myWidth = width;
        this.myHeight = height;
        this.myX = x;
        this.myY = y;
    }

    public void setgravity(int gravity) {
        this.gravity = gravity;
    }

    public void show() {
        try {
            initView();
            mLayoutParams = new WindowManager.LayoutParams();
            mLayoutParams.gravity = gravity;
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            mLayoutParams.format = PixelFormat.RGBA_8888;
            mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            mLayoutParams.width = myWidth;
            mLayoutParams.height = myHeight;
            mLayoutParams.x = myX;
            mLayoutParams.y = myY;
            mWindowManager.addView(mFloatView, mLayoutParams);
            mFloatView.setVisibility(View.GONE);
            Log.i("BaseFloatViewAction", "展示广告.....");
            Log.i("BaseFloatViewAction", "width：" + myWidth + "    height：" + myHeight + "    x：" + myX + "    y：" + myY);
        } catch (Exception e) {
        }

    }

    public abstract int initLayoutId();

    private void initView() {

        if (mFloatView != null){
            return;
        }

        mFloatView = mLayoutInflater.inflate(initLayoutId(), null);
        close=mFloatView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove();
            }
        });
        Log.i("BaseFloatViewAction", "初始化广告.....");
    }

    public View finViewById(int id) {
        if (mFloatView == null)
            return null;
        return mFloatView.findViewById(id);
    }

    public void remove() {
        try {
            if (mWindowManager != null && mFloatView != null)
                mWindowManager.removeView(mFloatView);
            mFloatView = null;
        } catch (Exception e) {
            mFloatView = null;
        }
        Log.i("BaseFloatViewAction", "隐藏广告.....");
    }


    public void log(String log) {
        Log.i(this.getClass().getSimpleName(), log);
    }
}
