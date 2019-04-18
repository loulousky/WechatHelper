package com.liuhai.wcbox.lock.base;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Created by chenliangj2ee on 2017/5/21.
 */

public abstract class BaseAction {

    public AccessibilityService service;

    public BaseAction(AccessibilityService service) {
        this.service = service;
    }

    public abstract void event(final AccessibilityEvent event);

    public void log(String log) {
        if (getClass() == null) {
            Log.i("BaseAction", log + "");
            return;
        }
        if (getClass().getSimpleName() == null) {
            Log.i("BaseAction", log + "");
            return;
        }
        Log.i(getClass().getSimpleName(), log + "");
    }

    public List<AccessibilityNodeInfo> findId(String id) {
        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
        if (nodeInfo != null)
            return nodeInfo.findAccessibilityNodeInfosByViewId(id);
        else
            return null;
    }
    public List<AccessibilityNodeInfo> findText(String text) {
        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
        if (nodeInfo != null)
            return nodeInfo.findAccessibilityNodeInfosByText(text);
        else
            return null;
    }

    public List<AccessibilityNodeInfo> findId(AccessibilityNodeInfo nodeInfo,String id) {
        if (nodeInfo != null)
            return nodeInfo.findAccessibilityNodeInfosByViewId(id);
        else
            return null;
    }
    public List<AccessibilityNodeInfo> findText(AccessibilityNodeInfo nodeInfo,String text) {
        if (nodeInfo != null)
            return nodeInfo.findAccessibilityNodeInfosByText(text);
        else
            return null;
    }
}
