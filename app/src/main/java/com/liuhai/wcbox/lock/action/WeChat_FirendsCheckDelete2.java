package com.liuhai.wcbox.lock.action;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.liuhai.wcbox.lock.base.BaseAction;
import com.liuhai.wcbox.lock.utils.MySettings;
import com.liuhai.wcbox.lock.floatview.CheckingFriendFloatView;

import java.util.List;

/**
 * Created by chenliangj2ee on 2017/5/17.
 * 微信好友删除检测
 */

public class WeChat_FirendsCheckDelete2 extends BaseAction {

    private String userNames = "";
    private int index = 0;
    private int pageNumber = 0;//记录上次滑动到了第几页，
    private int pageNumberCount;//记录当前滑动了第第几页
    private boolean checkFinish = false;//检测是否结束
    private String preUserName;//上一页第一个检测的名称，用于记录是否滑到最底下，
    private int selectUserNumber;//记录当前建群时选择的人数
    public static String checkUserName = "";
    public static int totleUserNumber;

    private CheckingFriendFloatView checking;

    public WeChat_FirendsCheckDelete2(AccessibilityService service) {
        super(service);
        index = 0;
        checking = new CheckingFriendFloatView(service);

    }


    @Override
    public void event(AccessibilityEvent event) {

        int eventType = event.getEventType();

        final AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();

        if (nodeInfo == null) {
            return;
        }


        if (index == 0) {
            log("开始检查............................................");
//            checking.show();
            List<AccessibilityNodeInfo> listviews = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/hr");//获得消息编辑框
            if (listviews == null || listviews.isEmpty())
                return;

            try {
                AccessibilityNodeInfo info = nodeInfo.getChild(0).getChild(0).getChild(0).getChild(0).getChild(1).getChild(0).getChild(1).getChild(1);
                if (info != null) {
                    info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    index = 1;
                    return;
                }
            } catch (Exception e) {

            }

        }


        if (index == 1) {
            log("发起群聊............................................");

            List<AccessibilityNodeInfo> listviews = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/lm");//获得消息编辑框
            if (listviews == null || listviews.isEmpty())
                return;

            try {
                AccessibilityNodeInfo info = listviews.get(0);
                if (info != null) {
                    info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    index = 10;
                    return;
                }
            } catch (Exception e) {

            }

        }

        if (index == 10) {
            log("滑动到第" + pageNumber + "页");

            List<AccessibilityNodeInfo> listviews = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/es");//获得消息编辑框
            if (listviews == null || listviews.isEmpty())
                return;
            AccessibilityNodeInfo listview = listviews.get(0);

            for (int i = pageNumberCount; i < 10; i++) {//记录上次滑动到的位置pageNumber，如果当前位置pageNumberCount没到上次位置，则再次往下滑动
                listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                pageNumberCount++;
                return;
            }
        }


        if (index == 2 && eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
            log("选择好友............................................");

            List<AccessibilityNodeInfo> listviews = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/es");//获得消息编辑框
            if (listviews == null || listviews.isEmpty())
                return;
            AccessibilityNodeInfo listview = listviews.get(0);

            for (int i = pageNumberCount; i < pageNumber; i++) {//记录上次滑动到的位置pageNumber，如果当前位置pageNumberCount没到上次位置，则再次往下滑动
                listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                pageNumberCount++;
                return;
            }


            try {

                for (int i = 0; i < listview.getChildCount(); i++) {
                    List<AccessibilityNodeInfo> item = listview.getChild(i).findAccessibilityNodeInfosByViewId("com.tencent.mm:id/nh");//获得消息编辑框
                    if (!item.isEmpty()) {
                        if (item.get(0).isChecked() == false) {
                            listview.getChild(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            WeChat_FirendsCheckDelete2.totleUserNumber++;
                            selectUserNumber++;
                        }


                    }
                    List<AccessibilityNodeInfo> usernames = listview.getChild(i).findAccessibilityNodeInfosByViewId("com.tencent.mm:id/j2");//获得用户名


                    if (!usernames.isEmpty()) {
                        String username = usernames.get(0).getText().toString();
                        if (preUserName == null) {//preUserName为null时，说明为第一页，手动出发一次滑动事件
                            listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                            pageNumberCount++;
                            preUserName = "";
                        }
                        if (preUserName.equals(username)) {
                            checkFinish = true;
                        } else {
                            preUserName = username;
                        }
                        log("text:" + preUserName);
                        WeChat_FirendsCheckDelete2.checkUserName = username;
                    }


                }
                if (selectUserNumber > 30) {
                    index = 3;
                    selectUserNumber = 0;
                } else {
                    index = 2;
                    pageNumber++;
                }

            } catch (Exception e) {

            }

        }

        if (index == 3) {
            List<AccessibilityNodeInfo> confirmButton = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/gd");//获得消息编辑框

            if (confirmButton.isEmpty() == false) {
                log("群聊开始创建............................................");
                confirmButton.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                index = 4;
            }


        }
        if (index == 4) {
            List<AccessibilityNodeInfo> messages = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/if");//获得消息编辑框
            for (int i = 0; i < messages.size(); i++) {
                AccessibilityNodeInfo info = messages.get(i);
                info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
            String text = event.getText().toString();
            if (text.contains("你无法邀请未添加你为好友的用户进去群聊")) {
                text = text.replace("你无法邀请未添加你为好友的用户进去群聊，请先向", "");
                text = text.replace("发送朋友验证申请。对方通过验证后，才能加入群聊。", "");
                log("你被以下好友删除：" + text);
                userNames += userNames + text.replace("[", "").replace("]", "") + "、";
                index = 5;
            } else if (text.contains("请注意隐私安全")) {
                text = text.replace("你无法邀请未添加你为好友的用户进去群聊，请先向", "");
                text = text.replace("发送朋友验证申请。对方通过验证后，才能加入群聊。", "");
                log("无好友删除你。。。：");
                index = 5;
            }


        }

        if (index == 5) {
            try {
                AccessibilityNodeInfo info = nodeInfo.getChild(0).getChild(0).getChild(0).getChild(0).getChild(1).getChild(0).getChild(1).getChild(0);

                if (info != null) {
                    info.performAction(AccessibilityNodeInfo.ACTION_CLICK);//进入群聊详情
                    index = 6;
                }
            } catch (Exception e) {

            }
        }

        if (index == 6) {
            try {
                List<AccessibilityNodeInfo> messages = nodeInfo.findAccessibilityNodeInfosByViewId("android:id/list");//获得ListView

                if (messages != null && messages.size() > 0) {
                    messages.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                    log("进入群聊详情向下滑动");
                    index = 7;
                }
            } catch (Exception e) {

            }

        }


        if (index == 7) {
            try {
                List<AccessibilityNodeInfo> delete = nodeInfo.findAccessibilityNodeInfosByText("删除并退出");//获得消息编辑框
                if (delete != null && delete.isEmpty() == false) {
                    for (int i = 0; i < delete.size(); i++) {
                        delete.get(i).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);//删除并且退出
                        delete.get(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);//删除并且退出
                        log("删除并且退出。。。");
                        index = 8;
                    }
                } else {
                    index = 6;
                }
            } catch (Exception e) {

            }

        }


        if (index == 8 && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {

            if (event.getText() == null)
                return;
            if (event.getText().toString().contains("删除并退出后，将不再接收此群聊信息, 取消, 确定")) {
                try {
                    List<AccessibilityNodeInfo> ok = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/abz");//获得 确定

                    if (ok != null && ok.size() > 0) {
                        ok.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        log("确定退出群。。。");
//                        pageNumber++;
                        pageNumberCount = 0;
                        index = 0;
                    }
                } catch (Exception e) {

                }
            } else {
                List<AccessibilityNodeInfo> delete = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/alf");//获得消息编辑框
                if (delete.isEmpty() == false) {
                    AccessibilityNodeInfo likai = delete.get(0).getChild(0);
                    if (likai != null) {
                        likai.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        log("离开群聊。。。");
                        log("删除我的孙子列表：" + userNames);
//                        pageNumber++;
                        pageNumberCount = 0;
                        index = 0;
                    }
                }
            }

            finish();
        }


    }

    private void finish() {
        if (checkFinish) {
            MySettings.setDeleteFriend(service, false);
            index = 0;
            checkFinish = false;

        }
    }


}
