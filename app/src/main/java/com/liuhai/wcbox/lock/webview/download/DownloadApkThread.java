package com.liuhai.wcbox.lock.webview.download;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import com.liuhai.wcbox.lock.utils.AppHelp;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class DownloadApkThread extends Thread {
    private String downloadUrl;
    private File saveFile;
    private String fileName;
    private Context context;
    private NotificationManager notificationManager;// 状态栏通知管理类
    NotificationCompat.Builder mBuilder;
    
    private Timer timer;// 定时器，用于更新下载进度
    private TimerTask task;// 定时器执行的任务

    private final int notificationID = 10000001;// 通知的id
    private final int updateProgress = 1;// 更新状态栏的下载进度
    private final int downloadSuccess = 2;// 下载成功
    private final int downloadError = 3;// 下载失败
    private DownLoadUtil downLoadUtil;
    public static boolean isDownload=false;

    public DownloadApkThread(String downloadUrl,  File fileLocation, String fileName,Context context) {
    	
        this.downloadUrl = downloadUrl;
        this.saveFile = fileLocation;
        this.context = context;
        this.fileName = fileName;
        DownloadApkThread.isDownload=true;
    }

    @Override
    public void run() {
        super.run();
        try {
            initNofication();
            handlerTask();
            downLoadUtil = new DownLoadUtil();
            int downSize = downLoadUtil.downloadUpdateFile(downloadUrl, saveFile, fileName, callback);
            if (downSize == downLoadUtil.getRealSize() && downSize != 0) {
                handler.sendEmptyMessage(downloadSuccess);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @author wangqian@iliveasia.com
     * @Description: 初始化通知栏
     */
    private void initNofication() {
    	
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context);  

        mBuilder.setContentTitle("应用正在下载，请稍后...")//设置通知栏标题  
        	.setContentText("")
        	.setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) //设置通知栏点击意图  
        	.setTicker("应用正在下载，请稍后...") //通知首次出现在通知栏，带上升动画效果的         
        	.setSmallIcon(android.R.drawable.stat_sys_download);
        
        notificationManager.notify(notificationID,  mBuilder.build());
    }
    
    public PendingIntent getDefalutIntent(int flags){  
        PendingIntent pendingIntent= PendingIntent.getActivity(context, 0, new Intent(), flags);  
        return pendingIntent;  
    } 

    /**
     * 
     * @author wangqian@iliveasia.com
     * @Description: 定时通知handler去显示下载百分比
     */
    private void handlerTask() {
        timer = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                handler.sendEmptyMessage(updateProgress);
            }
        };
        timer.schedule(task, 500, 500);
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
        	
            if (msg.what == updateProgress) {// 更新下载进度
            	
                int fileSize = downLoadUtil.getRealSize();
                int totalReadSize = downLoadUtil.getTotalSize();
                
                if (totalReadSize > 0) {
                    float size = (float) totalReadSize * 100 / (float) fileSize;
                    DecimalFormat format = new DecimalFormat("0.00");
                    String progress = format.format(size);
                   
                    mBuilder.setContentTitle("应用正在下载")//设置通知栏标题  
                		.setContentText("已下载" + progress + "%")
                		.setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)); //设置通知栏点击意图  
                	notificationManager.notify(notificationID,  mBuilder.build());
                }
            } else if (msg.what == downloadSuccess) {// 下载完成
            	DownloadApkThread.isDownload=false;
            	// 点击安装PendingIntent  
                Intent intent = new Intent(Intent.ACTION_VIEW);  
                intent.setDataAndType(Uri.fromFile(new File(saveFile,fileName)),"application/vnd.android.package-archive");   
                PendingIntent installIntent = PendingIntent.getActivity(context, 0, intent, Notification.FLAG_AUTO_CANCEL);  
            	mBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;
                
            	mBuilder.setContentTitle("应用下载完毕")//设置通知栏标题  
        			.setContentText("")
        			.setContentIntent(installIntent) //设置通知栏点击意图         
        			.setSmallIcon(android.R.drawable.stat_sys_download_done);
            	
                notificationManager.notify(notificationID, mBuilder.build());
                
                
                if (timer != null && task != null) {
                    timer.cancel();
                    task.cancel();
                    timer = null;
                    task = null;
                }
                // 安装apk
                AppHelp.installAPK(context,saveFile,fileName);
               

            } else if (msg.what == downloadError) {// 下载失败
                if (timer != null && task != null) {
                    timer.cancel();
                    task.cancel();
                    timer = null;
                    task = null;
                }
                notificationManager.cancel(notificationID);
                DownloadApkThread.isDownload=false;
            }
            
        }

    };
    /**
     * 下载回调
     */
    DownloadFileCallback callback = new DownloadFileCallback() {

        @Override
        public void downloadError(String msg) {
            handler.sendEmptyMessage(downloadError);
        }
    };

}
