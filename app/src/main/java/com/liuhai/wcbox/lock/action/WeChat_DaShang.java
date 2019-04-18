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
 * 微信抢红包
 */

public class WeChat_DaShang extends BaseAction {


    public WeChat_DaShang(AccessibilityService service) {
        super(service);
    }


    @Override
    public void event(AccessibilityEvent event) {

        if(event.getEventType()==AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED)
            return ;


        if (service == null)
            return;
        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
        if (nodeInfo == null)
            return;

        List<AccessibilityNodeInfo> titles = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/gs");//会话title
        if (titles != null && titles.size() > 0) {
            String username = titles.get(0).getText().toString();
            if (!"".equals(username)) {
                MySettings.setUserName(service, username);
                log(username);
            }

        }


        List<AccessibilityNodeInfo> messages = nodeInfo.findAccessibilityNodeInfosByText("红包金额9.90元");
        if (messages != null && messages.size() > 0) {

            if("旺财".equals(MySettings.getUserName(service)) ){
                Toast.makeText(service, "打赏成功，谢谢使用", Toast.LENGTH_SHORT).show();
                MySettings.setDaShang(service,true);
            }

        }

        List<AccessibilityNodeInfo> messages2 = nodeInfo.findAccessibilityNodeInfosByText("1个红包共9.90元");
        if (messages2 != null && messages2.size() > 0) {

            if("旺财".equals(MySettings.getUserName(service))){
                Toast.makeText(service, "打赏成功，谢谢使用", Toast.LENGTH_SHORT).show();
                MySettings.setDaShang(service,true);
            }

        }

    }





}
