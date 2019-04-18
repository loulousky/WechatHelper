package com.liuhai.wcbox.lock.floatview;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuhai.wcbox.R;
import com.liuhai.wcbox.lock.utils.MySettings;

/**
 * Created by chenliangj2ee on 2017/5/21.
 * 单聊对话
 */

public class CheckFinishDialog {
    protected TextView message;
    protected TextView close;
    protected TextView delete;
    protected LinearLayout banner;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private LayoutInflater mLayoutInflater;
    private View mFloatView;
    private Context con;
    String usernames;

    public CheckFinishDialog(Context con, String usernames) {
        this.usernames = usernames;
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
        Log.i("CheckFinishDialog", "展示广告......");

    }


    private void initView() {
        if (mFloatView != null)
            return;
        mFloatView = mLayoutInflater.inflate(R.layout.float_check_friend_finish_dialog, null);
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
        message = (TextView) mFloatView.findViewById(R.id.message);
        close = (TextView) mFloatView.findViewById(R.id.close);
        delete = (TextView) mFloatView.findViewById(R.id.delete);
        banner = (LinearLayout) mFloatView.findViewById(R.id.banner);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySettings.setDeleteFriendAction(con, true);

                Intent intent = new Intent();
                ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(cmp);
                con.startActivity(intent);

                remove();
            }
        });

        message.setText(usernames + ",是否将这帮傻逼删除？？");

    }

    public void remove() {
        try {
            if (mWindowManager != null && mFloatView != null)
                mWindowManager.removeView(mFloatView);
            mFloatView = null;
        } catch (Exception e) {
            mFloatView = null;
        }
        Log.i("CheckFinishDialog", "移除广告......");
    }


}
