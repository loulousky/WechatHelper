package com.liuhai.wcbox.lock.webview.download;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.text.DecimalFormat;

public class DownloadVideoThread extends Thread {
    private String downloadUrl;
    private File saveFile;
    private String fileName;
    private Context context;


    private final int updateProgress = 1;// 更新状态栏的下载进度
    private final int downloadSuccess = 2;// 下载成功
    private final int downloadError = 3;// 下载失败
    private DownLoadUtil downLoadUtil;
    public static boolean isDownload=false;

    public DownloadVideoThread(String downloadUrl, File fileLocation, String fileName, Context context) {
    	
        this.downloadUrl = downloadUrl;
        this.saveFile = fileLocation;
        this.context = context;
        this.fileName = fileName;
        DownloadVideoThread.isDownload=true;
    }

    @Override
    public void run() {
        super.run();
        try {
            downLoadUtil = new DownLoadUtil();
            Log.i("DownloadVideoThread","开始下载...."+downloadUrl);
            int downSize = downLoadUtil.downloadUpdateFile(downloadUrl, saveFile, fileName, callback);
            if (downSize == downLoadUtil.getRealSize() && downSize != 0) {
                handler.sendEmptyMessage(downloadSuccess);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    Log.i("DownloadVideoThread","下载中...."+progress);
                }
            } else if (msg.what == downloadSuccess) {// 下载完成
                Log.i("DownloadVideoThread","下载完成....");
            	DownloadVideoThread.isDownload=false;

            } else if (msg.what == downloadError) {// 下载失败
                saveFile.delete();
                DownloadVideoThread.isDownload=false;
                Log.i("DownloadVideoThread","下载失败....");
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
