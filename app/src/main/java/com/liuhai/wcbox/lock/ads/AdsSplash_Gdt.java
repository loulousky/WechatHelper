package com.liuhai.wcbox.lock.ads;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;


/**
 * Created by chenliangj2ee on 2017/5/14.
 * 快捷入口启动时展示的开屏
 */

public class AdsSplash_Gdt {
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private FrameLayout mFloatView;
    private Context con;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            remove();


        }
    };

    public AdsSplash_Gdt(Context con) {
        this.con = con;

    }

    public void showAds(FrameLayout view) {
        mWindowManager = (WindowManager) con.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mFloatView = view;
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mLayoutParams.format = PixelFormat.RGBA_8888;
//        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        mLayoutParams.x = 0;
        mLayoutParams.y = 0;
        mFloatView.setSystemUiVisibility(View.INVISIBLE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        mWindowManager.addView(mFloatView, mLayoutParams);
        handler.sendEmptyMessageDelayed(0, 6000);

    }


    public void remove() {
        try {
            if (mWindowManager != null && mFloatView != null)
                mWindowManager.removeView(mFloatView);
            mFloatView = null;

            log("开屏隐藏.........");
        } catch (Exception e) {

        }


    }

    public void update() {
        mWindowManager.updateViewLayout(mFloatView, mLayoutParams);
    }


    private void log(String s) {
        Log.i(getClass().getSimpleName(), s);
    }


}
