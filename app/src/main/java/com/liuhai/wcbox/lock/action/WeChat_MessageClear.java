package com.liuhai.wcbox.lock.action;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.liuhai.wcbox.lock.base.BaseAction;
import com.liuhai.wcbox.lock.utils.MySettings;

import java.util.List;

/**
 * Created by chenliangj2ee on 2017/5/17.
 * 微信自动删除那些删除我的人
 */

public class WeChat_MessageClear extends BaseAction {
    private int index = 0;

    public WeChat_MessageClear(AccessibilityService service) {
        super(service);
        index = 0;

    }


    @Override
    public void event(AccessibilityEvent event) {

        if (MySettings.isMessageClear(service) == false)
            return;

        try {


            String packageName = event.getPackageName() == null ? "" : event.getPackageName().toString();

            if (!"com.tencent.mm".equals(packageName)) {
                index = 0;
                return;
            }

            final AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();

            if (nodeInfo == null) {
                return;
            }


            if (index == 0) {//listview item 长按、
                log("当前步骤：" + index);
                List<AccessibilityNodeInfo> listviews = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/bqc");
                if (listviews != null && listviews.size() > 0) {
                    AccessibilityNodeInfo listview = listviews.get(0);
                    if (listview.getChildCount() > 0) {
                        AccessibilityNodeInfo item = listview.getChild(listview.getChildCount() - 1);
                        List<AccessibilityNodeInfo> names = item.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ajc");
                        if (names != null && names.size() > 0) {//判断不是【Windows微信已登录】
//                            if(event.getItemCount()<20&&"android.widget.ListView".equals(event.getClassName().toString())){
//                                MySettings.setMessageClear(service, false);//清空会话结束
//                                return;
//                            }
                            longClick(item);
                            index = 1;
                            return;
                        } else {
                            MySettings.setMessageClear(service, false);//清空会话结束
                            Toast.makeText(service,"清空会话结束",Toast.LENGTH_LONG).show();
                        }

                    }
                }
            }

            if (index == 1) {//删除
                log("当前步骤：" + index);
                List<AccessibilityNodeInfo> menus = nodeInfo.findAccessibilityNodeInfosByText("删除该聊天");
                if (menus != null && menus.size() > 0) {
                    click(menus.get(0));
                    index = 0;
                }
                 menus = nodeInfo.findAccessibilityNodeInfosByText("删除漂流瓶入口");
                if (menus != null && menus.size() > 0) {
                    click(menus.get(0));
                    index = 0;
                }
            }


        } catch (Exception e) {

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
