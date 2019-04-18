package com.liuhai.wcbox.lock.service.utils;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

import com.liuhai.wcbox.lock.floatview.LockView;
import com.liuhai.wcbox.lock.floatview.OneTouchFloatView;
import com.liuhai.wcbox.lock.floatview.WeChat_FloatButton;
import com.liuhai.wcbox.lock.floatview.WeChat_FloatButton_AutoHuiFu;
import com.liuhai.wcbox.lock.utils.MySettings;

/**
 * Created by chenliangj2ee on 2017/5/21.
 * 具有view的行为控制器
 */

public class FloatViewUtils {


    private LockView lockView;
    private WeChat_FloatButton weChat_menu;
    private WeChat_FloatButton_AutoHuiFu floatButton_autoHuiFu;
    private OneTouchFloatView oneTouchFloatView;

    public void event(final AccessibilityService con, final AccessibilityEvent event) {
        if (weChat_menu == null) {
            weChat_menu = new WeChat_FloatButton(con);

        }
       // if (MySettings.isWeixin(con))
            weChat_menu.event(con, event);
        if (floatButton_autoHuiFu == null) {
            floatButton_autoHuiFu = new WeChat_FloatButton_AutoHuiFu(con);
        }
        if (MySettings.isWeixin(con))
            floatButton_autoHuiFu.event(con, event);

        if (lockView == null) {
            lockView = new LockView(con);
        }
        if (MySettings.isWeChatLock(con) && MySettings.isWeixin(con)) {
            lockView.event(con, event);
        }


//        if (oneTouchFloatView == null) {
//            oneTouchFloatView = new OneTouchFloatView(con);
//            oneTouchFloatView.show();
//        }
//        oneTouchFloatView.event(con, event);
    }


}
