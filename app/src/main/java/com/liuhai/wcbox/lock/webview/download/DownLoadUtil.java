package com.liuhai.wcbox.lock.webview.download;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownLoadUtil {
    private int totalSize = 0;
    private int realSize = 0;
    private final static String TAG = "DownLoadUtil";

    /**
     * 
     * @author wangqian@iliveasia.com
     * @Description: 获取当前下载文件大小
     * @return
     */
    public int getTotalSize() {
        return totalSize;
    }

    /**
     * 
     * @author wangqian@iliveasia.com
     * @Description: 获取应该下载的文件大小
     * @return
     */
    public int getRealSize() {
        return realSize;
    }

    /**
     * 
     * @author wangqian@iliveasia.com
     * @Description: 下载文件
     * @param downloadUrl
     * @param saveFile
     * @param fileName
     * @param callback
     * @return
     * @throws Exception
     */
    public int downloadUpdateFile(String downloadUrl, File saveFile,String fileName, DownloadFileCallback callback)
            throws Exception {

        HttpURLConnection httpConnection = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            URL url = new URL(downloadUrl);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setConnectTimeout(10000);
            httpConnection.setReadTimeout(20000);
            if (httpConnection.getResponseCode() == 404) {
                httpConnection.disconnect();
                callback.downloadError("下载地址不存在或暂时无法访问");
                return 0;
            }
            realSize = httpConnection.getContentLength();
            is = httpConnection.getInputStream();
            
            
            File file = new File(saveFile,fileName);
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs(); 
            }
            
            Log.i("------eee--------", file.getAbsolutePath());
            
            fos = new FileOutputStream(file, false);
            byte buffer[] = new byte[4096];
            int readSize = 0;

            while ((readSize = is.read(buffer)) > 0) {
                fos.write(buffer, 0, readSize);
                totalSize += readSize;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "下载失败"+e.getMessage());
            callback.downloadError("下载失败");
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
            if (is != null) {
                is.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
        return totalSize;
    }

}
