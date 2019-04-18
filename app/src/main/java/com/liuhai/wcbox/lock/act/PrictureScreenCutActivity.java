package com.liuhai.wcbox.lock.act;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.liuhai.wcbox.lock.circle.view.PrictureScreenCutView;
import com.liuhai.wcbox.lock.service.PrictureScreenCutService;


/**
 * chenliangj2ee@163.com
 */
public class PrictureScreenCutActivity extends FragmentActivity {

    PrictureScreenCutView floatView;
    private int top = 0, bottom = 0, left = 0, right = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        floatView = new PrictureScreenCutView(this);

        floatView.setScreenCutListener(new PrictureScreenCutView.screenCutListener() {
            @Override
            public void start(int top, int bottom, int left, int right) {
                PrictureScreenCutService.top = top;
                PrictureScreenCutService.bottom = bottom;
                PrictureScreenCutService.left = left;
                PrictureScreenCutService.right = right;
                requestCapturePermission();


            }
        });

    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void requestCapturePermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager)
                getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        startActivityForResult(
                mediaProjectionManager.createScreenCaptureIntent(),
                0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK && data != null) {

                    PrictureScreenCutService.setResultData(data);
                    Intent intent=new Intent(getApplicationContext(), PrictureScreenCutService.class);
                    startService(intent);
                }
                break;
        }
      finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        floatView.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        floatView.remove();
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            Rect rectangle = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
            if (rectangle.top > 0){
                getSharedPreferences("LockScreed", 0).edit().putInt("actionBarHeight", rectangle.top).commit();

            }
         }
        super.onWindowFocusChanged(hasFocus);
    }


    public static int getTop(Context con) {
        int top = con.getSharedPreferences("LockScreed", 0).getInt("actionBarHeight", 65);
        if (top <= 0)
            top = 65;
        return top;
    }

}
