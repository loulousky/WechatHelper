package com.liuhai.wcbox.lock.act;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liuhai.wcbox.R;
import com.liuhai.wcbox.lock.utils.MySettings;
import com.lzx.lock.base.AppConstants;
import com.lzx.lock.bean.LockStage;
import com.lzx.lock.mvp.contract.GestureCreateContract;
import com.lzx.lock.mvp.p.GestureCreatePresenter;
import com.lzx.lock.utils.LockPatternUtils;
import com.lzx.lock.utils.SpUtil;
import com.lzx.lock.utils.SystemBarHelper;
import com.lzx.lock.widget.LockPatternView;
import com.lzx.lock.widget.LockPatternViewPattern;

import java.util.List;


/**
 * 功能：启动页
 * 模板修改说明：
 * 1、将nextActivityClass变量赋值为启动页之后进入的下一个Activity，
 * 如：nextActivityClass=MainActivity.class;
 */
public class WeChatLockActivity extends Activity implements View.OnClickListener,
        GestureCreateContract.View {

    protected List<LockPatternView.Cell> mChosenPattern = null; //密码
    private TextView mLockTip;
    private LockPatternView mLockPatternView;
    private TextView mBtnReset;
    //图案锁相关
    private LockStage mUiStage = LockStage.Introduction;
    private LockPatternUtils mLockPatternUtils;
    private LockPatternViewPattern mPatternViewPattern;
    private GestureCreatePresenter mGestureCreatePresenter;
    private RelativeLayout mTopLayout;
    private Runnable mClearPatternRunnable = new Runnable() {
        public void run() {
            mLockPatternView.clearPattern();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_lock);
        mLockPatternView = (LockPatternView) findViewById(com.lzx.lock.R.id.lock_pattern_view);
        mLockTip = (TextView) findViewById(com.lzx.lock.R.id.lock_tip);
        mBtnReset = (TextView) findViewById(com.lzx.lock.R.id.btn_reset);
        mTopLayout = (RelativeLayout) findViewById(com.lzx.lock.R.id.top_layout);
        mTopLayout.setPadding(0, SystemBarHelper.getStatusBarHeight(this),0,0);
        mGestureCreatePresenter = new GestureCreatePresenter(this, this);
        mBtnReset.setOnClickListener(this);
        initLockPatternView();
    }

    /**
     * 初始化锁屏控件
     */
    private void initLockPatternView() {
        mLockPatternUtils = new LockPatternUtils(this);
        mPatternViewPattern = new LockPatternViewPattern(mLockPatternView);
        mPatternViewPattern.setPatternListener(new LockPatternViewPattern.onPatternListener() {
            @Override
            public void onPatternDetected(List<LockPatternView.Cell> pattern) {
                mGestureCreatePresenter.onPatternDetected(pattern, mChosenPattern, mUiStage);
            }
        });
        mLockPatternView.setOnPatternListener(mPatternViewPattern);
        mLockPatternView.setTactileFeedbackEnabled(true);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == com.lzx.lock.R.id.btn_reset) {
            setStepOne();

        }
    }

    /**
     * 恢复到第一步
     */
    private void setStepOne() {
        mGestureCreatePresenter.updateStage(LockStage.Introduction);
        mLockTip.setText(getString(com.lzx.lock.R.string.lock_recording_intro_header));
    }

    private void gotoLockMainActivity() {
        SpUtil.getInstance().putBoolean(AppConstants.LOCK_STATE, true); //开启应用锁开关

    }

    /**
     * 更新当前锁的状态
     */
    @Override
    public void updateUiStage(LockStage stage) {
        mUiStage = stage;
    }

    /**
     * 更新当前密码
     */
    @Override
    public void updateChosenPattern(List<LockPatternView.Cell> mChosenPattern) {
        this.mChosenPattern = mChosenPattern;

    }

    /**
     * 更新提示信息
     */
    @Override
    public void updateLockTip(String text, boolean isToast) {
        mLockTip.setText(text);
    }

    /**
     * 更新提示信息
     */
    @Override
    public void setHeaderMessage(int headerMessage) {
        mLockTip.setText(headerMessage);
    }

    /**
     * LockPatternView的一些配置
     */
    @Override
    public void lockPatternViewConfiguration(boolean patternEnabled, LockPatternView.DisplayMode displayMode) {
        if (patternEnabled) {
            mLockPatternView.enableInput();
        } else {
            mLockPatternView.disableInput();
        }
        mLockPatternView.setDisplayMode(displayMode);
    }

    /**
     * 初始化
     */
    @Override
    public void Introduction() {
        clearPattern();
    }

    @Override
    public void HelpScreen() {
    }

    /**
     * 路径太短
     */
    @Override
    public void ChoiceTooShort() {
        mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);  //路径太短
        mLockPatternView.removeCallbacks(mClearPatternRunnable);
        mLockPatternView.postDelayed(mClearPatternRunnable, 500);
    }

    /**
     * 画完第一步转到第二步
     */
    @Override
    public void moveToStatusTwo() {

    }

    /**
     * 清空控件路径
     */
    @Override
    public void clearPattern() {
        mLockPatternView.clearPattern();
    }

    /**
     * 第一次和第二次画得不一样
     */
    @Override
    public void ConfirmWrong() {
        mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);  //路径太短
        mLockPatternView.removeCallbacks(mClearPatternRunnable);
        mLockPatternView.postDelayed(mClearPatternRunnable, 500);
    }

    /**
     * 画成功了
     */
    @Override
    public void ChoiceConfirmed() {
        mLockPatternUtils.saveLockPattern(mChosenPattern); //保存密码
        clearPattern();
        MySettings.setWeChatLock(this,true);
        Toast.makeText(this, "微信锁设置成功", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGestureCreatePresenter.onDestroy();
    }
}
