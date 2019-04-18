package com.liuhai.wcbox.lock.act;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.liuhai.wcbox.R;
import com.liuhai.wcbox.lock.action.WeChat_QunFa2;
import com.liuhai.wcbox.lock.utils.MySettings;

/**
 *
 */
public class EditMessageActivity extends FragmentActivity implements View.OnClickListener {


    protected EditText message;
    protected Button close;
    protected Button share;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_edit_message);
        initView();


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.close) {
            finish();
        } else if (view.getId() == R.id.share) {
            String msg = message.getText().toString().trim();
            if("".equals(msg)){
                Toast.makeText(this, "请输入群发内容", Toast.LENGTH_SHORT).show();
                return;
            }
            WeChat_QunFa2.message=msg;
            MySettings.setQunfa(this,true);
            finish();

        }
    }

    private void initView() {
        message = (EditText) findViewById(R.id.message);
        close = (Button) findViewById(R.id.close);
        close.setOnClickListener(EditMessageActivity.this);
        share = (Button) findViewById(R.id.share);
        share.setOnClickListener(EditMessageActivity.this);

    }
}
