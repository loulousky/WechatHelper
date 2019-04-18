package com.liuhai.wcbox.lock.act;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.liuhai.wcbox.R;


public class InfoActivity extends Activity {


    protected TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_info);
        initView();

    }

    private void initView() {
        info = (TextView) findViewById(R.id.info);

        String text = "当前插件版本为：" + getVersion(this, getPackageName()) + ",匹配微信版本为：" + getVersion(this, getPackageName())
                + ",与你当前安装的微信版本[" + getVersion(this, "com.tencent.mm") + "]不匹配，请升级微信到最新版本，或者升级微信插件到最新版本（注：微信插件将会在微信官方升级后7天内发布最新版本的插件，请耐心等待）,如有其它问题，请联系开发者QQ：912196320";

        info.setText(text);
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
}
