package com.liuhai.wcbox.lock.action;

import android.accessibilityservice.AccessibilityService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.liuhai.wcbox.lock.base.BaseAction;
import com.liuhai.wcbox.lock.utils.MySettings;

import java.util.List;

/**
 * Created by chenliangj2ee on 2017/5/17.
 */

public class WeChat_QunFa extends BaseAction {

    private int index = 0;
    private int page;
    private int zanIndex;//当前页第几个赞
    private int totalCount;
    private int preCount;//当前页itemCount;

    private String preText = "";//记录上次点击item时的text;


    public WeChat_QunFa(AccessibilityService service) {
        super(service);
        index = 0;
    }


    @Override
    public void event(AccessibilityEvent event) {
        try {
            if (MySettings.isQunfa(service) == false) {
                index = 0;
                return;
            }
            String packageName = event.getPackageName() == null ? "" : event.getPackageName().toString();

            if (!"com.tencent.mm".equals(packageName)) {
                return;
            }
            int eventType = event.getEventType();

            final AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();

            if (nodeInfo == null) {
                return;
            }

            if (index == 0) {//获取【通讯录】textview，获取父类执行点击事件
                List<AccessibilityNodeInfo> tongxunls = nodeInfo.findAccessibilityNodeInfosByText("通讯录");
                if (tongxunls == null || tongxunls.size() <= 0)
                    return;
                AccessibilityNodeInfo tongxunl = tongxunls.get(0);
                click(tongxunl);
                index = 1;
            }


            if (index == 1) {
                List<AccessibilityNodeInfo> listviews = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/hv");//获得通讯录ListView
                if (listviews == null || listviews.isEmpty()) {
//                    index=0;
                    return;
                }

                if(preCount>=listviews.get(0).getChildCount()) {
                    listviews.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                    page++;
                    preCount = 0;
                    return;
                }
                AccessibilityNodeInfo item = listviews.get(0).getChild(preCount);//获得ListView的第count个item
                if (item != null && item.isClickable()) {
                    item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    totalCount++;
                    preCount++;
                    index = 2;

                    if (hasText(item, "位联系人")) {
                        log("群发结束：" + totalCount);
                        MySettings.setQunfa(service, false);
                        index = 0;
                    }
                    log(index + "当前页数:" + page + "    当前item:" + preCount);

                }
            }

            if (index == 2) {//获取【发消息】按钮，点击进入详细资料

                List<AccessibilityNodeInfo> faxiaoxis = nodeInfo.findAccessibilityNodeInfosByText("发消息");//获得【发消息】按钮
                if (faxiaoxis != null && !faxiaoxis.isEmpty()) {
                    faxiaoxis.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                    index = 3;
                    index = 5;//测试
                }
            }


            if (index == 3) {
                List<AccessibilityNodeInfo> edits = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/a5e");//获得编辑框
                if (edits != null && edits.size() > 0) {
                    ClipboardManager clipboard = (ClipboardManager) service.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("text", "你好");
                    clipboard.setPrimaryClip(clip);
                    edits.get(0).performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                    edits.get(0).performAction(AccessibilityNodeInfo.ACTION_PASTE);
                    index = 4;
                }

            }

            if (index == 4) {
                List<AccessibilityNodeInfo> fasongs = nodeInfo.findAccessibilityNodeInfosByText("发送");//获得【发送】按钮
                if (fasongs != null && fasongs.size() > 0) {
                    fasongs.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    index = 5;
                }


            }


            if (index == 5) {
                List<AccessibilityNodeInfo> listviews = nodeInfo.findAccessibilityNodeInfosByText("通讯录"); //判断是否返回到首页
                if (listviews == null || listviews.isEmpty()) {
                    service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                } else {
                    index = 0;
                }
            }

//
//            if (index == 6) {
//                List<AccessibilityNodeInfo> tongxunls = nodeInfo.findAccessibilityNodeInfosByText("通讯录");
//                if (tongxunls != null && tongxunls.size()>0){
//                    AccessibilityNodeInfo tongxunl = tongxunls.get(0);
//                    click(tongxunl);
//                    index=1;
//                }
//            }




        } catch (Exception e) {
            index = 0;
            MySettings.setQunfa(service, false);
            log("群发异常，结束。。。");
            e.printStackTrace();
        }

    }

    private boolean hasText(AccessibilityNodeInfo item, String text) {
        List<AccessibilityNodeInfo> infos = item.findAccessibilityNodeInfosByText(text);
        if (infos != null && infos.size() > 0) {
            return true;
        }
        return false;
    }

    private void click(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo.isClickable() == false) {
            click(nodeInfo.getParent());
        } else {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }
}
