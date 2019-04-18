package com.liuhai.wcbox.lock.action;

import android.accessibilityservice.AccessibilityService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.SystemClock;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.liuhai.wcbox.lock.base.BaseAction;
import com.liuhai.wcbox.lock.floatview.CheckFinishDialog;
import com.liuhai.wcbox.lock.floatview.CheckingFriendFloatView;
import com.liuhai.wcbox.lock.utils.MySettings;
import com.lzx.lock.utils.ToastUtil;

import java.util.List;

/**
 * Created by chenliangj2ee on 2017/5/17.
 * 微信好友删除检测
 */

public class WeChat_FirendsCheckDelete extends BaseAction {
    public static int dayCreateCount = 20;

    private int index = -3;
    private String userNames = "";
    private String checkNames = "";
    private int page;
    private int forwardPage;
    public static boolean checkFinish = false;//检测是否结束
    public static String checkUserName = "";
    public static int totleUserNumber;


    private CheckingFriendFloatView checking;
    private int preSeletePage;//本次建群翻了几页

    private int prexqpage = 0;//详情向下找+号

    public WeChat_FirendsCheckDelete(AccessibilityService service) {
        super(service);
        index = -3;
        page = MySettings.getCreateQunPageNum(service);//获取上次查询检测到的页数。
        //      checking = new CheckingFriendFloatView(service);
//        checking.show();
    }

    public String getLauncherPackageName(Context context) {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            return "";
        }
        //如果是不同桌面主题，可能会出现某些问题，这部分暂未处理
        if (res.activityInfo.packageName.equals("android")) {
            return "";
        } else {
            return res.activityInfo.packageName;
        }


    }

    private String lastUserName = "-1";

    @Override
    public void event(AccessibilityEvent event) {
        try {
            int eventType = event.getEventType();


            if (MySettings.isDeleteFriend(service) == false) {
                index = -3;
                //      finish();
                return;
            }

            page = MySettings.getCreateQunPageNum(service);//获取上次查询检测到的页数。

            String packageName = event.getPackageName() == null ? "" : event.getPackageName().toString();

            if (!packageName.contains("com.tencent.mm")) {

                return;
            }
            final AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();

            if (nodeInfo == null) {
                return;
            }

            if (getLauncherPackageName(service).equals(event.getPackageName().toString())) {
                MySettings.setDeleteFriend(service, false);
                // checking.remove();
            }
            log("index：" + index);

            if (index == -3) {//获得输入编辑框Edittext 查找有没有存在的群聊有的话删除重新创建
                log("当前步骤：" + index);
                List<AccessibilityNodeInfo> edits = null; //获得输入编辑框
                edits = service.getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.tencent.mm:id/kh");

                if (edits != null && !edits.isEmpty()) {

                    if (putClipboard1(edits.get(0), "微商盒子查杀群")) {

                        SystemClock.sleep(500);


                        index = -2;

                    } else {

                        return;
                    }


//                    ClipboardManager clipboard = (ClipboardManager) service.getSystemService(Context.CLIPBOARD_SERVICE);
//                    ClipData clip = ClipData.newPlainText("text", "啪啪查杀群");
//                    clipboard.setPrimaryClip(clip);
//                //    edits.get(0).performAction(AccessibilityNodeInfo.ACTION_FOCUS);
//                    Bundle arguments = new Bundle();
//                    arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "啪啪查杀群");
//                 //   edits.get(0).performAction(AccessibilityNodeInfo.ACTION_PASTE);
//                    edits.get(0).performAction(AccessibilityNodeInfo.ACTION_SET_TEXT,arguments);
//
//                    if(edits.get(0).getText()==null||edits.get(0).getText().equals("")){
//
//                    }else{
//                        index = -2;
//                    }

                }

            }


            //查找啪啪群的结果
            if (index == -2) {
                List<AccessibilityNodeInfo> papaqun = null; //获得输入编辑框
                papaqun = service.getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.tencent.mm:id/q0");
                //     papaqun= service.getRootInActiveWindow().findAccessibilityNodeInfosByText("啪啪查杀群");

                log(papaqun.toString());
                if (papaqun != null && !papaqun.isEmpty()) {

                    log(papaqun.size() + "~~~");
                    // for (int i=0;i<papaqun.size();i++){
                    //  if(papaqun.get(i).getText().equals("啪啪查杀群")){
                    log("找到了结果趴趴查杀群");
                    //点击进去群聊
                    //  papaqun.get(0).getParent().getParent().getParent().getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    service.getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.tencent.mm:id/bwh").
                            get(0).getChild(1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    index = 20;

                    return;

                    //   }
                    //   }


                } else {

                    //点击返回准备创建群聊 并设置检测从头开始
                    forwardPage=0;
                    page = 0;
                    MySettings.setCreateQunPageNum(service, 0);//当检测到底部时，记录页数，下次检测，从第一次检测，否则从上次记录的页数开始检测。
                    service.getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ke").get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    //      service.getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ke").get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);;
                    index = -1;
                }

            }


            List<AccessibilityNodeInfo> error = nodeInfo.findAccessibilityNodeInfosByText("频率过快，请明日再试");
            if (error != null && error.size() > 0) {

                ToastUtil.showToast("次数被限制，明日再试");
                checkFinish = true;
                service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                Thread.sleep(1000);
                service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                finish();
                MySettings.setCheckFinish(service, true);
                return;
            }
            if (index == -1) {

                log("index：" + index);
                SystemClock.sleep(500);
                List<AccessibilityNodeInfo> menus = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/iq");//获得左上角【+】按钮
                log("index：" + menus.toString());
                for (AccessibilityNodeInfo n :
                        menus) {
                    if (n != null) {
                        if (n.getParent().getContentDescription() != null) {
                            if (n.getParent().getContentDescription().equals("更多功能按钮")) {
                                log("index：" + "更多功能");
                                n.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                SystemClock.sleep(500);
                                index = 0;
                            }
                        }
                    }
                }

                log("index：" + "走这里");
                List<AccessibilityNodeInfo> listviews = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/i9");//获取listview
                if (listviews != null && !listviews.isEmpty()){
                    log("index：" + "尽然进来了"+listviews.toString());
                    index = 1;
                }


                //点击返回准备创建群聊
                service.getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.tencent.mm:id/kf").get(0).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);




            }
            if (index == 0) {

                SystemClock.sleep(500);
                log("发起群聊1............................................" + index);
                List<AccessibilityNodeInfo> items = nodeInfo.findAccessibilityNodeInfosByText("发起群聊");
                if (items == null || items.isEmpty()) {

                    //点击返回准备创建群聊
                    service.getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.tencent.mm:id/kf").get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);


                    index = -1;
                    return;
                }

                SystemClock.sleep(500);

                try {
                    items.get(0).getParent().getParent().getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);

                } catch (Exception e) {

                    List<AccessibilityNodeInfo> listviews = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/i9");//获取listview
                    if (listviews == null || listviews.isEmpty())
                        return;
                    index = -1;


                }
                index = 1;
                log("发起群聊2............................................" + index);

            }
            if (index == 1) {
                List<AccessibilityNodeInfo> listviews = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/i9");//获取listview
                if (listviews == null || listviews.isEmpty())
                    return;
                log("forwardPage：" + forwardPage + "    page:" + page);
                if (forwardPage < page) {
                    listviews.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                    forwardPage++;
                } else {
                    index = 2;
                }

                log("下一页............................................" + index);
            }


            if (index == 2) {
                if (preSeletePage < 3) {
                    List<AccessibilityNodeInfo> checkboxs = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/a08");//获取ChcekBox
                    List<AccessibilityNodeInfo> names = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/q0");//获取用户名称
                    String username = "";
                    for (int i = 0; i < checkboxs.size(); i++) {


                        if (i < names.size()) {
                            if (names.get(i) != null && names.get(i).getText() != null) {
                                username += names.get(i).getText().toString() + "、";

                            }
                        }


                        if (!checkboxs.get(i).isChecked()) {
                            click(checkboxs.get(i));
                            WeChat_FirendsCheckDelete.totleUserNumber++;//记录已经检测了多少人
                            if (checkNames.contains(username) == false) {
                                checkNames = checkNames + username;
                                MySettings.setCheckUserCount(service, MySettings.getCheckUserCount(service) + 1);
                                log("当天检测好友人数：" + MySettings.getCheckUserCount(service));
                            }


                        }
                    }

                    //      checking.checking(username);
                    List<AccessibilityNodeInfo> listviews = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/i9");//获取listview
                    if (preSeletePage < 3) {

                        if (listviews != null && listviews.size() > 0) {
                            listviews.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                            page++;

                            MySettings.setCreateQunPageNum(service, page);
                            Log.i("worini", "当前页:" + page);
                        }
                    } else {
                        index = 3;
                        //    MySettings.setCreateQunPageNum(service, page);
                    }
                    preSeletePage++;

                    if (listviews != null && listviews.size() > 0 && "android.widget.ListView".equals(event.getClassName().toString())) {

                        int itemCount = event.getItemCount();
                        int toIndex = event.getToIndex();
                        if (event.getItemCount() - 1 == event.getToIndex()) {//当toIndex等于listview的itemCount值时，则判定为滑动到listview最下面了，检测结束
                            checkFinish = true;
                            Toast.makeText(service, "检测结束...", Toast.LENGTH_SHORT).show();
                            finish();//测试代码
                            index = -3;
                            page = 0;
                        }

                    }


                } else {
                    index = 3;
                }
                log("preSeletePage:" + preSeletePage);
            }

            if (index == 3) {

//                //测试代码
//                preSeletePage = 0;
//                forwardPage = 0;
//                service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
//                index=0;
//                //测试代码

                List<AccessibilityNodeInfo> buttons = nodeInfo.findAccessibilityNodeInfosByText("确定");//获得消【确定】
                preSeletePage = 0;
                forwardPage = 0;
                if (buttons.isEmpty() == false) {
                    log("群聊开始创建............................................");
                    buttons.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    preSeletePage = 0;
                    index = 4;

                }

            }


            if (index == 4) {
                Thread.sleep(1000);
                List<AccessibilityNodeInfo> messages = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/nu");//获得消息编辑框

                if (messages != null && !messages.isEmpty()) {
                    for (int i = 0; i < messages.size(); i++) {
                        AccessibilityNodeInfo info = messages.get(i);
                        info.performAction(AccessibilityNodeInfo.ACTION_CLICK);

                        if (info.getText() != null) {
                            String text = info.getText().toString();
                            if (text.contains("你无法邀请未添加你为好友的用户进去群聊")) {
                                text = text.replace("你无法邀请未添加你为好友的用户进去群聊，请先向", "");
                                text = text.replace("发送朋友验证申请。对方通过验证后，才能加入群聊。", "");
                                Log.d("dawang", "你被以下好友删除：" + text);
                                text = text.replace("[", "").replace("]", "");
                                if (userNames.contains(text) == false) {
                                    userNames = userNames + text + "&";
                                }


                            }
                        }
                    }

                }
                if (messages.size() > 0) {
                    index = 5;
                }

                //对话框中显示的被删除的人命
                List<AccessibilityNodeInfo> name = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/d6y");//获得消息编辑框
                if (name != null && !name.isEmpty()) {
                    String text = name.get(0).getText().toString();
                    if (text.contains("未把你添加到通讯录")) {
                        if (userNames.contains(text) == false) {
                            int index = text.indexOf("未把你添加到通讯录，需要发送验证申请，等对方通过。");
                            userNames = text.substring(0, index);
                            userNames = userNames + text + "&";
                        }
                    }

                }

                List<AccessibilityNodeInfo> sure = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/az_");
                if (sure != null && !sure.isEmpty()) {
                    sure.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);

                }


                if (sure.size() > 0) {
                    index = 20;
                }


            }


            //返回到首页
//            if (index == 5) {
//                AccessibilityNodeInfo info = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/k2").get(0);
//
//                if (info != null) {
//                    service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
//                    index = 6;
//                }
//
//            }
//
//
//            //循环建立多个群聊
//            if(index==6&&!checkFinish) {
//
//                try {
//                    log("下一页主页点击菜单进入............................................");
//                    AccessibilityNodeInfo menus =nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/jf").get(0).getChild(1).getChild(1);//获得左上角【+】按钮
//                    if (menus == null)
//                        return;
//                    log("没有点击");
//                    //service.performGlobalAction(AccessibilityService.G);
//                    menus.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                  //  click(menus);
//                    index=0;
//                }catch (Exception e){
//
//                }
//
//            }

            if (index == 5) {
                AccessibilityNodeInfo info = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/jy").get(0);

                if (info != null) {
                    info.performAction(AccessibilityNodeInfo.ACTION_CLICK);//进入群聊详情
                    index = 6;
                }

            }


            if (index == 6) {
                //判断群名是不是啪啪查杀群
                AccessibilityNodeInfo info = nodeInfo.findAccessibilityNodeInfosByViewId("android:id/summary").get(0);
                if (!info.getText().equals("微商盒子查杀群")) {
                    AccessibilityNodeInfo pa = info.getParent().getParent().getParent().getParent().getParent().getParent();

                    if (pa != null) {
                        pa.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        index = 7;//改名字
                    }
                } else {

                    index = 20;

                }
            }

            if (index == 7) {
                //改群名
                AccessibilityNodeInfo info = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/dab").get(0);
                if (info != null) {

                    if (putClipboard1(info, "微商盒子查杀群")) {
                        SystemClock.sleep(500);
//                        ClipboardManager clipboard = (ClipboardManager) service.getSystemService(Context.CLIPBOARD_SERVICE);
//                        ClipData clip = ClipData.newPlainText("text", "啪啪查杀群");
//                        clipboard.setPrimaryClip(clip);
//                        info.performAction(AccessibilityNodeInfo.ACTION_PASTE);
                        index = 8;
                    }
                }
            }

            if (index == 8) {
                //fan
                List<AccessibilityNodeInfo> sends = null; //获得【发送】按钮
                sends = service.getRootInActiveWindow().findAccessibilityNodeInfosByText("保存");
                if (sends != null && !sends.isEmpty()) {
                    sends.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    index = 20;
                }
            }

            if (index == 20) {
                //进入详情 删除除了自己之外的全部人
                List<AccessibilityNodeInfo> info = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/jy");

                if (info != null&&!info.isEmpty()) {
                    info.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);//进入群聊详情
                    index = 21;
                }else{
                    List<AccessibilityNodeInfo> add = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/e0c");

                    if (add != null && !add.isEmpty())
                        index=21;

                }


            }

            if (index == 21) {
                List<AccessibilityNodeInfo> add = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/e0c");

                if (add != null && !add.isEmpty()) {

                    for (int i = 0; i < add.size(); i++) {

                        if (add.get(i).getContentDescription() != null) {
                            if (add.get(i).getContentDescription().equals("删除成员")) {
                                log("删除成员");
                                add.get(i).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                SystemClock.sleep(500);
                                //重新添加
                                index = 22;
                                return;

                            }
                        }

                    }

                    index=9;
                    return ;

                }
            }

            if (index == 22) {

                List<AccessibilityNodeInfo> listviews = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/e4j");//获取listview
                if (listviews == null || listviews.isEmpty())
                    return;


                log("删除列表");
                int ij=0;
              //  while (ij!=3) {

                //    ij++;
                    log("循环删除");
                    List<AccessibilityNodeInfo> checkboxs = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/aus");//获取ChcekBox
                    for (int i = 0; i < checkboxs.size(); i++) {
                       // if (!checkboxs.get(i).isChecked()) {
                            click(checkboxs.get(i));
                      //  }
                    }

                  //  listviews.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
             //   }


                index=23;

            }

            //删除
            if(index==23){
                List<AccessibilityNodeInfo> buttons = nodeInfo.findAccessibilityNodeInfosByText("删除");//获得消【确定】
                if (buttons.isEmpty() == false) {
                    buttons.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    SystemClock.sleep(500);
                    index = 24;

                }


            }

            //删除
            if(index==24){
              //  List<AccessibilityNodeInfo> buttons = nodeInfo.findAccessibilityNodeInfosByText("确定");//获得消【确定】


                List<AccessibilityNodeInfo> buttons=nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/az_");
                if (buttons.isEmpty() == false) {
                    buttons.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    index = 20;

                }


            }





            if (index == 9) {

                List<AccessibilityNodeInfo> add = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/e0c");

                if (add != null && !add.isEmpty()) {

                    for (int i = 0; i < add.size(); i++) {

                        if (add.get(i).getContentDescription() != null) {
                            if (add.get(i).getContentDescription().equals("添加成员")) {
                                log("添加成员");
                                add.get(i).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                SystemClock.sleep(500);
                                //重新添加
                                index = 1;
                                return;

                            }
                        }

                    }

//                        List<AccessibilityNodeInfo> checkboxs = nodeInfo.findAccessibilityNodeInfosByViewId("android:id/list");
//                        if (checkboxs != null && !checkboxs.isEmpty()) {
//
//                            checkboxs.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
//
//                            return;
//                        }


                }


            }

            if (index == 10) {
                AccessibilityNodeInfo info = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/jy").get(0);

                if (info != null) {
                    info.performAction(AccessibilityNodeInfo.ACTION_CLICK);//进入群聊详情
                    index = 11;
                }

            }

            if (index == 11) {
                List<AccessibilityNodeInfo> messages = nodeInfo.findAccessibilityNodeInfosByViewId("android:id/list");//获得聊天信息ListView

                if (messages != null && messages.size() > 0) {
                    messages.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                    log("进入群聊详情向下滑动");
                    index = 12;
                }

            }


//            if (index == 6) {
//                List<AccessibilityNodeInfo> messages = nodeInfo.findAccessibilityNodeInfosByViewId("android:id/list");//获得聊天信息ListView
//
//                if (messages != null && messages.size() > 0) {
//                    messages.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
//                    log("进入群聊详情向下滑动");
//                    index = 7;
//                }
//
//
//            }


            if (index == 12) {
                List<AccessibilityNodeInfo> deletes = nodeInfo.findAccessibilityNodeInfosByText("删除并退出");//获得消息编辑框
                if (deletes != null && deletes.size() > 0) {
                    for (int i = 0; i < deletes.size(); i++) {
                        click(deletes.get(i));
                        index = 13;
                        log("删除并且退出。。。");
                    }
                } else {
                    index = 11;
                }


            }
//
//
            if (index == 13) {

                if (event.getText() == null)
                    return;
                if (event.getText().toString().contains("删除并退出后，将不再接收此群聊信息, 取消, 确定")) {
                    List<AccessibilityNodeInfo> ok = nodeInfo.findAccessibilityNodeInfosByText("确定"); //获得 确定

                    if (ok != null && ok.size() > 0) {
                        ok.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        finish();
                        log("确定退出群。。。");
                        index = -3;
                    }

                } else {
                    List<AccessibilityNodeInfo> delete = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ko");//获得离开群聊对应的listview
                    if (delete.isEmpty() == false) {
                        AccessibilityNodeInfo likai = delete.get(0).getChild(0);
                        if (likai != null) {
                            likai.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            finish();
                            log("离开群聊。。。");
                            log("删除我的孙子列表：" + userNames);
                            index = -3;
                        }
                    }


                }


            }

        } catch (Exception e) {
            //   MySettings.setDeleteFriend(service, false);
            e.printStackTrace();
            //  index = 0;
        }
    }


    private void click(AccessibilityNodeInfo info) {
        Log.i("FirendsCheckDelete", "click。。。。。");
        if (info == null)
            return;
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
            //        checking.remove();
            if (userNames == null || "".equals(userNames)) {
                Toast.makeText(service, "检测结束,没有发现死人", Toast.LENGTH_SHORT).show();
            } else {
                CheckFinishDialog dialog = new CheckFinishDialog(service, userNames);
                dialog.show();
                WeChat_DeleteFirends.usernames = userNames;
                userNames = "";
                Toast.makeText(service, "检测结束...", Toast.LENGTH_SHORT).show();
            }
            preSeletePage = 0;
            forwardPage=0;

            page = 0;
            MySettings.setCreateQunPageNum(service, 0);//当检测到底部时，记录页数，下次检测，从第一次检测，否则从上次记录的页数开始检测。
            checkNames = "";

            MySettings.setDeleteFriend(service, false);
            MySettings.setCreateQunPageNum(service, page);
        }

    }


    //自动为edittext粘贴上文字内容
    public Boolean putClipboard1(AccessibilityNodeInfo edittext, String text) {
        if (edittext != null) {
            ClipboardManager clipboard = (ClipboardManager) service.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("text", text);
            clipboard.setPrimaryClip(clip);
            //焦点（n是AccessibilityNodeInfo对象）
            edittext.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
            ////粘贴进入内容
            return edittext.performAction(AccessibilityNodeInfo.ACTION_PASTE);
            //发送
            //...
        }
        return false;
    }


}
