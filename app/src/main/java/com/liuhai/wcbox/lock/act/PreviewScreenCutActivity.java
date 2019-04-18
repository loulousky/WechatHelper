package com.liuhai.wcbox.lock.act;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import com.liuhai.wcbox.R;

import java.io.File;

/**
 *
 */
public class PreviewScreenCutActivity extends FragmentActivity implements View.OnClickListener {


    protected ImageView previewImage;
    protected Button close;
    protected Button share;
    public static Bitmap bitmap;
    public static String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_preview_layout);
        initView();


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.close) {
            finish();
        } else if (view.getId() == R.id.share) {

            Uri imageUri = Uri.fromFile(new File(PreviewScreenCutActivity.url));

            Intent imageIntent = new Intent(Intent.ACTION_SEND);
            imageIntent.setType("image/*");
            imageIntent.setPackage("com.tencent.mm");
            imageIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            startActivity(Intent.createChooser(imageIntent, "分享"));
            finish();


        }
    }

    private void initView() {
        previewImage = (ImageView) findViewById(R.id.preview_image);
        close = (Button) findViewById(R.id.close);
        close.setOnClickListener(PreviewScreenCutActivity.this);
        share = (Button) findViewById(R.id.share);
        share.setOnClickListener(PreviewScreenCutActivity.this);

        if (bitmap != null)
            previewImage.setImageBitmap(PreviewScreenCutActivity.bitmap);
    }
}
