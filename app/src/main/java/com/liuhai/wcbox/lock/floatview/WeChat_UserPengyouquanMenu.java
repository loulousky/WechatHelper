package com.liuhai.wcbox.lock.floatview;

import android.accessibilityservice.AccessibilityService;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import com.liuhai.wcbox.R;
import com.liuhai.wcbox.lock.base.BaseFloatViewAction;

/**
 * Created by chenliangj2ee on 2017/5/21.
 */

public class WeChat_UserPengyouquanMenu extends BaseFloatViewAction {
    @Override
    public int initLayoutId() {
        return R.layout.float_pengyouquan_menu;
    }

    @Override
    public void event(AccessibilityService service, AccessibilityEvent event) {
        super.event(service, event);

        if (event.getEventType() != AccessibilityEvent.TYPE_VIEW_CLICKED && event.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED)
            return;

        remove();
        if (event.getPackageName() == null)
            return;

        if (!"com.tencent.mm".equals(event.getPackageName().toString()))
            return;

        log("微信分享");
        if("com.tencent.mm.plugin.sns.ui.SnsUserUI".equals(event.getClassName().toString())){
            initLocation(service);
            show();

            mFloatView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            log("show....");
        }else{
            log("remove....");
        }

    }

    public void initLocation(AccessibilityService service) {
        if(myHeight>0)
            return ;//已经初始化完毕
        setLocation(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,0,0);
        setgravity(Gravity.RIGHT|Gravity.TOP);

    }
}
