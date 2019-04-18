package com.liuhai.wcbox.lock.floatview;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.liuhai.wcbox.R;
import com.liuhai.wcbox.lock.utils.MySettings;


/**
 * Created by chenliangj2ee on 2017/5/13.
 * 检测好友时，弹出全屏广告
 */

public class CheckingFriendFloatView {

    protected TextView username;
    protected TextView userNumber;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private LayoutInflater mLayoutInflater;
    private View mFloatView;
    private View close;
    private Context con;


    public CheckingFriendFloatView(Context con) {
        init(con);
    }

    public void init(Context con) {
        this.con = con;
//        remove();
        mWindowManager = (WindowManager) con.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mLayoutInflater = LayoutInflater.from(con);

    }

    public void show() {
        initView();


    }

    private void initView() {
        if (mFloatView != null)
            return;
        mFloatView = mLayoutInflater.inflate(R.layout.float_check_friend_delete, null);
        username = (TextView) mFloatView.findViewById(R.id.username);
        close = (TextView) mFloatView.findViewById(R.id.close);
        userNumber = (TextView) mFloatView.findViewById(R.id.userNumber);
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        mLayoutParams.format = PixelFormat.RGBA_8888;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        mLayoutParams.x = 0;
        mLayoutParams.y = 0;
        mWindowManager.addView(mFloatView, mLayoutParams);
        mFloatView.setSystemUiVisibility(View.INVISIBLE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySettings.setDeleteFriend(con, false);
                remove();
            }
        });
    }

    public void remove() {
        try {
            if (mWindowManager != null && mFloatView != null) {
                mWindowManager.removeView(mFloatView);
                mFloatView = null;
            }
        } catch (Exception e) {
            mFloatView = null;
        }
        Log.i("CheckFriendDeleteSplash", "移除广告......");
    }

    public void refreshText(String user, String number) {
        if (username != null) {
            username.setText(user);
        }
        if (userNumber != null) {
            userNumber.setText("已检测" + number + "人");
        }
        mFloatView.invalidate();
        username.invalidate();
        userNumber.invalidate();
    }


    public void checking(String usernameStr) {
        if (username != null) {
            username.setText(usernameStr);
            mWindowManager.updateViewLayout(mFloatView, mLayoutParams);

        }

    }

}
