package com.liuhai.wcbox.lock;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.liuhai.wcbox.lock.service.PushUmengMessageService;
import com.lzx.lock.LockApplication;
import com.lzy.okgo.OkGo;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.tag.TagManager;

/**
 * Created by chenliangj2ee on 2017/8/21.
 */

public class MyApp extends LockApplication {
    PushAgent mPushAgent;

    @Override
    public void onCreate() {
        super.onCreate();
        OkGo.getInstance().init(this);


        youmeng();
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setPushIntentServiceClass(PushUmengMessageService.class);
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.i("MyAppdddd", "onFailure" + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.i("MyAppdddd", "onFailure" + s);
                Log.i("MyAppdddd", "onFailure" + s1);
            }
        });


        mPushAgent.getTagManager().add(new TagManager.TCallBack() {
            @Override
            public void onMessage(final boolean isSuccess, final ITagManager.Result result) {
                //isSuccess表示操作是否成功
            }
        }, getVersion(this, getPackageName()));
    }

    public String getVersion(Context context, String packageName) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(packageName, 0);
            return info.versionName;
        } catch (Exception e) {

        }
        return "";
    }

    private void youmeng() {
        MobclickAgent.setDebugMode(true);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        PushAgent.getInstance(this).onAppStart();
    }

}
