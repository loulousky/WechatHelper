package com.liuhai.wcbox.lock.floatview;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.liuhai.wcbox.R;


/**
 * Created by chenliangj2ee on 2017/5/13.
 * 检测好友时，弹出全屏广告
 */

public class Float_ShareingFriendsView {

    protected TextView messages;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private LayoutInflater mLayoutInflater;
    private View mFloatView;
    private Context con;
    private String mes;

    public Float_ShareingFriendsView(Context con) {
        init(con);
    }

    public void init(Context con) {
        this.con = con;
        mWindowManager = (WindowManager) con.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mLayoutInflater = LayoutInflater.from(con);

    }

    public void setMessages(String mes) {
        this.mes = mes;
    }
    public String getMessage(){
        return mes;
    }

    int time;

    public void setTime(int time) {
        this.time = time;
    }

    public void show() {
        initView();
    }

    private void initView() {
        if (mFloatView != null)
            return;
        mFloatView = mLayoutInflater.inflate(R.layout.float_share_friends, null);
        messages = (TextView) mFloatView.findViewById(R.id.message);
        this.setMessages(mes);
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        mLayoutParams.format = PixelFormat.RGBA_8888;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        mLayoutParams.x = 0;
        mLayoutParams.y = 0;
        mWindowManager.addView(mFloatView, mLayoutParams);
        mFloatView.setSystemUiVisibility(View.INVISIBLE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public void remove() {
        try {
            if (mWindowManager != null && mFloatView != null)
                mWindowManager.removeView(mFloatView);
            mFloatView = null;
        } catch (Exception e) {
            mFloatView = null;
        }
    }

}
