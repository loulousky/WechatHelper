package com.liuhai.wcbox.lock;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.liuhai.wcbox.R;
import com.liuhai.wcbox.lock.ads.AdsSplash_Gdt;
import com.liuhai.wcbox.lock.utils.MySettings;


public class FloatSplashActivity extends Activity {

    protected FrameLayout rootview;
    protected FrameLayout splashContainer;
    private TextView skipView;
    private static final String SKIP_TEXT = "点击跳过 %d";
    AdsSplash_Gdt adt;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            finish();
            Log.i("SplashActivity", "广告隐藏1....");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MySettings.isAdsShowTimeOut(this)) {
            rootview = (FrameLayout) View.inflate(this, R.layout.activity_splash_tengxun, null);
            adt = new AdsSplash_Gdt(this);
            adt.showAds(rootview);
            initView(rootview);
            handler.sendEmptyMessageDelayed(0, 6000);
        } else {
            finish();
        }
    }

    private void initView(View view) {
        splashContainer = (FrameLayout) view.findViewById(R.id.splash_container);
        skipView = (TextView) view.findViewById(R.id.skip_view);
        rootview.setAlpha(0);
    }





    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
        if (adt != null) {
            adt.remove();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
