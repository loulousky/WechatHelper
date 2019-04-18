package com.liuhai.wcbox.lock.action;

import android.accessibilityservice.AccessibilityService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.liuhai.wcbox.lock.base.BaseAction;
import com.liuhai.wcbox.lock.utils.MySettings;
import com.robot.bean.ChatMessage;
import com.robot.tools.HttpUtils;

import java.util.List;

/**
 * Created by chenliangj2ee on 2017/5/17.
 * 机器人回复
 */

public class WeChat_AutoHuifu2 extends BaseAction {

    private int index = 0;
    private String lastMessage;
    private String resultMessage;
    List<AccessibilityNodeInfo> edits;

    public WeChat_AutoHuifu2(AccessibilityService service) {
        super(service);
        index = 0;
    }

    /**
     * TYPE_NOTIFICATION_STATE_CHANGED; EventTime: 32823036; PackageName: com.tencent.mm; MovementGranularity: 0; Action: 0 [ ClassName: android.app.Notification; Text: [陈谅: 逛街呵]; ContentDescription: null; ItemCount: -1; CurrentItemIndex: -1; IsEnabled: false; IsPassword: false; IsChecked: false; IsFullScreen: false; Scrollable: false; BeforeText: null; FromIndex: -1; ToIndex: -1; ScrollX: -1; ScrollY: -1; MaxScrollX: -1; MaxScrollY: -1; AddedCount: -1; RemovedCount: -1; ParcelableData: Notification(pri=1 contentView=com.tencent.mm/0xa030053 headsUpContentView=/0xa030057 bigContentView=null vibrate=[] sound=null tick defaults=0x0 flags=0x101 color=0x00000000 category=msg vis=PRIVATE) ]; recordCount: 0
     *
     * @param event
     */
    @Override
    public void event(AccessibilityEvent event) {


        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
        String packageName = event.getPackageName() == null ? "" : event.getPackageName().toString();
        if (nodeInfo == null || !"com.tencent.mm".equals(packageName)) {
            return;
        }

        List<AccessibilityNodeInfo> titles2 = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/k3");//会话title
        if (titles2 != null && titles2.size() > 0) {
            String title=titles2.get(0).getText().toString();
            if(MySettings.isJiQiRen(service,title)==false){
                return ;
            }
        }

        /**
         * 处理通知栏信息
         * <p>
         * 如果是微信红包的提示信息,则模拟点击
         *
         * @param event
         */


        if (index==0&&event.getEventType() != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            try {
                // List<AccessibilityNodeInfo> listview = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/a5j");//获得列表listview
                AccessibilityNodeInfo listview = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/alc").get(0).getChild(0);
                if (listview!=null) {
                    AccessibilityNodeInfo lastItem = listview.getChild(listview.getChildCount() - 1);//获取listview最后一个item
                    //获取item中头像view
                    AccessibilityNodeInfo itemHeadInfos = lastItem.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ns").get(0);
                    //获取当前会话用户的name(顶部)
                    AccessibilityNodeInfo titleUserName = titles2.get(0);
                    //如果名称一样，说明是对方发的、只回复对方发的消息
                    if (itemHeadInfos.getContentDescription().toString().contains(titleUserName.getText().toString())) {
                        List<AccessibilityNodeInfo> items = lastItem.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/nu");//获得消息TextView
                        if (items != null && items.size() > 0) {

                            items.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);//点击消息TextView，用来获取上面的文字
                            index=1;
                        }
                    }
                    edits = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/amb");//获得编辑框
                }
            } catch (Exception e) {

                index = 0;
            }
        }


        if (index == 1) {
            try {

                String message = event.getText().toString().replace("[","").replace("]","");
                if (message != null && !message.equals(lastMessage)) {
                    chat(message);
                }
            } catch (Exception e) {

                index = 0;
            }

        }

        if (index == 2) {
            try {
                List<AccessibilityNodeInfo> edits = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/amb");//获得编辑框
                List<AccessibilityNodeInfo> titles = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/k3");//获得回话标题，含有“()”的，则为群聊
                if (edits != null && edits.size() > 0) {
                    ClipboardManager clipboard = (ClipboardManager) service.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("text", resultMessage);
                    clipboard.setPrimaryClip(clip);
                    edits.get(0).performAction(AccessibilityNodeInfo.ACTION_PASTE);
                    index = 3;
                }


            } catch (Exception e) {

                index = 0;
            }

        }

        if (index == 3) {
            try {
                List<AccessibilityNodeInfo> fasongs = nodeInfo.findAccessibilityNodeInfosByText("发送");//获得赞按钮
                if (fasongs != null && fasongs.size() > 0) {
                    fasongs.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    index = 0;
                }
            } catch (Exception e) {

                index = 0;
            }


        }
        Log.i("WeChat_AutoHuifu2", "index:"+index);
    }


    // 4.发送消息聊天
    private void chat(final String mes) {
        // 3.发送你的消息，去服务器端，返回数据
        new Thread() {
            public void run() {
                ChatMessage chat = HttpUtils.sendMessage(mes);
                Message message = new Message();
                message.what = 0x1;
                message.obj = chat.getMessage();
                if(mes.contains("\"")&&mes.length()<5){
                    message.obj = "别发语音，我现在不方便听...(＾▽＾)";
                }
                handler.sendMessage(message);
                Log.i("WeChat_AutoHuifu2", mes + ":" + chat.getMessage());
            }

            ;
        }.start();
        lastMessage = mes;
    }


    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            if (msg.what == 0x1) {
                if (msg.obj != null) {
                    resultMessage = (String) msg.obj;
                    index = 2;
                    if (edits != null) {
                        edits.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }
            }
        }

        ;
    };
}