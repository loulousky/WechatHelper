package com.liuhai.wcbox.lock.action;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.liuhai.wcbox.lock.base.BaseAction;
import com.liuhai.wcbox.lock.utils.MySettings;

import java.util.List;

import static android.content.Context.POWER_SERVICE;

/**
 * Created by chenliangj2ee on 2017/5/17.
 * 微信抢红包
 */

public class WeChat_RedPackageAction  extends BaseAction {



    public WeChat_RedPackageAction(AccessibilityService service) {
        super(service);
    }


    @Override
    public void event(AccessibilityEvent event) {
        if (event == null|| MySettings.isRedPackage(service)==false)
            return;
//        if (event.getEventType() != AccessibilityEvent.TYPE_VIEW_CLICKED && event.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED)
//            return;
        log(event.toString());

        String packageName=event.getPackageName()==null?"":event.getPackageName().toString();

        if (!packageName.contains("com.tencent.mm")) {
            return;
        }

        handleNotification(event);
        getPacket();
        openPacket();
        close(event);
    }

    /**
     * 处理通知栏信息
     * <p>
     * 如果是微信红包的提示信息,则模拟点击
     *
     * @param event
     */
    private void handleNotification(AccessibilityEvent event) {
        List<CharSequence> texts = event.getText();
        if (!texts.isEmpty()) {
            for (CharSequence text : texts) {
                if(text==null)
                    continue;
                String content = text.toString();
                //如果微信红包的提示信息,则模拟点击进入相应的聊天窗口
                if (content.contains("微信红包")) {
                    if (event.getParcelableData() != null && event.getParcelableData() instanceof Notification) {
                        acquire();
                        Notification notification = (Notification) event.getParcelableData();
                        PendingIntent pendingIntent = notification.contentIntent;
                        try {
                            pendingIntent.send();
                            log("打开通知。。。。");
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

    }

    /**
     * 关闭红包详情界面,实现自动返回聊天窗口
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void close(AccessibilityEvent event){
        try {
            String className = event.getClassName() == null ? "" : event.getClassName().toString();
            if ("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI".equals(className)) {
                Thread.sleep(2000);
                service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                log("关闭红包。。。。");
            }
        } catch (Exception e) {
        }

    }

    /**
     * 模拟点击,拆开红包
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void openPacket() {
        try {
            AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
            if (nodeInfo != null) {
                List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/cyf");//获得【开】按钮
                if (list.isEmpty() == false) {
                    list.get(list.size() - 1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    log("拆开红包。。。。");
                }
            }
        } catch (Exception e) {
        }

    }

    /**
     * 模拟点击,打开抢红包界面
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void getPacket() {

        try {
            AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
            if (nodeInfo != null) {

                AccessibilityNodeInfo listview = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/alc").get(0).getChild(0);//获得列表listview
                if (listview!=null) {
                    AccessibilityNodeInfo info = listview.getChild(listview.getChildCount() - 1);//获取listview最后一个item
                    if(info!=null){
                        List<AccessibilityNodeInfo> last=info.findAccessibilityNodeInfosByText("你领取了");

                        if (last.isEmpty()==false) {
                            //最后一个item中包含“你领取了”等字段，说明最后一个红包领取完了
                            log("最后一个红包领取完了。。。。");
                        } else {

                            List<AccessibilityNodeInfo> wxhbs= info.findAccessibilityNodeInfosByText("微信红包");
                            if(wxhbs==null||wxhbs.size()<=0)
                                return ;

                            //获取item中头像view
                            AccessibilityNodeInfo itemHeadInfos= info.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ns").get(0);

                            //获取当前会话用户的name(顶部)
                            AccessibilityNodeInfo titleUserName= nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/k3").get(0);

                            log("itemHeadInfos:"+itemHeadInfos.getContentDescription().toString());
                            log("titleUserName:"+titleUserName.getText().toString());
                            //如果名称一样，说明是对方发的红包、只抢对方发的红包
                            if(itemHeadInfos.getContentDescription().toString().contains(titleUserName.getText().toString())){

                                //获取红包views
                                List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/a7z");
                                if (list.isEmpty() == false) { //最后一个红包执行click
                                    list.get(list.size() - 1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    log("查看红包。。。。");
                                }

                            }




                        }

                    }

                }


            }
        } catch (Exception e) {
        }

    }

    PowerManager.WakeLock mWakelock;

    public void acquire() {
        Log.i("WeChat_AutoHuifu", "亮屏.....");
        PowerManager pm = (PowerManager) service.getSystemService(POWER_SERVICE);// init powerManager
        mWakelock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                        | PowerManager.FULL_WAKE_LOCK
                        | PowerManager.ACQUIRE_CAUSES_WAKEUP
                        | PowerManager.ON_AFTER_RELEASE
                , "target");
        mWakelock.acquire();
        jiesuo();
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
