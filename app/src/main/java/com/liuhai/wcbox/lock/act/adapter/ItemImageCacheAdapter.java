package com.liuhai.wcbox.lock.act.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.liuhai.wcbox.R;
import com.liuhai.wcbox.lock.utils.UtilsImage;

import java.util.ArrayList;

public class ItemImageCacheAdapter extends BaseAdapter {

    private ArrayList<String> objects = new ArrayList<String>();

    private Context context;
    private LayoutInflater layoutInflater;
    private boolean isVideo;

    public ItemImageCacheAdapter(Context context, ArrayList<String> objects, boolean isVideo) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
        this.isVideo = isVideo;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public String getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_image_cache, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((String) getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(String object, ViewHolder holder) {
        UtilsImage.load(context, object, holder.image);
        if (isVideo)
            holder.video.setVisibility(View.VISIBLE);
        else {
            holder.video.setVisibility(View.GONE);
        }
    }

    protected class ViewHolder {
        private FrameLayout activityBase;
        private ImageView image;
        private ImageView video;

        public ViewHolder(View view) {
            activityBase = (FrameLayout) view.findViewById(R.id.activity_base);
            image = (ImageView) view.findViewById(R.id.image);
            video = (ImageView) view.findViewById(R.id.isVideo);
        }
    }
}
