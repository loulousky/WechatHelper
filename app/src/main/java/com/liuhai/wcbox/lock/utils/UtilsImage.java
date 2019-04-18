package com.liuhai.wcbox.lock.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by chenliangj2ee on 2017/3/22.
 */

public class UtilsImage {


    public static void load(Context context, String url, ImageView image){
        Glide.with(context)
                .load(url)
                .into(image);
    }
}
