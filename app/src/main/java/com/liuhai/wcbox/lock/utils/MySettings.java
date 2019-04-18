package com.liuhai.wcbox.lock.utils;

import android.content.Context;

import java.text.SimpleDateFormat;

/**
 * Created by chenliangj2ee on 2017/5/25.
 */

public class MySettings {


    /**
     * 微信查杀僵尸
     * @param con
     * @param boo
     */
    public static void setDeleteFriend(Context con, boolean boo) {
        con.getSharedPreferences("MySettings", 0).edit().putBoolean("setDeleteFriend", boo).commit();
    }

    public static boolean isDeleteFriend(Context con) {
        return con.getSharedPreferences("MySettings", 0).getBoolean("setDeleteFriend", false);
    }

    /**
     * 微信自动抢红包
     * @param con
     * @param boo
     */
    public static void setRedPackage(Context con, boolean boo) {
        con.getSharedPreferences("MySettings", 0).edit().putBoolean("setRedPackage", boo).commit();
    }

    public static boolean isRedPackage(Context con) {
        return con.getSharedPreferences("MySettings", 0).getBoolean("setRedPackage", true);
    }


    /**
     * 微信朋友圈定位
     * @param con
     * @param boo
     */
    public static void setLocation(Context con, boolean boo) {
        con.getSharedPreferences("MySettings", 0).edit().putBoolean("setLocation", boo).commit();
    }

    public static boolean isLocation(Context con) {
        return con.getSharedPreferences("MySettings", 0).getBoolean("setLocation", false);
    }


    /**
     * 微信删除好友
     * @param con
     * @param boo
     */
    public static void setDeleteFriendAction(Context con, boolean boo) {
        con.getSharedPreferences("MySettings", 0).edit().putBoolean("setDeleteFriendAction", boo).commit();
    }

    public static boolean isDeleteFriendAction(Context con) {
        return con.getSharedPreferences("MySettings", 0).getBoolean("setDeleteFriendAction", false);
    }


    /**
     * 上一次展示广告的时间
     * @param con
     * @param boo
     */
    public static void setAdsShowTime(Context con) {
        con.getSharedPreferences("MySettings", 0).edit().putLong("setAdsShowTime", System.currentTimeMillis()).commit();
    }

    public static boolean isAdsShowTimeOut(Context con) {
        int ran=0;// (int) (Math.random()*30);
        return System.currentTimeMillis()-con.getSharedPreferences("MySettings", 0).getLong("setAdsShowTime", 0)>(ran+60)*60*000;
    }


    /**
     * 是否开启微信锁频
     * @param con
     * @param boo
     */
    public static void setWeChatLock(Context con,boolean boo) {
        con.getSharedPreferences("MySettings", 0).edit().putBoolean("setWeChatLock",boo).commit();
    }

    public static boolean isWeChatLock(Context con) {
        return  con.getSharedPreferences("MySettings", 0).getBoolean("setWeChatLock", false) ;
    }


    /**
     * 设置微信锁
     * @param con
     * @param boo
     */
    public static void setWeChatLockPass(Context con,String boo) {
        con.getSharedPreferences("MySettings", 0).edit().putString("setWeChatLockPass",boo).commit();

    }

    public static String getWeChatLockPas(Context con) {
        return  con.getSharedPreferences("MySettings", 0).getString("setWeChatLockPass","") ;
    }


    /**
     * 微信是否退出
     * @param con
     * @param boo
     */
    public static void setWeChatExit(Context con,boolean boo) {
        con.getSharedPreferences("MySettings", 0).edit().putBoolean("setWeChatExit",boo).commit();
    }

    public static boolean isWeChatExit(Context con) {
        return  con.getSharedPreferences("MySettings", 0).getBoolean("setWeChatExit", false) ;
    }


    /**
     * 微信是否退出
     * @param con
     * @param boo
     */
    public static void setZan(Context con,boolean boo) {
        con.getSharedPreferences("MySettings", 0).edit().putBoolean("setZan",boo).commit();
    }

    public static boolean isZan(Context con) {
        return  con.getSharedPreferences("MySettings", 0).getBoolean("setZan", false) ;
    }

    /**
     * 微信是否退出
     * @param con
     * @param boo
     */
    public static void setJiQiRen(Context con,String name,boolean boo) {
        con.getSharedPreferences("MySettings", 0).edit().putBoolean("setJiQiRen"+name,boo).commit();
    }

    public static boolean isJiQiRen(Context con,String name) {
        return  con.getSharedPreferences("MySettings", 0).getBoolean("setJiQiRen"+name, false) ;
    }
    /**
     * 微信是否退出
     * @param con
     * @param boo
     */
    public static void setUserName(Context con,String name) {
        con.getSharedPreferences("MySettings", 0).edit().putString("setUserName",name).commit();
    }

    public static String getUserName(Context con) {
        return  con.getSharedPreferences("MySettings", 0).getString("setUserName", null) ;
    }

    /**
     * 微信是否退出
     * @param con
     * @param boo
     */
    public static void setDaShang(Context con,boolean boo) {
        con.getSharedPreferences("MySettings", 0).edit().putBoolean("setDaShang",boo).commit();
    }

    public static boolean isDaShang(Context con) {
        return  con.getSharedPreferences("MySettings", 0).getBoolean("setDaShang", false) ;
    }
    /**
     * 微信自动抢红包
     * @param con
     * @param boo
     */
    public static void setAutoHF(Context con, boolean boo) {
        con.getSharedPreferences("MySettings", 0).edit().putBoolean("setAutoHF", boo).commit();
    }

    public static boolean isAutoHF(Context con) {
        return con.getSharedPreferences("MySettings", 0).getBoolean("setAutoHF", false);
    }

    /**
     * 微信自动抢红包
     * @param con
     * @param boo
     */
    public static void setLuping(Context con, boolean boo) {
        con.getSharedPreferences("MySettings", 0).edit().putBoolean("setLuping", boo).commit();
    }

    public static boolean isLuping(Context con) {
        return con.getSharedPreferences("MySettings", 0).getBoolean("setLuping", false);
    }


    /**
     * 微信自动抢红包
     * @param con
     * @param boo
     */
    public static void setWeixin(Context con, boolean boo) {
        con.getSharedPreferences("MySettings", 0).edit().putBoolean("setWeixin", boo).commit();
    }

    public static boolean isWeixin(Context con) {
        return con.getSharedPreferences("MySettings", 0).getBoolean("setWeixin", true);
    }

    /**
     * 微信自动抢红包
     * @param con
     * @param boo
     */
    public static void setAddFriend(Context con, boolean boo) {
        con.getSharedPreferences("MySettings", 0).edit().putBoolean("setAddFriend", boo).commit();
    }

    public static boolean issetAddFriend(Context con) {
        return con.getSharedPreferences("MySettings", 0).getBoolean("setAddFriend", false);
    }

    /**
     * 微信自动抢红包
     * @param con
     * @param boo
     */
    public static void setQunfa(Context con, boolean boo) {
        con.getSharedPreferences("MySettings", 0).edit().putBoolean("setQunfa", boo).commit();
    }

    public static boolean isQunfa(Context con) {
        return con.getSharedPreferences("MySettings", 0).getBoolean("setQunfa", false);
    }


    /**
     * 判断是否清空会话
     * @param con
     * @param boo
     */
    public static void setMessageClear(Context con, boolean boo) {
        con.getSharedPreferences("MySettings", 0).edit().putBoolean("setMessageClear", boo).commit();
    }

    public static boolean isMessageClear(Context con) {
        return con.getSharedPreferences("MySettings", 0).getBoolean("setMessageClear", false);
    }



    /**
     * 当天创建群的次数，超过了N次，则当天不能再创建群，需要等到明天
     * @param con
     * @param boo
     */
    public static void setCheckFinish(Context con, boolean boo) {
        con.getSharedPreferences("MySettings", 0).edit().putBoolean("setCheckFinish", boo).commit();
    }

    public static boolean isCheckFinish(Context con) {
        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyyMMdd");
        return con.getSharedPreferences("MySettings", 0).getBoolean("setCheckFinish", false);
    }
    /**
     * 当天创建群的次数，超过了N次，则当天不能再创建群，需要等到明天
     * @param con
     * @param boo
     */
    public static void setCheckUserCount(Context con, int count) {
        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyyMMdd");
        con.getSharedPreferences("MySettings", 0).edit().putInt("setCheckUserCount"+format.format(System.currentTimeMillis()), count).commit();
    }

    public static int getCheckUserCount(Context con) {
        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyyMMdd");
        return con.getSharedPreferences("MySettings", 0).getInt("setCheckUserCount"+format.format(System.currentTimeMillis()), 0);
    }
    /**
     * 记录上次建群选择好友时，翻到了第几页
     * @param con
     * @param boo
     */
    public static void setCreateQunPageNum(Context con, int count) {
        con.getSharedPreferences("MySettings", 0).edit().putInt("setCreateQunPageNum", count).commit();
    }

    public static int getCreateQunPageNum(Context con) {
        return con.getSharedPreferences("MySettings", 0).getInt("setCreateQunPageNum", 0);
    }




    /**
     * 判断是否分享到微信
     * @param con
     * @param boo
     */
    public static void setShareWeChat(Context con, boolean boo) {
        con.getSharedPreferences("MySettings", 0).edit().putBoolean("setShareWeChat", boo).commit();
    }

    public static boolean isShareWeChat(Context con) {
        return con.getSharedPreferences("MySettings", 0).getBoolean("setShareWeChat", false);
    }
}
