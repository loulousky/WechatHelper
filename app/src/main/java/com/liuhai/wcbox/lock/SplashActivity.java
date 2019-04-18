package com.liuhai.wcbox.lock;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.liuhai.wcbox.R;
import com.liuhai.wcbox.lock.utils.AppHelp;


public class SplashActivity extends Activity  {

    protected FrameLayout splashContainer;
    private TextView skipView;
    private static final String SKIP_TEXT = "点击跳过 %d";

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
        Log.i("SplashActivity", "SplashActivity  onCreate...");
        if(getIntent().getBooleanExtra("finish",false)){
            clear();
        }else{
            setContentView(R.layout.activity_splash_tengxun);
            handler.sendEmptyMessageDelayed(0, 6000);
            initView();
        }

    }

    private void initView() {
        splashContainer = (FrameLayout)findViewById(R.id.splash_container);
        skipView = (TextView)findViewById(R.id.skip_view);
    }




    private void clear() {

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.cancelAll();


        Intent mHomeIntent=new Intent(Intent.ACTION_MAIN);
        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mHomeIntent);

        finish();
//        Intent intent = getBaseContext().getPackageManager()
//                .getLaunchIntentForPackage(getBaseContext().getPackageName());
//        PendingIntent restartIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
//        AlarmManager mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 3000, restartIntent); // 1秒钟后重启应用
//        System.exit(0);

    }





    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    boolean boo;

    private void click(View view){
        if(AppHelp.isWifi(this)==false)
            return ;

        Log.i("SplashActivity", view.getClass().getSimpleName());
        if(view instanceof ViewGroup){
             for(int i=0;i< ((ViewGroup) view).getChildCount();i++){
                 click(((ViewGroup) view).getChildAt(i));
             }
        }else{
            if(view instanceof ImageView) {
               if(view.isClickable()&&boo==false) {
                   view.performClick();
                   boo=true;
                   Log.i("SplashActivity", "click，，，");
               }
            }
        }
    }


}
