package com.lzx.lock.mvp.p;

import android.content.Context;

import com.lzx.lock.R;
import com.lzx.lock.bean.LockStage;
import com.lzx.lock.mvp.contract.GestureCreateContract;
import com.lzx.lock.utils.LockPatternUtils;
import com.lzx.lock.widget.LockPatternView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xian on 2017/2/17.
 */

public class GestureCreatePresenter implements GestureCreateContract.Presenter {
    private GestureCreateContract.View mView;
    private Context mContext;

    public GestureCreatePresenter(GestureCreateContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    @Override
    public void updateStage(LockStage stage) {
        mView.updateUiStage(stage); //更新UiStage
        if (stage == LockStage.ChoiceTooShort) { //如果少于4个点
            mView.updateLockTip(mContext.getResources().getString(stage.headerMessage, LockPatternUtils.MIN_LOCK_PATTERN_SIZE), true);
        } else {
            if (stage.headerMessage == R.string.lock_need_to_unlock_wrong) {
                mView.updateLockTip(mContext.getResources().getString(R.string.lock_need_to_unlock_wrong), true);
                mView.setHeaderMessage(R.string.lock_recording_intro_header);
            } else {
                mView.setHeaderMessage(stage.headerMessage); //
            }
        }
        // same for whether the patten is enabled
        mView.lockPatternViewConfiguration(stage.patternEnabled, LockPatternView.DisplayMode.Correct);

        if (stage == LockStage.Introduction) {
            mView.Introduction(); //第一步

        } else if (stage == LockStage.HelpScreen) {
            mView.HelpScreen();

        } else if (stage == LockStage.ChoiceTooShort) {
            mView.ChoiceTooShort();

        } else if (stage == LockStage.FirstChoiceValid) {
            updateStage(LockStage.NeedToConfirm); //转跳到第二步
            mView.moveToStatusTwo();

        } else if (stage == LockStage.NeedToConfirm) {
            mView.clearPattern();  //第二步

        } else if (stage == LockStage.ConfirmWrong) {//第二步跟第一步不一样
            mView.ConfirmWrong();

        } else if (stage == LockStage.ChoiceConfirmed) {//第三步
            mView.ChoiceConfirmed();

        }
    }

    @Override
    public void onPatternDetected(List<LockPatternView.Cell> pattern, List<LockPatternView.Cell> mChosenPattern, LockStage mUiStage) {
        if (mUiStage == LockStage.NeedToConfirm) { //如果下一步
            if (mChosenPattern == null)
                throw new IllegalStateException("null chosen pattern in stage 'need to confirm");
            if (mChosenPattern.equals(pattern)) {
                updateStage(LockStage.ChoiceConfirmed);
            } else {
                updateStage(LockStage.ConfirmWrong);
            }
        } else if (mUiStage == LockStage.ConfirmWrong) {
            if (pattern.size() < LockPatternUtils.MIN_LOCK_PATTERN_SIZE) {
                updateStage(LockStage.ChoiceTooShort);
            } else {
                if (mChosenPattern.equals(pattern)) {
                    updateStage(LockStage.ChoiceConfirmed);
                } else {
                    updateStage(LockStage.ConfirmWrong);
                }
            }
        } else if (mUiStage == LockStage.Introduction || mUiStage == LockStage.ChoiceTooShort) {
            if (pattern.size() < LockPatternUtils.MIN_LOCK_PATTERN_SIZE) {
                updateStage(LockStage.ChoiceTooShort);
            } else {
                mChosenPattern = new ArrayList<>(pattern);
                mView.updateChosenPattern(mChosenPattern);
                updateStage(LockStage.FirstChoiceValid);
            }
        } else {
            throw new IllegalStateException("Unexpected stage " + mUiStage + " when " + "entering the pattern.");
        }
    }

    @Override
    public void onDestroy() {

    }
}
