package com.liuhai.wcbox.lock.action;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

import com.liuhai.wcbox.lock.base.BaseAction;

/**
 * Created by chenliangj2ee on 2017/5/17.
 * 微信朋友圈屏蔽检测，找出谁屏蔽了我，并且将其也屏蔽
 */

public class WeChat_PengYouQuanShieldCheck extends BaseAction {

    public WeChat_PengYouQuanShieldCheck(AccessibilityService service) {
        super(service);
    }


    @Override
    public void event(AccessibilityEvent event) {
        if (event == null)
            return;
        log(event.toString());

        String packageName = event.getPackageName() == null ? "" : event.getPackageName().toString();

        if (!"com.tencent.mm".equals(packageName))
            return;

    }


}
