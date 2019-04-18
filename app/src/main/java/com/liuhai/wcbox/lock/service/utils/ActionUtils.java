package com.liuhai.wcbox.lock.service.utils;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

import com.liuhai.wcbox.lock.action.MM_MessageClear;
import com.liuhai.wcbox.lock.action.WeChar_PengyouquanLocation;
import com.liuhai.wcbox.lock.action.WeChat_AutoAddFriends;
import com.liuhai.wcbox.lock.action.WeChat_AutoHuifu;
import com.liuhai.wcbox.lock.action.WeChat_AutoHuifu2;
import com.liuhai.wcbox.lock.action.WeChat_DaShang;
import com.liuhai.wcbox.lock.action.WeChat_DeleteFirends;
import com.liuhai.wcbox.lock.action.WeChat_FirendsCheckDelete;
import com.liuhai.wcbox.lock.action.WeChat_MessageClear;
import com.liuhai.wcbox.lock.action.WeChat_Paishe;
import com.liuhai.wcbox.lock.action.WeChat_QunFa2;
import com.liuhai.wcbox.lock.action.WeChat_RedPackageAction;
import com.liuhai.wcbox.lock.action.WeChat_Zan;
import com.liuhai.wcbox.lock.base.BaseAction;
import com.liuhai.wcbox.lock.utils.MySettings;

/**
 * Created by chenliangj2ee on 2017/5/21.
 * 工具控制器，如抢红包，好友删除检测
 */

public class ActionUtils {


    private BaseAction action01;
    private BaseAction action02;
    private BaseAction action03;
    private BaseAction action04;
    private BaseAction action05;
    private BaseAction action06;
    private BaseAction action07;
    private BaseAction action08;
    private BaseAction action09;
    private BaseAction action10;
    private WeChat_AutoHuifu2 autoHuifu2;
    private WeChat_DaShang daShang;
    private WeChat_AutoAddFriends addFriends;
    private WeChat_QunFa2 qunfa;
    private WeChat_MessageClear messageClear;
    private MM_MessageClear mm_messageClear;


    public void event(AccessibilityService con, AccessibilityEvent event) {


        if (MySettings.isDeleteFriend(con)) {
            if (action02 == null) {
                action02 = new WeChat_FirendsCheckDelete(con);
                //  action02.event(event);
            }
            // if (MySettings.isWeixin(con))
            action02.event(event);
        } else {
            action02 = null;
        }

        if (MySettings.isRedPackage(con)) {
            if (action04 == null) {
                action04 = new WeChat_RedPackageAction(con);
            }
            // if (MySettings.isWeixin(con))
            action04.event(event);
        } else {
            action04 = null;
        }

        if (MySettings.isDeleteFriendAction(con)) {
            if (action05 == null) {
                action05 = new WeChat_DeleteFirends(con);
            }
            //  if (MySettings.isWeixin(con))
            action05.event(event);
        } else {
            action05 = null;
        }


        if (action06 == null) {
            action06 = new WeChar_PengyouquanLocation(con);
        }

//        action06.event(event);

        if (action07 == null) {
            action07 = new WeChat_Paishe(con);
        }
        if (MySettings.isWeixin(con))
            action07.event(event);


        if (action09 == null) {
            action09 = new WeChat_Zan(con);
        }

        if (MySettings.isZan(con)) {
            action09.event(event);
        }


        if (action10 == null) {
            action10 = new WeChat_AutoHuifu(con);
        }
        if (MySettings.isAutoHF(con)) {
            action10.event(event);
        }


        if (autoHuifu2 == null) {
            autoHuifu2 = new WeChat_AutoHuifu2(con);
        }

        if (MySettings.isAutoHF(con)) {
            autoHuifu2.event(event);
        }

        if (addFriends == null && MySettings.issetAddFriend(con)) {
            addFriends = new WeChat_AutoAddFriends(con);
            addFriends.event(event);
        }
        if (MySettings.issetAddFriend(con)) {
            addFriends.event(event);
        }
        if (MySettings.issetAddFriend(con) == false)
            addFriends = null;

        if (qunfa == null) {
            qunfa = new WeChat_QunFa2(con);
        }
        if (MySettings.isQunfa(con)) {
            qunfa.event(event);
        }


        if (messageClear == null) {
            messageClear = new WeChat_MessageClear(con);
        }
        messageClear.event(event);

//        if(mm_messageClear==null)//脉脉自动清空消息
//            mm_messageClear=new MM_MessageClear(con);
//        mm_messageClear.event(event);

    }

}
