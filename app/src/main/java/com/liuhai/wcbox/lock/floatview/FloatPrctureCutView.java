package com.liuhai.wcbox.lock.floatview;


import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.liuhai.wcbox.R;
import com.liuhai.wcbox.lock.act.PrictureScreenCutActivity;

/**
 * Created by chenliangj2ee on 2017/5/14.
 * 快捷入口启动时展示的开屏
 */

public class FloatPrctureCutView {
    public int screenWidth;
    public int screenHeight;
    protected View rootView;
    protected LinearLayout rootview;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private LayoutInflater mLayoutInflater;
    private LinearLayout mFloatView;
    private Context con;
    private int x, y;



    public FloatPrctureCutView(Context con) {
        this.con = con;
        this.mWindowManager = (WindowManager) con.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        this.screenHeight = mWindowManager.getDefaultDisplay().getHeight();
        this.screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        init();
    }


    public void init() {

        mWindowManager = (WindowManager) con.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mLayoutInflater = LayoutInflater.from(con);
        mFloatView = (LinearLayout) mLayoutInflater.inflate(R.layout.float_prcture_cut, null);
        initView(mFloatView);
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mLayoutParams.format = PixelFormat.RGBA_8888;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        x = screenWidth / 2 - 200;
        y = screenHeight / 2;
        mLayoutParams.x = x;
        mLayoutParams.y = y;
    }


    boolean add = false;

    public void show() {
        if (add)
            return;

        if (mFloatView == null)
            init();

        if (mFloatView == null)
            return;
        mWindowManager.addView(mFloatView, mLayoutParams);
        add = true;
        mFloatView.postInvalidate();


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
    }

    private void log(String s) {
        Log.i(getClass().getSimpleName(), s);
    }

    private int lastX;
    private int lastY;

    long start;
    private void initView(final View rootView) {
        rootview = (LinearLayout) rootView.findViewById(R.id.rootview);
        rootview.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int rawX = (int) motionEvent.getRawX();
                int rawY = (int) motionEvent.getRawY();
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    lastX = rawX;
                    lastY = rawY;
                    start= System.currentTimeMillis();
                }

                int offsetX = rawX - lastX;
                int offsetY = rawY - lastY;

                mLayoutParams.x = (int) (x + offsetX);
                mLayoutParams.y = (int) (y + offsetY);
                mWindowManager.updateViewLayout(mFloatView, mLayoutParams);

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    x = mLayoutParams.x;
                    y = mLayoutParams.y;
                    if(System.currentTimeMillis()-start<100){
                        Intent intent = new Intent(con, PrictureScreenCutActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        con.startActivity(intent);
                        remove();
                    }
                }
                return true;
            }
        });
    }
}
