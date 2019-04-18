/*
 * Copyright (c) 2014 Yrom Wang <http://www.yrom.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.yrom.screenrecorder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends Activity {
    private static final int REQUEST_CODE = 1;
    private MediaProjectionManager mMediaProjectionManager;
    private ScreenRecorder mRecorder;
    public static boolean boo;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(MainActivity.boo==false){
            finish();
            return;
        }

        mMediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        Log.i("screenrecorder", "onCreate.............");
        Intent captureIntent = mMediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(captureIntent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        MediaProjection mediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
        if (mediaProjection == null) {
            return;
        }
        final int width = 720;
        final int height = 1280;
        file = new File(Environment.getExternalStorageDirectory(),
                "DCIM/Video");
        file.mkdirs();
        file = new File(Environment.getExternalStorageDirectory(),
                "DCIM/Video/" + System.currentTimeMillis() + ".mp4");

        final int bitrate = 6000000;
        mRecorder = new ScreenRecorder(width, height, bitrate, 1, mediaProjection, file.getAbsolutePath());
        mRecorder.start();
        moveTaskToBack(true);
        Log.i("screenrecorder", "开始录屏.............");


    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("screenrecorder", "onNewIntent.............");
        if (mRecorder != null) {
            mRecorder.quit();
            mRecorder = null;
            new SingleMediaScanner(this, file);
            Uri uri = Uri.parse(file.getAbsolutePath());
            Intent intent2 = new Intent(Intent.ACTION_VIEW);
            intent2.setDataAndType(uri, "video/mp4");
            startActivity(intent2);
            Toast.makeText(this, "录制结束，正在打开...", Toast.LENGTH_SHORT).show();
        }

        Log.i("screenrecorder", "结束录屏.............");
    }



    public class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

        private MediaScannerConnection mMs;
        private File mFile;

        public SingleMediaScanner(Context context, File f) {
            mFile = f;
            mMs = new MediaScannerConnection(context, this);
            mMs.connect();
        }

        @Override
        public void onMediaScannerConnected() {
            mMs.scanFile(mFile.getAbsolutePath(), null);
        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
            mMs.disconnect();
            finish();
        }

    }
}
