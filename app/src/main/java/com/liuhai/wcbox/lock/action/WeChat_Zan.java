package com.liuhai.wcbox.lock.action;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.liuhai.wcbox.lock.base.BaseAction;
import com.liuhai.wcbox.lock.utils.MySettings;

import java.util.List;

/**
 * Created by chenliangj2ee on 2017/5/17.
 */

public class WeChat_Zan extends BaseAction {

    private int index = 0;
    private int page;
    private int zanIndex;//当前页第几个赞
    private int zanCount;


    public WeChat_Zan(AccessibilityService service) {
        super(service);
        index = 0;
    }


    @Override
    public void event(AccessibilityEvent event) {

        if( MySettings.isZan(service)==false)
            return ;
        log("event.................");
        Log.i("WeChat_Zan", "微信点赞...");
        String packageName = event.getPackageName() == null ? "" : event.getPackageName().toString();

//        if (!"com.tencent.mm".equals(packageName)) {
//            index = 0;
//            return;
//        }


        if (!packageName.contains("com.tencent.mm")) {
            index = 0;

            return;
        }
        final AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();

        if (nodeInfo == null) {
            return;
        }


        List<AccessibilityNodeInfo> listviews = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/eew");//获得朋友圈ListView

        if (listviews == null || listviews.isEmpty()) {
            //如果当前页面没有找到。可以判断出当前不是朋友圈，则返回
            return;
        }


        if (index == 0) {
            try {
                log("当前步骤：" + index);
                List<AccessibilityNodeInfo> pingluns = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ee2");//获得评论按钮
                if (pingluns == null || pingluns.isEmpty()) {//如果当前页，没有找到评论按钮，则进入下一页
                    listviews.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                    page++;
                    zanIndex=0;
                    if( page==10){
                        Toast.makeText(service, "自动点赞"+page+"页，点赞"+zanCount+"次", Toast.LENGTH_SHORT).show();
                        MySettings.setZan(service,false);
                        page=0;
                        zanCount=0;
                    }
                } else {
                    if (zanIndex < pingluns.size()) {//如果当前赞按钮，还没点击结束，则点击下一个赞
                        pingluns.get(zanIndex++).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        index = 1;
                    } else {//如果当前赞按钮，点击结束，则进入下一页
                        listviews.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                        page++;
                        zanIndex=0;
                        if( page==10){
                            Toast.makeText(service, "自动点赞"+page+"页，点赞"+zanCount+"次", Toast.LENGTH_SHORT).show();
                            MySettings.setZan(service,false);
                            page=0;
                            zanCount=0;
                        }
                    }

                }


            } catch (Exception e) {
                e.printStackTrace();
                index = 0;
            }

        }

        if (index == 1) {
            try {
                log("当前步骤：" + index);
                List<AccessibilityNodeInfo> zan = nodeInfo.findAccessibilityNodeInfosByText("赞");//获得赞按钮
                if (zan != null || zan.isEmpty()==false) {
                    zan.get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    zanCount++;
                }else{//没找到赞按钮、找到的是取消按钮，则点击评论按钮，取消弹框
                    List<AccessibilityNodeInfo> pingluns = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ee2");//获得评论按钮
                    if(pingluns!=null&&pingluns.isEmpty()==false)
                        pingluns.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
                index=0;
            } catch (Exception e) {
                e.printStackTrace();
                index=0;
            }


        }


    }
}
