package com.liuhai.wcbox.lock.action;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.liuhai.wcbox.lock.base.BaseAction;
import com.liuhai.wcbox.lock.floatview.FriendDeleteingDialog;
import com.liuhai.wcbox.lock.utils.MySettings;

import java.util.List;

/**
 * Created by chenliangj2ee on 2017/5/17.
 * 微信自动删除那些删除我的人
 */

public class WeChat_DeleteFirends extends BaseAction {
    public static boolean start = false;
    private int index = 0;
    public static String usernames = "";
    private FriendDeleteingDialog dialog;
    private int number;
    private boolean isLastItem;
    private int itemIndex;
    private boolean islastPage;
    private int isTab02Click;

    public WeChat_DeleteFirends(AccessibilityService service) {
        super(service);
        index = 0;
    }


    @Override
    public void event(AccessibilityEvent event) {
        log("Index:" + index);
        try {



            if (event.getPackageName() == null)
                return;

            String packageName = event.getPackageName().toString();

            if (!packageName.contains("com.tencent.mm")) {
                return;
            }

            final AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();

            if (nodeInfo == null) {
                return;
            }
//            if (dialog == null && MySettings.isDeleteFriendAction(service)) {
//                dialog = new FriendDeleteingDialog(service);
//                dialog.show();
//            }

            if (index == 0) {//点击通讯录
                List<AccessibilityNodeInfo> tabs = findText("通讯录");
                if (tabs != null && tabs.size() > 0  ) {
                    click(tabs.get(0));
                    isTab02Click++;

                }
                if (isTab02Click >= 2)
                    index = 1;

            } else if (index == 1) {

                List<AccessibilityNodeInfo> listviews = findId("com.tencent.mm:id/mi");//获取通讯录列表
                if (listviews == null || listviews.isEmpty()) {
                    return;
                }
                AccessibilityNodeInfo listview = listviews.get(0);


                for (int i = itemIndex; i < listview.getChildCount(); i++, itemIndex++) {
                    List<AccessibilityNodeInfo> lastinfos = listview.getChild(i).findAccessibilityNodeInfosByText("位联系人");
                    if (lastinfos != null && lastinfos.size() > 0) {
                        finish();
                    } else

                        {

                        List<AccessibilityNodeInfo> usernameTexts = findId(listview.getChild(i), "com.tencent.mm:id/ng");//获取用户名view
                        if (usernameTexts.isEmpty() == false) {
                            String username = usernameTexts.get(0).getText().toString();
                            Log.i("WeChat_DeleteFirends", "username:" + username);
                            if (WeChat_DeleteFirends.usernames.contains(username)) {
                                WeChat_DeleteFirends.usernames = WeChat_DeleteFirends.usernames.replace(username, "");//检测完的，从usernames中删除
                                index = 2;
                                number++;//删除人数记录
                                dialog.refreshText(username, number + "");
                                Thread.sleep(1000);
                                click(listview.getChild(i));//点击进入好友详情
                                return;
                            }

                        }
                    }


                }
                index = 6;

            } else if (index == 2) {
                AccessibilityNodeInfo info = findId("com.tencent.mm:id/jy").get(0).getChild(1).getChild(0);
                if (info != null) {
                    click(info);
                    index = 3;
                }


            } else if (index == 3) {
                List<AccessibilityNodeInfo> listviews = findId("com.tencent.mm:id/d78");//好友菜单列表往下滑动
                if (listviews != null && listviews.isEmpty() == false) {
                    listviews.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                    index = 4;
                }

            } else if (index == 4) {
                List<AccessibilityNodeInfo> deletes = nodeInfo.findAccessibilityNodeInfosByText("删除");
                if (deletes != null && deletes.isEmpty() == false) {
                    click(deletes.get(0));
                    index = 5;
                }


            } else if (index == 5) {

                List<AccessibilityNodeInfo> deletes = findId("com.tencent.mm:id/az_");//【删除】按钮
//                List<AccessibilityNodeInfo> cancels = nodeInfo.findAccessibilityNodeInfosByText("取消");//测试代码，假删除

                if (deletes != null && deletes.isEmpty() == false) {
                    click(deletes.get(0));
//                    service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);//测试代码，假删除后，返回
                    index = 6;
                }


            }


            if (index == 6) {

                List<AccessibilityNodeInfo> listviews = findId("com.tencent.mm:id/mi");//获取通讯录列表
                if (listviews == null || listviews.isEmpty()) {
                    return;
                }
                AccessibilityNodeInfo listview = listviews.get(0);
                if (itemIndex < listview.getChildCount()) {//如果当前页循环还没到当前页最后一个，如果不是，则继续循环
                    index = 1;
                }
                if (islastPage == false) {//如果当前item是最后一个，且还不是最后一页，则向下滑动一页。
                    listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                    itemIndex = 0;
                    index = 7;
                }

                log("islastPage:" + islastPage + "  itemIndex:" + itemIndex + "   getChildCount:" + listview.getChildCount());
            } else if (index == 7) {//判断是不是最后一页
                if ("android.widget.ListView".equals(event.getClassName())) {
                    if (event.getItemCount() == event.getToIndex() - 1) {
                        islastPage = true;
                    }
                }
                index = 1;

            }

        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }


    }


    private void finish() {
        if (dialog != null)
            dialog.remove();

        MySettings.setDeleteFriendAction(service, false);
        dialog = null;
        index = 0;
        Toast.makeText(service, "僵尸清理完毕...", Toast.LENGTH_SHORT).show();
        log("好友删除结束............................................");
    }

    private void click(AccessibilityNodeInfo info) {
        if (info.isClickable()) {
            info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            click(info.getParent());
        }
    }
}
