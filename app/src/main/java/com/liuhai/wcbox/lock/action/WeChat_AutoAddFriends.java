package com.liuhai.wcbox.lock.action;

import android.accessibilityservice.AccessibilityService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;
import android.widget.Toast;

import com.liuhai.wcbox.lock.base.BaseAction;
import com.liuhai.wcbox.lock.utils.MySettings;

import java.util.List;

/**
 * Created by chenliangj2ee on 2017/5/17.
 */

public class WeChat_AutoAddFriends extends BaseAction {

    private int index = 0;
    private int page;
    private int zanIndex;//当前页第几个赞
    private int totalCount;
    private int preCount = 1;//当前页itemCount;

    private String preText = "";//记录上次点击item时的text;


    public WeChat_AutoAddFriends(AccessibilityService service) {
        super(service);
        index = 0;
    }


    @Override
    public void event(AccessibilityEvent event) {
        try {
            if (MySettings.issetAddFriend(service) == false) {
                index = 0;
                return;
            }
            String packageName = event.getPackageName() == null ? "" : event.getPackageName().toString();

            if (!packageName.contains("com.tencent.mm")) {
                return;
            }
            int eventType = event.getEventType();

            final List<AccessibilityWindowInfo> nodeInfolist = service.getWindows();


            if (nodeInfolist == null) {
                return;
            }
            //  Log.d("liuhai", "自动加好友：" + event.toString());

            if (index == 0) {//item点击进入用户详情
                //       Log.d("liuhai","当前步骤：" + index);
                //       Log.d("liuhai","当前步骤：" + nodeInfolist.toString());
                List<AccessibilityNodeInfo> listviews = null;
                listviews = service.getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.tencent.mm:id/dd2");//获得附近人ListView
                if (listviews == null) {
                    return;
                }
                //   Log.d("liuhai","当前步骤：" + "找到了");
                //     Log.d("liuhai","当前步骤：" + listviews.size());
                //      Log.d("liuhai","列表项的子view：" + listviews.get(0).getChildCount());
                AccessibilityNodeInfo item = listviews.get(0).getChild(preCount);//获得ListView的第count个item
                //       Log.d("liuhaiListview",item.toString());
                if (item != null && item.isClickable()) {
                    if (totalCount < 100) {
                        Log.d("liuhai", "当前步骤：" + "item 点击了。。。。。" + item.toString());
                        preCount++;
                        totalCount++;
                        index = 1;
                        item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    } else {
                        MySettings.setAddFriend(service, false);
                        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                        Toast.makeText(service, "添加结束", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            if (index == 1) {//获取【打招呼】按钮
                Log.d("liuhai", index + "~~");
                List<AccessibilityNodeInfo> sends = null;//获得【打招呼】按钮
                sends = service.getRootInActiveWindow().findAccessibilityNodeInfosByText("发消息");
                if (sends != null && !sends.isEmpty()) {
                    service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    index = 5;
                    return;
                }
                //com.tencent.mm:id/d7v
                List<AccessibilityNodeInfo> dazhaohus = null;//获得【打招呼】按钮
                dazhaohus = service.getRootInActiveWindow().findAccessibilityNodeInfosByText("打招呼");
                if (dazhaohus != null && !dazhaohus.isEmpty()) {

                    for (int i = 0; i < dazhaohus.size(); i++) {
                        dazhaohus.get(i).getParent().getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }

                    index = 2;
                }
            }

            if (index == 2) {//获得输入编辑框Edittext
                log("当前步骤：" + index);
                List<AccessibilityNodeInfo> edits = null; //获得输入编辑框
                edits = service.getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.tencent.mm:id/e0o");

                if (edits != null && !edits.isEmpty()) {
                    ClipboardManager clipboard = (ClipboardManager) service.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("text", "不要加，测试微信助手程序打扰了，谢谢.");
                    clipboard.setPrimaryClip(clip);
                    edits.get(0).performAction(AccessibilityNodeInfo.ACTION_PASTE);
                    index = 3;
                }

            }


            if (index == 3) {////获得【发送】按钮
                log("当前步骤：" + index);
                List<AccessibilityNodeInfo> sends = null; //获得【发送】按钮
                sends = service.getRootInActiveWindow().findAccessibilityNodeInfosByText("发送");
                if (sends != null && !sends.isEmpty()) {
                    sends.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    index = 4;
                }

            }

            if (index == 4) {
                Log.d("liuhai", "当前步骤：" + index);
                //   service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);

                List<AccessibilityNodeInfo> sends = null;//获得【打招呼】按钮
                // sends = service.getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.tencent.mm:id/kb");
                //   Log.d("liuhai", "打招呼：" + sends.size());

                sends = service.getRootInActiveWindow().findAccessibilityNodeInfosByText("打招呼");

                //sends=service.getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ka");
                if (sends != null && !sends.isEmpty()) {
                    Log.d("liuhai", "点击返回按钮了");
                    //sends.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    index = 5;
                } else {
                    index = 4;
                    Log.d("liuhai", "没有找到按钮");
                }
            }
            if (index == 5) {//获得附近人ListView,判断是否进入下一页
                log("当前步骤：" + index);
                try {
                    List<AccessibilityNodeInfo> listviews = null;
                    listviews = service.getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.tencent.mm:id/dd2");//获得附近人ListView
                    if (listviews != null && !listviews.isEmpty()) {
                        if (preCount >= listviews.get(0).getChildCount()) {
                            listviews.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                            preCount = 0;
                        }
                        index = 0;
                    }
                }catch (Exception e){

                    index=4;

                }


            }


        } catch (Exception e) {

            Log.d("liuhai", e.toString());
            //index = 0;
        }

    }
}
