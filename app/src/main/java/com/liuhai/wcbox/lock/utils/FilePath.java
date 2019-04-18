package com.liuhai.wcbox.lock.utils;

import android.content.Context;

import java.io.File;

/**
 * Created by chenliangj2ee on 2017/3/2.
 */

public class FilePath {

    /**
     * 根据网络路径，判断本地是否存在该图片
     * @param con
     * @param fileUrl
     * @return
     */
    public static boolean hasImage(Context con,String fileUrl){
        File filePath = new File(con.getExternalCacheDir() + "/image");
        String filename = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        File file = new File(filePath, filename);
        if (file.exists() == false)  //本地缓存广告图不存在
            return false;
            return true;
    }
    /**
     * 根据网络路径，获取本地图片对应的路径
     * @param con
     * @param fileUrl
     * @return
     */
    public static String getImagePath(Context con,String fileUrl){
        String filename = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
         if(hasImage(con,fileUrl)){
             return con.getExternalCacheDir() + "/image" + "/" + filename;
         }else{
             return "";
         }
    }

    /**
     * 根据网络路径，判断本地是否存在该视频
     * @param con
     * @param fileUrl
     * @return
     */
    public static boolean hasVideo(Context con,String fileUrl){
        File filePath = new File(con.getExternalCacheDir() + "/video");
        String filename = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        File file = new File(filePath, filename);
        if (file.exists() == false)  //本地缓存广告图不存在
            return false;
        return true;
    }
    /**
     * 根据网络路径，获取本地视频对应的路径
     * @param con
     * @param fileUrl
     * @return
     */
    public static String getVideoPath(Context con,String fileUrl){
        String filename = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        if(hasVideo(con,fileUrl)){
            return con.getExternalCacheDir() + "/video" + "/" + filename;
        }else{
            return "";
        }
    }

    public static String getFileName(String fileUrl){
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }

    public static String getImageDir(Context con){
        return con.getExternalCacheDir() + "/image";
    }
    public static String getVideoDir(Context con){
        return con.getExternalCacheDir() + "/video";
    }
}
