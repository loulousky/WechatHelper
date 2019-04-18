package com.liuhai.wcbox.lock.action;

import android.accessibilityservice.AccessibilityService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.liuhai.wcbox.lock.base.BaseAction;
import com.liuhai.wcbox.lock.floatview.Float_ShareingFriendsView;
import com.liuhai.wcbox.lock.utils.MySettings;
import com.lzx.lock.utils.ToastUtil;

import java.util.List;

/**
 * Created by chenliangj2ee on 2017/5/17.
 */

public class WeChat_QunFa2 extends BaseAction {

    private int index = 0;
    private int totalNumber;
    private int preNumber;
    private int selectPage;//当前页itemCount;
    boolean isLastPage;
    boolean is200;
    public static String message="";
    Float_ShareingFriendsView dialog;

    public WeChat_QunFa2(AccessibilityService service) {
        super(service);
        index = 0;
    }


    @Override
    public void event(AccessibilityEvent event) {
        try {

            if (MySettings.isQunfa(service) == false) {
                finish();
                return;
            }
            Log.e("WeChat_QunFa2", "测试开始");

            String packageName = event.getPackageName() == null ? "" : event.getPackageName().toString();

            if (!packageName.contains("com.tencent.mm")) {
                return;
            }

            final AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();

            if (nodeInfo == null) {
                return;
            }


//            if(dialog==null){
//                dialog=new Float_ShareingFriendsView(service);
//                dialog.setMessages("正在分享...");
//                dialog.show();
//            }


            Log.e("WeChat_QunFa2", "找到列表");
            List<AccessibilityNodeInfo> mlistviews = findId("com.tencent.mm:id/mi");

            if (mlistviews != null && mlistviews.size() > 10 && "android.widget.ListView".equals(event.getClassName())) {
                if (event.getItemCount() - 1 == event.getToIndex()) {
                    isLastPage = true;
                    Log.i("WeChat_QunFa2", "滑到底了....");
                }
            }

            Log.e("WeChat_QunFa2", "有没有继续");
            Log.i("WeChat_QunFa2", "index:" + index);
            if (index == 0) {//点击【搜索】

                try {
                    List<AccessibilityNodeInfo> sousuo= nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/iq");
                 //   AccessibilityNodeInfo sousuo = nodeInfo.getChild(0).getChild(0).getChild(0).getChild(0).getChild(1).getChild(0).getChild(1).getChild(0);
                   // &&"搜索".equals(sousuo.getContentDescription().toString())
                    if (sousuo != null && sousuo.size()>0) {
                        click(sousuo.get(0));
                        index = 1;
                    }
                } catch (Exception e) {

                }
                return;
            }
            if (index == 1) {//【搜索】Edittext内粘贴群发
                List<AccessibilityNodeInfo> sousuoEdit = findId("com.tencent.mm:id/kh");
                if (sousuoEdit != null && sousuoEdit.size() > 0&&sousuoEdit.get(0).getText().equals("搜索")) {
                    ToastUtil.showToast("输入群发");
                    ClipboardManager clipboard = (ClipboardManager) service.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("text", "群发");
                    clipboard.setPrimaryClip(clip);
                    sousuoEdit.get(0).performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                    Bundle arguments = new Bundle();
                    arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "群发");
                    sousuoEdit.get(0).performAction(AccessibilityNodeInfo.ACTION_SET_TEXT,arguments);
                   // sousuoEdit.get(0).performAction(AccessibilityNodeInfo.ACTION_PASTE);
                    Log.i("WeChat_QunFa2", "粘贴");

                    Thread.sleep(1000);
                    index = 2;
                }
                return;
            }


            if (index == 2) {//找到【群发助手】item，点击进入
                List<AccessibilityNodeInfo> qunfas = nodeInfo.findAccessibilityNodeInfosByText("群发助手");
                if (qunfas != null && qunfas.size() > 0) {
                    click(qunfas.get(0));
                    index = 3;
                }
                return;
            }

            if (index == 3) {//找到【新建群发】，点击新建群发
                List<AccessibilityNodeInfo> qunfas = nodeInfo.findAccessibilityNodeInfosByText("新建群发");
                if (qunfas != null && qunfas.size() > 0) {
                    click(qunfas.get(0));
                    if (is200)
                        index = 4;
                    else
                        index = 100;
                    selectPage = 0;
                }

                return;

            }


            if (index == 4) {//找到checkbox多选框选中

                boolean click = false;
                List<AccessibilityNodeInfo> checkboxs = findId("com.tencent.mm:id/a08");
                if (checkboxs != null && checkboxs.size() > 0) {
                    for (int i = 0; i < checkboxs.size(); i++) {
                        if (checkboxs.get(i).isChecked() == false) {
                           if (preNumber <= totalNumber) {
                                preNumber++;
                            } else {
                                click(checkboxs.get(i));
                                click = true;
                                totalNumber++;
                                preNumber++;
                            }
                        }
                    }
                }
                if (click)
                    selectPage++;
                if (selectPage < 5 && isLastPage == false) {  //如果当前选择，没有到指定人数，则翻页
                    List<AccessibilityNodeInfo> listviews = findId("com.tencent.mm:id/i9");
                    if (listviews != null && listviews.size() > 0) {
                        listviews.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                        index = 4;
                    }

                } else {
                    index = 5;
                }
                Thread.sleep(800);
                return;
            }
//
            selectAll();
            if (index == 5) {
                List<AccessibilityNodeInfo> nexts = findText("下一步");
                if (nexts != null && nexts.size() > 0) {
                    click(nexts.get(0));
                    index = 6;
                }
                return;
            }

            if (index == 6) {//找到消息【编辑框】
                List<AccessibilityNodeInfo> edits = findId("com.tencent.mm:id/amb");

                if (edits != null && edits.size() > 0) {
                    ClipboardManager clipboard = (ClipboardManager) service.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("text", WeChat_QunFa2.message);
                    clipboard.setPrimaryClip(clip);
                    edits.get(0).performAction(AccessibilityNodeInfo.ACTION_FOCUS);
                    edits.get(0).performAction(AccessibilityNodeInfo.ACTION_PASTE);
                    index = 7;
                }
                return;
            }

            if (index == 7) {
                List<AccessibilityNodeInfo> sends = findText("发送");
                if (sends != null && sends.size() > 0) {
                    click(sends.get(0));
                    preNumber = 0;
                    Thread.sleep(1000);
                    index = 8;
                    service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    Thread.sleep(1000);
                    service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    Thread.sleep(1000);
                    service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    Thread.sleep(1000);
                }
                return;
            }

            if (index == 8) {
                if (isLastPage) {
                    service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    finish();
                } else {
                    index = 3;
                }
                return;
            }
//

        } catch (Exception e) {
            finish();
            log("群发异常，结束。。。");
            e.printStackTrace();
        }

    }

    private void finish() {
        log("群发结束,群发人数:" + totalNumber);
        index = 0;
        selectPage = 0;
        isLastPage = false;
        MySettings.setQunfa(service,false);
        totalNumber = 0;
        WeChat_QunFa2.message="";
        if(dialog!=null&&"正在分享...".equals(dialog.getMessage())){
            Toast.makeText(service, "分享完成", Toast.LENGTH_SHORT).show();
            MySettings.setShareWeChat(service,true);
        }
        if(dialog!=null)
        dialog.remove();
        dialog=null;
    }

    private boolean hasText(AccessibilityNodeInfo item, String text) {
        List<AccessibilityNodeInfo> infos = item.findAccessibilityNodeInfosByText(text);
        if (infos != null && infos.size() > 0) {
            return true;
        }
        return false;
    }

    private void click(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null)
            return;
        if (nodeInfo.isClickable()) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            click(nodeInfo.getParent());
        }
    }


    private void selectAll() {
        if (index == 100) {
            List<AccessibilityNodeInfo> selectAlls = findText("全选");
            if (selectAlls != null && selectAlls.size() > 0) {
                click(selectAlls.get(0));
                index = 101;
            }
            return;
        }
        if (index == 101) {
            List<AccessibilityNodeInfo> selectAlls = findText("下一步(");
            if (selectAlls != null && selectAlls.size() > 0) {
                String numberStr = selectAlls.get(0).getText().toString();
                numberStr = numberStr.replace("下一步(", "");
                numberStr = numberStr.replace(")", "");
                int num = Integer.parseInt(numberStr);
                if (num < 200) {
                    is200 = false;
                    index = 5;
                    isLastPage=true;
                } else {
                    is200 = true;
                    List<AccessibilityNodeInfo> notselectAlls = findText("不选");
                    if (notselectAlls != null && notselectAlls.size() > 0) {
                        click(selectAlls.get(0));
                        index = 4;
                    }
                    return;
                }
            }

        }
    }
}
