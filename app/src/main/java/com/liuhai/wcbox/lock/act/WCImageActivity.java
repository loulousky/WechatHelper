package com.liuhai.wcbox.lock.act;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.liuhai.wcbox.R;
import com.liuhai.wcbox.lock.act.adapter.ItemImageCacheAdapter;

import java.io.File;
import java.util.ArrayList;


public class WCImageActivity extends AppCompatActivity {

    protected GridView gridview;
    protected Button clear;
    protected TextView impty;

    private String path;

    private String name = "snst_snsu_";
    private String name2 = "sight";//视频
    private ArrayList<String> objects = new ArrayList<String>();
    ItemImageCacheAdapter adapter;
    private ProgressDialog dialog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    dialog.dismiss();
                    Toast.makeText(WCImageActivity.this, "数据完成", Toast.LENGTH_SHORT).show();
                    Toast.makeText(WCImageActivity.this, "缓存数据：" + objects.size() + "条", Toast.LENGTH_SHORT).show();
                   if(objects.size()>0){
                       gridview.setVisibility(View.VISIBLE);
                       clear.setVisibility(View.VISIBLE);
                       impty.setVisibility(View.GONE);
                   }else{
                       gridview.setVisibility(View.GONE);
                       clear.setVisibility(View.GONE);
                       impty.setVisibility(View.VISIBLE);
                   }

                    break;
                case 1:
                    dialog.setMessage("正在删除..." + fileName);
                    adapter.notifyDataSetChanged();
                    break;
                case 2:
                    adapter.notifyDataSetChanged();
                    break;
                case 3:
                    dialog.dismiss();
                    objects.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(WCImageActivity.this, "清空完成", Toast.LENGTH_SHORT).show();
                    Toast.makeText(WCImageActivity.this, "清空数据：" + fileCount + "条", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_wc_image);
        path = getSDPath() + "/tencent/MicroMsg";
        objects.clear();
        initView();
        dialog = new ProgressDialog(this);
        dialog.setMessage("正在加载数据...");
        dialog.show();


        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(path);
                getSDPath(new File(path));
                handler.sendEmptyMessageDelayed(0, 0);
            }
        }).start();
    }

    public void getSDPath(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    getSDPath(files[i]);
                } else {//文件
                    addImage(files[i].getPath());
                    handler.sendEmptyMessageDelayed(2, 0);
                }
            }
        } else {//文件

            addImage(file.getPath());
            handler.sendEmptyMessageDelayed(2, 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void addImage(String path) {
        if (path.contains(".nomedia") || path.contains("snstblur") || path.contains("MSG_") || path.contains("emoji") || path.contains("syncFile") || path.contains(".zip"))
            return;
        File file = new File(path.replace("snst_", "sight_"));
        if (file.exists())
            return;
        objects.add("file://" + path + "");
        Log.i("WCImageActivity2", "file://" + path + "");
    }

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }

    private void initView() {
        gridview = (GridView) findViewById(R.id.gridview);
        adapter = new ItemImageCacheAdapter(this, objects,false);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("onItemClick", objects.get(i));
                Intent in = new Intent(WCImageActivity.this, WCImageViewpagerActivity.class);
                WCImageViewpagerActivity.objects = objects;
                in.putExtra("index", i);
                startActivity(in);
            }
        });
        clear = (Button) findViewById(R.id.clear);
        impty = (TextView) findViewById(R.id.impty);
    }

    private String fileName;
    private int fileCount;

    public void clear(View view) {
        dialog = new ProgressDialog(this);
        dialog.setMessage("正在删除...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (String s : objects) {
                    File file = new File(s.replace("file://", ""));
                    fileName = file.getName();
                    file.delete();
                    fileCount++;
                    handler.sendEmptyMessageDelayed(1, 0);
//                    objects.remove(s);
                }
                handler.sendEmptyMessageDelayed(3, 0);
            }
        }).start();
    }
}
