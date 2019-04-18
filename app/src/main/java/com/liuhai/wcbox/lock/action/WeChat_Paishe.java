package com.liuhai.wcbox.lock.action;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.liuhai.wcbox.lock.base.BaseAction;

import java.util.List;

/**
 * Created by chenliangj2ee on 2017/5/17.
 * 微信自动删除那些删除我的人
 */

public class WeChat_Paishe extends BaseAction {
    public static boolean start = false;
    public static int itemIndex = 0;
    private int index = 0;

    public WeChat_Paishe(AccessibilityService service) {
        super(service);
        index = 0;

    }


    @Override
    public void event(AccessibilityEvent event) {

        if (WeChat_Paishe.start == false)
            return;
        String packageName = event.getPackageName() == null ? "" : event.getPackageName().toString();

        if (!"com.tencent.mm".equals(packageName)) {
            index = 0;
            return;
        }
        int eventType = event.getEventType();

        final AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
        if (nodeInfo == null) {
            return;
        }

        log(event.toString());


        if (index == 0) {
            try {
                log("当前步骤：" + index);
                List<AccessibilityNodeInfo> lists = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/fk");
                if (lists.size() > 0) {
                    AccessibilityNodeInfo info = lists.get(0).getParent();//获得【更多功能按钮】RelativeLayout
                    if (info != null) {
                        log("通讯录..........");
                        info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        index = 1;
                        return;
                    }
                }

            } catch (Exception e) {
                index = 0;
            }

        }

        if (index == 1) {
            try {
                AccessibilityNodeInfo listview = nodeInfo.findAccessibilityNodeInfosByText("拍摄").get(0).getParent().getParent().getParent();//获得拍摄TextView，在获取listview
                if (listview!=null) {
                    AccessibilityNodeInfo info = listview.getChild(WeChat_Paishe.itemIndex);
                    if (info != null) {
                        info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        log("拍摄。。。");
                        index = 0;
                        WeChat_Paishe.start = false;
                    }
                }
            } catch (Exception e) {

            }


        }


    }
}
