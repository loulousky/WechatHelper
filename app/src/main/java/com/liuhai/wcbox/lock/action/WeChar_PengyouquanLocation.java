package com.liuhai.wcbox.lock.action;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.liuhai.wcbox.lock.base.BaseAction;

import java.util.List;

/**
 * Created by chenliangj2ee on 2017/5/21.
 */

public class WeChar_PengyouquanLocation extends BaseAction {
    public int index;//步骤
    public static boolean start = false;

    public String text = "[11, 10月, 老爸托人给我们捎来的梨太多了，吃不完，都快烂了，我突发奇想煮梨水喝，别看这梨其貌不扬，煮出来的冰糖梨水不是一般的好喝，酸酸甜甜的，这是我喝过的最好喝的饮料，没有之一！冻冰箱里，一个星期不用买饮料了[胜利], 共2张]";

    public WeChar_PengyouquanLocation(AccessibilityService service) {
        super(service);
    }

    @Override
    public void event(AccessibilityEvent event) {

//        if (WeChar_PengyouquanLocation.start == false)
//            return;


        String packageName = event.getPackageName() == null ? "" : event.getPackageName().toString();
        if (!"com.tencent.mm".equals(packageName))
            return;
        int eventType = event.getEventType();

        final AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();

        if (nodeInfo == null) {
            return;
        }

        log("index" + index + "  :" + event.toString());

        if (index == 0) {
            try {

                if (nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/cp7") == null)
                    return;
                AccessibilityNodeInfo listview = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/cp7").get(0);

                if (listview != null) {


                    for (int i = 0; i < listview.getChildCount(); i++) {
                        if (listview.getChild(i) == null)
                            continue;
                        List<AccessibilityNodeInfo> texts = listview.getChild(i).findAccessibilityNodeInfosByViewId("com.tencent.mm:id/b4e");
                        if(texts!=null&&texts.isEmpty()==false){
                            AccessibilityNodeInfo item = texts.get(0);
                            if (item != null) {
                                String text = item.getText() == null ? "" : item.getText().toString();
                                if (this.text.contains(text) && !"[]".equals(text) && !"".equals(text)) {
                                    log("找到字符：" + text);
                                    index = 1;
                                    break;
                                }
                            }
                        }


                    }
                    if (index != 1)
                        listview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);

                } else {
                    log("listview 为null");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}
