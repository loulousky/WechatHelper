package com.liuhai.wcbox.lock.floatview;


import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.liuhai.wcbox.R;

import net.yrom.screenrecorder.MainActivity;

/**
 * Created by chenliangj2ee on 2017/5/14.
 * 快捷入口启动时展示的开屏
 */

public class WeChat_FloatScreenRecorder {
    public int screenWidth;
    public int screenHeight;
    protected View rootView;
    protected TextView timeTextView;
    protected TextView startStop;
    protected LinearLayout rootview;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private LayoutInflater mLayoutInflater;
    private LinearLayout mFloatView;
    private Context con;
    private int x, y;
    private int time;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(MainActivity.boo){
                time++;
                String s=time/60>=10?time/60+"":"0"+time/60;
                String m=time%60>=10?time%60+"":"0"+time%60;
                timeTextView.setText(s+":"+m);
            }else{
                time=0;
            }

        }
    };


    public WeChat_FloatScreenRecorder(Context con) {
        this.con = con;
        this.mWindowManager = (WindowManager) con.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        this.screenHeight = mWindowManager.getDefaultDisplay().getHeight();
        this.screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        init();
    }


    public void init() {

        mWindowManager = (WindowManager) con.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mLayoutInflater = LayoutInflater.from(con);
        mFloatView = (LinearLayout) mLayoutInflater.inflate(R.layout.float_screen_recorder_menu, null);
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

    private void initView(final View rootView) {
        timeTextView = (TextView) rootView.findViewById(R.id.time);
        startStop = (TextView) rootView.findViewById(R.id.start_stop);
        rootview = (LinearLayout) rootView.findViewById(R.id.rootview);
        if(MainActivity.boo){
            startStop.setText("结束");
            remove();
        }else{
            startStop.setText("开始");
        }
        startStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.boo=!MainActivity.boo;
                if(MainActivity.boo){
                    startStop.setText("结束");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            while (MainActivity.boo) {
                                try {
                                    Thread.sleep(1000);
                                    handler.sendEmptyMessageDelayed(0,1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                }else{
                    startStop.setText("开始");
                    handler.sendEmptyMessageDelayed(1,0);
                    time=-1;
                    remove();

                }
                Intent intent = new Intent(con, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                con.startActivity(intent);
            }
        });
        rootview.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int rawX = (int) motionEvent.getRawX();
                int rawY = (int) motionEvent.getRawY();
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    lastX = rawX;
                    lastY = rawY;
                }

                int offsetX = rawX - lastX;
                int offsetY = rawY - lastY;

                mLayoutParams.x = (int) (x + offsetX);
                mLayoutParams.y = (int) (y + offsetY);
                mWindowManager.updateViewLayout(mFloatView, mLayoutParams);

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    x = mLayoutParams.x;
                    y = mLayoutParams.y;
                }
                return true;
            }
        });
    }
}
