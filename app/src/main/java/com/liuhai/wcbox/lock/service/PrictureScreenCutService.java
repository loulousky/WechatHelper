package com.liuhai.wcbox.lock.service;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.os.AsyncTaskCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.liuhai.wcbox.lock.act.PreviewScreenCutActivity;
import com.liuhai.wcbox.lock.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by branch on 2016-5-25.
 * <p>
 * 启动悬浮窗界面
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class PrictureScreenCutService extends Service {
    public static int top = 0, bottom = 0, left = 0, right = 0;

    public static Intent newIntent(Context context, Intent mResultData) {

        Intent intent = new Intent(context, PrictureScreenCutService.class);

        if (mResultData != null) {
            intent.putExtras(mResultData);
        }
        return intent;
    }

    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;

    private static Intent mResultData = null;


    private ImageReader mImageReader;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mScreenDensity = 1;
    private int actionBar;


    @Override
    public void onCreate() {
        super.onCreate();
        WindowManager mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mScreenWidth = mWindowManager.getDefaultDisplay().getWidth();
        mScreenHeight = mWindowManager.getDefaultDisplay().getHeight();
        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;
        Log.i("FloatWindowsService", "mScreenHeight:" + mScreenHeight);
        actionBar = getSharedPreferences("LockScreed", 0).getInt("actionBarHeight", 65);

    }


    public static Intent getResultData() {
        return mResultData;
    }

    public static void setResultData(Intent mResultData) {
        PrictureScreenCutService.mResultData = mResultData;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        createImageReader();
        startScreenShot();
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


    private void startScreenShot() {

//
//        Handler handler1 = new Handler();
//        handler1.postDelayed(new Runnable() {
//            public void run() {
//                //start virtual
//
//            }
//        }, 5);
//
//        handler1.postDelayed(new Runnable() {
//            public void run() {
//                //capture the screen
//
//
//            }
//        }, 30);

        virtualDisplay();
        startCapture();
    }


    private void createImageReader() {

        mImageReader = ImageReader.newInstance(mScreenWidth, mScreenHeight, PixelFormat.RGBA_8888, 1);

    }

//    public void startVirtual() {
//        if (mMediaProjection != null) {
//
//        } else {
//            setUpMediaProjection();
//        }
//    }

//    public void setUpMediaProjection() {
//        if (mResultData == null) {
////            Intent intent = new Intent(Intent.ACTION_MAIN);
////            intent.addCategory(Intent.CATEGORY_LAUNCHER);
////            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////            startActivity(intent);
//        } else {
//            mMediaProjection = getMediaProjectionManager().getMediaProjection(Activity.RESULT_OK, mResultData);
//        }
//    }

    private MediaProjectionManager getMediaProjectionManager() {

        return (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    }

    private void virtualDisplay() {
        try {
            if (mMediaProjection == null)
                mMediaProjection = getMediaProjectionManager().getMediaProjection(Activity.RESULT_OK, mResultData);
            mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                    mScreenWidth, mScreenHeight, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    mImageReader.getSurface(), null, null);
        } catch (Exception e) {
            stopSelf();

        }

    }

    private void startCapture() {

        Image image = mImageReader.acquireLatestImage();

        if (image == null) {
            startScreenShot();
        } else {
            SaveTask mSaveTask = new SaveTask();
            AsyncTaskCompat.executeParallel(mSaveTask, image);
        }
    }


    public class SaveTask extends AsyncTask<Image, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Image... params) {

            if (params == null || params.length < 1 || params[0] == null) {

                return null;
            }

            Image image = params[0];

            int width = image.getWidth();
            int height = image.getHeight();
            final Image.Plane[] planes = image.getPlanes();
            final ByteBuffer buffer = planes[0].getBuffer();
            //每个像素的间距
            int pixelStride = planes[0].getPixelStride();
            //总的间距
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * width;
            Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(buffer);


            int x,y,w,h;

            if(PrictureScreenCutService.right > PrictureScreenCutService.left){
                w = PrictureScreenCutService.right - PrictureScreenCutService.left;
                x = PrictureScreenCutService.left;
            }else{
                w = PrictureScreenCutService.left - PrictureScreenCutService.right;
                x = PrictureScreenCutService.right;
            }

            if(PrictureScreenCutService.bottom > PrictureScreenCutService.top){
                h = PrictureScreenCutService.bottom - PrictureScreenCutService.top;
                y = PrictureScreenCutService.top;
            }else{
                h = PrictureScreenCutService.top - PrictureScreenCutService.bottom;
                y = PrictureScreenCutService.bottom;
            }


            if (PrictureScreenCutService.top + h > bitmap.getHeight()) {
                h = bitmap.getHeight() - PrictureScreenCutService.top;
            }
            y = y > 0 ? y : 0;
            w=Math.abs(w);
            h=Math.abs(h);
            w=w==0?1:w;
            h=h==0?1:h;
            bitmap = Bitmap.createBitmap(bitmap, x, y, w, h);

            image.close();
            File fileImage = null;
            if (bitmap != null) {
                try {
                    fileImage = new File(FileUtil.getScreenShotsName(getApplicationContext()));
                    if (!fileImage.exists()) {
                        fileImage.createNewFile();
                    }
                    FileOutputStream out = new FileOutputStream(fileImage);
                    if (out != null) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri contentUri = Uri.fromFile(fileImage);
                        media.setData(contentUri);
                        sendBroadcast(media);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    fileImage = null;
                } catch (IOException e) {
                    e.printStackTrace();
                    fileImage = null;
                } finally {
                    stopSelf();
                }
            }

            if (fileImage != null) {
                PreviewScreenCutActivity.url = fileImage.getAbsolutePath();
                return bitmap;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            //预览图片
            if (bitmap != null) {

                Intent intent = new Intent(getApplicationContext(), PreviewScreenCutActivity.class);
                PreviewScreenCutActivity.bitmap = bitmap;
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
            stopSelf();

        }
    }


    private void tearDownMediaProjection() {
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
    }

    private void stopVirtual() {
        if (mVirtualDisplay == null) {
            return;
        }
        mVirtualDisplay.release();
        mVirtualDisplay = null;
    }

    @Override
    public void onDestroy() {
        // to remove mFloatLayout from windowManager
        super.onDestroy();
        stopVirtual();

        tearDownMediaProjection();
    }


}
