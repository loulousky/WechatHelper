package com.liuhai.wcbox.lock.floatview;


import android.accessibilityservice.AccessibilityService;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liuhai.wcbox.R;
import com.liuhai.wcbox.lock.utils.MySettings;
import com.lzx.lock.bean.LockStage;
import com.lzx.lock.db.CommLockInfoManager;
import com.lzx.lock.utils.LockPatternUtils;
import com.lzx.lock.widget.LockPatternView;
import com.lzx.lock.widget.LockPatternViewPattern;

import java.util.List;

/**
 * Created by chenliangj2ee on 2017/5/14.
 * 快捷入口启动时展示的开屏
 */

public class LockView implements View.OnClickListener {

    public int screenHeight;
    protected View rootView;
    protected ImageView lockIcon;
    protected TextView lockTip;
    protected LockPatternView unlockLockView;
    protected LinearLayout unlockLayout;
    protected CheckBox checkbox;
    protected RelativeLayout topLayout;

    //图案锁相关
    private LockStage mUiStage = LockStage.Introduction;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private LayoutInflater mLayoutInflater;
    private LinearLayout mFloatView;
    private AccessibilityService con;


    public LockView(AccessibilityService con) {
        this.con = con;
        this.mWindowManager = (WindowManager) con.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        this.screenHeight = mWindowManager.getDefaultDisplay().getHeight();

        init();

    }

    public void event(AccessibilityService service, AccessibilityEvent event) {
        if (event.getEventType() != AccessibilityEvent.TYPE_VIEW_CLICKED && event.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED)
            return;


        if (event.getPackageName() == null)
            return;

        if(event.getPackageName().toString().contains("android.widget.Toast"))

            return ;

        if (getLauncherPackageName(con).equals(event.getPackageName().toString())) {
            MySettings.setWeChatExit(con, true);
            remove();
        }

//        if(MySettings.isAutoHF(con))
//            return;

        if ("com.tencent.mm".equals(event.getPackageName().toString()) && MySettings.isWeChatExit(con)) {
            show();
            MySettings.setWeChatExit(con, false);
        } else {

        }


    }

    public String getLauncherPackageName(Context context) {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            return "";
        }
        //如果是不同桌面主题，可能会出现某些问题，这部分暂未处理
        if (res.activityInfo.packageName.equals("android")) {
            return "";
        } else {
            return res.activityInfo.packageName;
        }


    }

    public void init() {

        mWindowManager = (WindowManager) con.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mLayoutInflater = LayoutInflater.from(con);
        mFloatView = (LinearLayout) mLayoutInflater.inflate(R.layout.layout_self_unlock, null);
        initView(mFloatView);
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mLayoutParams.format = PixelFormat.RGBA_8888;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        mLayoutParams.x = 0;
        mLayoutParams.y = 0;
        mFloatView.setSystemUiVisibility(View.INVISIBLE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        initData();
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == com.lzx.lock.R.id.btn_reset) {

        }
    }


    boolean add = false;

    private void show() {
        if (add)
            return;
        if (mFloatView == null)
            init();
        unlockLockView.clearPattern();
        mWindowManager.addView(mFloatView, mLayoutParams);
        add = true;
        if( MySettings.isWeChatLock(con)){
            checkbox.setChecked(true);
        }
        ObjectAnimator an = ObjectAnimator.ofFloat(mFloatView, "alpha", 0f, 0.95f);
        an.setDuration(500);
        an.start();
    }

    public void remove() {
        try {
            if (mWindowManager != null && mFloatView != null)
                mWindowManager.removeView(mFloatView);
            add = false;
            log(" remove WeChat_FaXian.........");
        } catch (Exception e) {

        }

    }

    private void log(String s) {
        Log.i(getClass().getSimpleName(), s);
    }


    private LockPatternUtils mLockPatternUtils;
    private LockPatternViewPattern mPatternViewPattern;
    private int mFailedPatternAttemptsSinceLastTimeout = 0;
    private CommLockInfoManager mManager;
    private RelativeLayout mTopLayout;

    private TextureView mTextureView;


    protected void initData() {
        mManager = new CommLockInfoManager(con);
        initLockPatternView();

    }

    /**
     * 初始化解锁控件
     */
    private void initLockPatternView() {
        mLockPatternUtils = new LockPatternUtils(con);
        mPatternViewPattern = new LockPatternViewPattern(unlockLockView);
        mPatternViewPattern.setPatternListener(new LockPatternViewPattern.onPatternListener() {
            @Override
            public void onPatternDetected(List<LockPatternView.Cell> pattern) {
                if (mLockPatternUtils.checkPattern(pattern)) { //解锁成功,更改数据库状态
                    unlockLockView.setDisplayMode(LockPatternView.DisplayMode.Correct);
                    MySettings.setWeChatExit(con, false);
                    remove();
                    if(checkbox.isChecked()==false){
                        MySettings.setWeChatLock(con,false);
                    }
                } else {
                    unlockLockView.postDelayed(mClearPatternRunnable, 500);
                    Toast.makeText(con, "密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        unlockLockView.setOnPatternListener(mPatternViewPattern);
        unlockLockView.setTactileFeedbackEnabled(true);
    }

    private Runnable mClearPatternRunnable = new Runnable() {
        public void run() {
            unlockLockView.clearPattern();
        }
    };


    private void initView(View rootView) {
        lockIcon = (ImageView) rootView.findViewById(R.id.lock_icon);
        lockTip = (TextView) rootView.findViewById(R.id.lock_tip);
        unlockLockView = (LockPatternView) rootView.findViewById(R.id.unlock_lock_view);
        unlockLayout = (LinearLayout) rootView.findViewById(R.id.unlock_layout);
        checkbox = (CheckBox) rootView.findViewById(R.id.checkbox);
        topLayout = (RelativeLayout) rootView.findViewById(R.id.top_layout);
    }
}
