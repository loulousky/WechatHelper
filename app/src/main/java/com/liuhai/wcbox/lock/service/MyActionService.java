package com.liuhai.wcbox.lock.service;

import android.accessibilityservice.AccessibilityService;
import android.app.KeyguardManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.PowerManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.liuhai.wcbox.lock.service.utils.ActionUtils;
import com.liuhai.wcbox.lock.service.utils.FloatViewUtils;
import com.liuhai.wcbox.lock.service.utils.SplashUtils;
import com.liuhai.wcbox.lock.utils.MySettings;
import com.liuhai.wcbox.lock.utils.StringUtils;


public class MyActionService extends AccessibilityService {

    private SplashUtils splashUtils;//开屏控制器
    private FloatViewUtils floatViewUtils;//浮动广告控制器
    private ActionUtils toolUtils;//工具控制器
    private ClipboardManager clipboard;
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Toast.makeText(this, "微商盒子启用成功", Toast.LENGTH_SHORT).show();
        Log.i("MyActionService", "onServiceConnected。。。。。。。。");
        init();
    }


    public void init() {


        if (floatViewUtils == null)
            floatViewUtils = new FloatViewUtils();

        if (splashUtils == null) {
            splashUtils = new SplashUtils();
        }

        if (toolUtils == null)
            toolUtils = new ActionUtils();

        MySettings.setDeleteFriend(this, false);
        MySettings.setDeleteFriendAction(this,false);
        MySettings.setQunfa(this,false);
        MySettings.setAddFriend(this,false);
        MySettings.setZan(this,false);
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        try {

            Log.d("~~~liuhai~~~",event.toString());
            String packageName = event.getPackageName() == null ? "" : event.getPackageName().toString();
            //不在微信界面内清空所有自动的动作
            if (getPackageName().equals(packageName)) {
                //
                MySettings.setDeleteFriend(this, false);
                MySettings.setDeleteFriendAction(this,false);
                MySettings.setQunfa(this,false);
                MySettings.setAddFriend(this,false);
                MySettings.setZan(this,false);
                return;
            }
            if (!packageName.contains("com.tencent.mm")){
                MySettings.setDeleteFriend(this, false);
                MySettings.setDeleteFriendAction(this,false);
                MySettings.setQunfa(this,false);
                MySettings.setAddFriend(this,false);
                MySettings.setZan(this,false);
                return;
            }

            floatViewUtils.event(this, event);
            splashUtils.event(this, event);
            toolUtils.event(this, event);

            yanzhengma(event);

        } catch (Exception e) {
            e.printStackTrace();
            init();
        }

    }



    /**
     * 验证码
     * @param event
     */
    private void yanzhengma(AccessibilityEvent event){

//        陈亮: 【丰巢】您的验证码337673，90秒内有效。
        if(event.getEventType()==AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            String message=event.getText().toString();
            String yanzhengma = StringUtils.tryToGetCaptchas(message);
            String conpany=null;
            if(message.contains("【")&&message.contains("】")) {
                conpany=message.substring(message.indexOf("【")+1, message.indexOf("】"));
            }
            if (clipboard == null) {
                clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
            }
            if (yanzhengma != null && !"".equals(yanzhengma)&&yanzhengma.length()>3&&!message.contains("验证码已复制:")) {
                ClipData clip = ClipData.newPlainText("text", yanzhengma);
                clipboard.setPrimaryClip(clip);
                if(conpany==null){
                    Toast.makeText(this, "验证码已复制:" + yanzhengma, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "【"+conpany+"】验证码已复制:" + yanzhengma, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    @Override
    public void onInterrupt() {

    }


    @Override
    public void unbindService(ServiceConnection conn){
        super.unbindService(conn);
        log("unbindService:..................");
        Toast.makeText(this, "微商启用失败", Toast.LENGTH_SHORT).show();
    }

    public void log(String log) {
        Log.i(getClass().getSimpleName(), log);
    }


    PowerManager.WakeLock mWakelock;

    boolean jeisuo=false;

    public void acquire() {
        Log.i("WeChat_AutoHuifu", "亮屏.....");
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);// init powerManager
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
        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        kl = km.newKeyguardLock("kale");
//        kl.disableKeyguard();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        kl.disableKeyguard();
        kl.disableKeyguard();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyActionService","服务销毁了");
    }
}
