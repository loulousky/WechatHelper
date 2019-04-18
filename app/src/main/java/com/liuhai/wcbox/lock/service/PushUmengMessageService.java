package com.liuhai.wcbox.lock.service;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;

import com.liuhai.wcbox.lock.SplashActivity;
import com.umeng.message.UmengMessageService;

import org.android.agoo.common.AgooConstants;

/**
 * Created by chenliangj2ee on 2017/9/8.
 */

public class PushUmengMessageService extends UmengMessageService {
    Context context;

    Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            clearSplash();

            try {
                mWakelock.release();
                kl.reenableKeyguard();
            } catch (Exception e) {

            }
        }
    };

    @Override
    public void onMessage(Context context, Intent intent) {
       this.context=context;
        String message = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        if (message.contains("clear")) {
            Log.i("PushUmengMessageService", "clear");
            clearSplash();
        } else {
            Log.i("PushUmengMessageService", "no clear");
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            boolean ifOpen = powerManager.isScreenOn();
            if(ifOpen){

            }else{
                openSplash();
            }



        }
    }
    private void clearSplash(){
        Intent in = new Intent(this, SplashActivity.class);
        in.putExtra("finish",true);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
    }

    private void openSplash(){

        try {
            acquire();//亮屏
        } catch (Exception e) {

        }
        Intent in = new Intent(this, SplashActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
        handler.sendEmptyMessageDelayed(0,10000);
    }

    PowerManager.WakeLock mWakelock;

    boolean jeisuo=false;

    public void acquire() {
        Log.i("WeChat_AutoHuifu", "亮屏.....");
        PowerManager pm = (PowerManager) context.getSystemService(POWER_SERVICE);// init powerManager
        mWakelock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                        | PowerManager.FULL_WAKE_LOCK
                        | PowerManager.ACQUIRE_CAUSES_WAKEUP
                        | PowerManager.ON_AFTER_RELEASE,
                "target");
        mWakelock.acquire();
        jiesuo();
        jeisuo=true;
    }

    KeyguardManager.KeyguardLock kl;

    private void jiesuo() {
        Log.i("WeChat_AutoHuifu", "解锁.....");
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        kl = km.newKeyguardLock("kale");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        kl.disableKeyguard();
    }

}
