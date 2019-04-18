package com.liuhai.wcbox.lock.action;

import android.accessibilityservice.AccessibilityService;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.PowerManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.liuhai.wcbox.lock.base.BaseAction;

import java.util.List;

import static android.content.Context.POWER_SERVICE;

/**
 * 自动回复
 */

public class WeChat_AutoHuifu extends BaseAction {

    private int index = 0;

    public WeChat_AutoHuifu(AccessibilityService service) {
        super(service);
        index = 0;
    }


    @Override
    public void event(AccessibilityEvent event) {

        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
        String packageName = event.getPackageName() == null ? "" : event.getPackageName().toString();
        if (nodeInfo == null || !packageName.contains("com.tencent.mm")) {
            return;
        }

        /**
         * 处理通知栏信息
         * <p>
         * 如果是微信红包的提示信息,则模拟点击
         *
         * @param event
         */


        if (true) {
            List<CharSequence> texts = event.getText();
            if (!texts.isEmpty()) {
                for (CharSequence text : texts) {
                    String content = text.toString();
                    //如果不是微信红包提示
                    if (!content.contains("微信红包")) {

                        if (event.getParcelableData() != null && event.getParcelableData() instanceof Notification) {
                            acquire();//亮屏
                            Notification notification = (Notification) event.getParcelableData();
                            PendingIntent pendingIntent = notification.contentIntent;
                            try {
                                pendingIntent.send();
                                log("打开对话。。。。");
                                index = 1;
                                break;
                            } catch (PendingIntent.CanceledException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }

            }
        }
        if (index == 1) {
            try {
                log("当前步骤：" + index);
                List<AccessibilityNodeInfo> edits = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/amb");//获得编辑框
                List<AccessibilityNodeInfo> titles = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/k3");//获得回话标题，含有“()”的，则为群聊
                if (edits != null && edits.size() > 0 && titles.get(0).getText().toString().contains("(") && titles.get(0).getText().toString().contains(")")) {
                    service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
                    return;
                }
                if (edits != null && edits.size() > 0) {
                    ClipboardManager clipboard = (ClipboardManager) service.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("text", "此时在忙，稍后回复...[自动回复-安卓版<徽商盒子>]");
                    clipboard.setPrimaryClip(clip);
                    edits.get(0).performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                    edits.get(0).performAction(AccessibilityNodeInfo.ACTION_PASTE);
                    index = 2;
                    return ;
                }

//                service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);

            } catch (Exception e) {

                index = 0;
            }

        }

        if (index == 2) {
            try {
                log("当前步骤：" + index);
                List<AccessibilityNodeInfo> fasongs = nodeInfo.findAccessibilityNodeInfosByText("发送");//获得赞按钮
                if (fasongs != null && fasongs.size() > 0) {
                    fasongs.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    index = 3;
                }
            } catch (Exception e) {

                index = 0;
            }


        }
        if (index == 3) {
            try {
                log("当前步骤：" + index);
                index = 0;
                Thread.sleep(1000);
                service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
                if(jeisuo) {
                    mWakelock.release();
                    kl.reenableKeyguard();
                }
            } catch (Exception e) {

                index = 0;
            }

        }


    }

    PowerManager.WakeLock mWakelock;

    boolean jeisuo=false;

    public void acquire() {
        Log.i("WeChat_AutoHuifu", "亮屏.....");
        PowerManager pm = (PowerManager) service.getSystemService(POWER_SERVICE);// init powerManager
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
        KeyguardManager km = (KeyguardManager) service.getSystemService(Context.KEYGUARD_SERVICE);
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
}