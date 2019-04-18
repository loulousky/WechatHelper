package com.liuhai.wcbox.lock.act;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.liuhai.wcbox.R;


/**
 * chenliangj2ee@163.com
 */
public class YanshiActivity extends FragmentActivity implements SurfaceHolder.Callback {
    MediaPlayer player;
    SurfaceHolder surfaceHolder;

    SurfaceView surface;
    int[] shipingId = {R.raw.yanshi01,R.raw.yanshi02,R.raw.yanshi03};
    int index;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yanshi);
        index = getIntent().getIntExtra("index", 0);
        surface = (SurfaceView) findViewById(R.id.surface);

        surfaceHolder = surface.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }


    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setDisplay(surfaceHolder);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                finish();
                Toast.makeText(YanshiActivity.this, "演示结束", Toast.LENGTH_SHORT).show();
                Toast.makeText(YanshiActivity.this, "演示结束", Toast.LENGTH_SHORT).show();

            }
        });
        try {
            AssetFileDescriptor file = getResources().openRawResourceFd(shipingId[index]);
            player.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            player.prepare();
            player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (player.isPlaying()) {
            player.stop();
        }
        player.release();
    }
}
