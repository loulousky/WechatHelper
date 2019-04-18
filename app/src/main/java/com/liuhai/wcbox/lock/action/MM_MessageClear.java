package com.liuhai.wcbox.lock.action;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.liuhai.wcbox.lock.base.BaseAction;

import java.util.List;

/**
 * Created by chenliangj2ee on 2017/5/17.
 * 脉脉删除消息列表
 */

public class MM_MessageClear extends BaseAction {
    private int index = 0;

    public MM_MessageClear(AccessibilityService service) {
        super(service);
        index = 0;

    }


    @Override
    public void event(AccessibilityEvent event) {


        try {


            String packageName = event.getPackageName() == null ? "" : event.getPackageName().toString();


            final AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();

            if (nodeInfo == null) {
                return;
            }


            if (index == 0) {//listview item 长按、
                log("当前步骤：" + index);
                List<AccessibilityNodeInfo> listviews = nodeInfo.findAccessibilityNodeInfosByViewId("android:id/list");
                if (listviews != null && listviews.size() > 0) {
                    AccessibilityNodeInfo listview = listviews.get(0);
                    if (listview.getChildCount() > 0) {
                        AccessibilityNodeInfo item = listview.getChild(listview.getChildCount() - 1);
                        longClick(item);
                        index = 1;
                        return;
                    }
                }
            }

            if (index == 1) {//删除
                log("当前步骤：" + index);
                List<AccessibilityNodeInfo> menus = nodeInfo.findAccessibilityNodeInfosByText("删除该消息");
                if (menus != null && menus.size() > 0) {
                    click(menus.get(0));
                    index = 0;
                }
            }


        } catch (
                Exception e
                )

        {

        }

    }


    private void click(AccessibilityNodeInfo info) {
        if (info.isClickable()) {
            info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            click(info.getParent());
        }
    }

    private void longClick(AccessibilityNodeInfo info) {
        if (info.isClickable()) {
            info.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
        } else {
            click(info.getParent());
        }
    }
}
