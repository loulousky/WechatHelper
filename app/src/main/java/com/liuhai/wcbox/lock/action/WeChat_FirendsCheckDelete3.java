package com.liuhai.wcbox.lock.action;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.liuhai.wcbox.lock.base.BaseAction;
import com.liuhai.wcbox.lock.floatview.CheckFinishDialog;
import com.liuhai.wcbox.lock.floatview.CheckingFriendFloatView;
import com.liuhai.wcbox.lock.utils.MySettings;

import java.util.List;

/**
 * Created by chenliangj2ee on 2017/5/17.
 * 微信好友删除检测
 */

public class WeChat_FirendsCheckDelete3 extends BaseAction {

    private String userNames = "";
    private int index = 0;
    private int pageNumber = 1;//记录上次滑动到了第几页，
    private int prePageNumber = 1;//记录上次滑动到了第几页，
    public static boolean checkFinish = false;//检测是否结束
    public static String checkUserName = "";
    public static int totleUserNumber;


    private CheckingFriendFloatView checking;
    private int preSeletePage;//本次建群翻了几页

    public WeChat_FirendsCheckDelete3(AccessibilityService service) {
        super(service);
        index = 0;
        checking = new CheckingFriendFloatView(service);

    }


    private String lastUserName = "-1";

    @Override
    public void event(AccessibilityEvent event) {
        try {
            int eventType = event.getEventType();

            final AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();

            if (nodeInfo == null) {
                return;
            }

//            checking.show();
            if (index == 0) {
                log("开始检查............................................" + pageNumber);

                List<AccessibilityNodeInfo> menus = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/fr");//获得左上角【+】按钮
                if (menus == null || menus.isEmpty())
                    return;

                click(menus.get(0));
                index = 1;
                prePageNumber = 1;
                return;


            }


            if (index == 1) {
                log("发起群聊............................................");

                List<AccessibilityNodeInfo> items = nodeInfo.findAccessibilityNodeInfosByText("发起群聊");
                if (items == null || items.isEmpty())
                    return;
                click(items.get(0));
                index = 2;
                return;


            }

            if (index == 2) {
                log("滑动到第" + prePageNumber + "页" + pageNumber);

                List<AccessibilityNodeInfo> listviews = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/f_");//获取listview
                if (listviews == null || listviews.isEmpty())
                    return;
                AccessibilityNodeInfo listview = listviews.get(0);

                if (prePageNumber < pageNumber) {
                    listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);//记录上次滑动到的位置pageNumber，如果当前位置pageNumberCount没到上次位置，则再次往下滑动
                    prePageNumber++;
                } else {
                    index = 3;
                }

            }


            if (index == 3 && eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
                log("选择好友............................................");

                List<AccessibilityNodeInfo> listviews = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/pc");//获得
                if (listviews == null || listviews.isEmpty())
                    return;
                AccessibilityNodeInfo listview = listviews.get(0);


                for (int i = 0; i < listview.getChildCount(); i++) {
                    List<AccessibilityNodeInfo> item = listview.getChild(i).findAccessibilityNodeInfosByViewId("com.tencent.mm:id/nh");//获得消息编辑框
                    if (!item.isEmpty()) {
                        if (item.get(0).isChecked() == false) {
                            listview.getChild(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            WeChat_FirendsCheckDelete3.totleUserNumber++;//记录已经检测了多少人
                        }


                    }


                    List<AccessibilityNodeInfo> usernames = listview.getChild(i).findAccessibilityNodeInfosByViewId("com.tencent.mm:id/j2");//获得用户名

                    String username = "";
                    if (!usernames.isEmpty()) {
                        username = usernames.get(0).getText().toString();
                        WeChat_FirendsCheckDelete3.checkUserName = username;
                    }

                    checking.refreshText(username, WeChat_FirendsCheckDelete3.totleUserNumber + "");
                    log("text:" + username);

                    if (i == listview.getChildCount() - 1) {
                        if (lastUserName.equals(username)) {
                            checkFinish = true;//检查完毕
                            log("检测完毕");

                        }

                        lastUserName = username;
                    }

                }
                preSeletePage++;
                ;

                if (preSeletePage < 3) {//当前建群时，添加的人数页数，当小于3页数，再次向下滑动，继续选择
                    listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                    index = 3;

                } else {//当本次建群满足3页，进入下一步
                    preSeletePage = 0;//下一步
                    index = 4;

                    //以下测试代码
                    {
                        index = 0;
                        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                        finish();
                    }
                }
                pageNumber++;

                //以下测试代码
                if (checkFinish) {
                    index = 0;
                    service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    finish();
                }


            }

            if (index == 4) {
                List<AccessibilityNodeInfo> confirmButton = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/gd");//获得消息编辑框

                if (confirmButton.isEmpty() == false) {
                    log("群聊开始创建............................................");
                    confirmButton.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    index = 5;
                }


            }
            if (index == 5) {
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
                    index = 6;
                } else if (text.contains("请注意隐私安全")) {
                    text = text.replace("你无法邀请未添加你为好友的用户进去群聊，请先向", "");
                    text = text.replace("发送朋友验证申请。对方通过验证后，才能加入群聊。", "");
                    log("无好友删除你。。。：");
                    index = 6;
                }


            }

            if (index == 6) {
                AccessibilityNodeInfo info = nodeInfo.getChild(0).getChild(0).getChild(0).getChild(0).getChild(1).getChild(0).getChild(1).getChild(0);

                if (info != null) {
                    info.performAction(AccessibilityNodeInfo.ACTION_CLICK);//进入群聊详情
                    index = 7;
                }

            }

            if (index == 7) {
                List<AccessibilityNodeInfo> messages = nodeInfo.findAccessibilityNodeInfosByViewId("android:id/list");//获得ListView

                if (messages != null && messages.size() > 0) {
                    messages.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                    log("进入群聊详情向下滑动");
                    index = 8;
                }


            }


            if (index == 8) {
                List<AccessibilityNodeInfo> delete = nodeInfo.findAccessibilityNodeInfosByText("删除并退出");//获得消息编辑框
                if (delete != null && delete.isEmpty() == false) {
                    for (int i = 0; i < delete.size(); i++) {
                        delete.get(i).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);//删除并且退出
                        delete.get(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);//删除并且退出
                        log("删除并且退出。。。");
                        index = 9;
                    }
                } else {
                    index = 7;
                }


            }


            if (index == 9 && eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {

                if (event.getText() == null)
                    return;
                if (event.getText().toString().contains("删除并退出后，将不再接收此群聊信息, 取消, 确定")) {
                    List<AccessibilityNodeInfo> ok = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/abz");//获得 确定

                    if (ok != null && ok.size() > 0) {
                        ok.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        log("确定退出群。。。");
                        index = 0;
                    }

                } else {
                    List<AccessibilityNodeInfo> delete = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/alf");//获得消息编辑框
                    if (delete.isEmpty() == false) {
                        AccessibilityNodeInfo likai = delete.get(0).getChild(0);
                        if (likai != null) {
                            likai.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            log("离开群聊。。。");
                            log("删除我的孙子列表：" + userNames);
                            index = 0;
                        }
                    }
                }

                finish();
            }

        } catch (Exception e) {
            MySettings.setDeleteFriend(service,false);
            index=0;
        }
    }


    private void click(AccessibilityNodeInfo info) {
        if (info != null && info.isClickable()) {
            info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            click(info.getParent());
        }
    }

    private void finish() {
        if (checkFinish) {
            MySettings.setDeleteFriend(service, false);
            index = 0;
            checkFinish = false;
            checking.remove();
            CheckFinishDialog dialog = new CheckFinishDialog(service,"");
            dialog.show();

        }
    }


}
