package com.liuhai.wcbox.lock.act;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liuhai.wcbox.R;
import com.liuhai.wcbox.lock.utils.UtilsImage;

import java.io.File;
import java.util.ArrayList;


public class WCImageViewpagerActivity extends AppCompatActivity implements View.OnClickListener {
    protected Button delete;
    public static ArrayList<String> objects;
    protected ArrayList<ImageView> images = new ArrayList<>();
    private MyViewPagerAdapter adapter;
    protected ViewPager viewpager;
    protected TextView points;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_image_viewpager);
        index = getIntent().getIntExtra("index", 0);


        for (int i = 0; i < WCImageViewpagerActivity.objects.size(); i++) {
            ImageView image = new ImageView(this);
            images.add(image);
        }

        initView();
    }


    private void initView() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        points = (TextView) findViewById(R.id.points);
        adapter = new MyViewPagerAdapter(images);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(index);
        points.setText((1 + index) + "/" + WCImageViewpagerActivity.objects.size());
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                position = position + 1;
                points.setText(position + "/" + WCImageViewpagerActivity.objects.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(WCImageViewpagerActivity.this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.delete) {
            File file = new File(WCImageViewpagerActivity.objects.get(viewpager.getCurrentItem()).replace("file://",""));
            file.delete();
            WCImageViewpagerActivity.objects.remove(viewpager.getCurrentItem());
            images.remove(viewpager.getCurrentItem());
            int index2=viewpager.getCurrentItem();
            adapter.notifyDataSetChanged();
            viewpager.setAdapter(adapter);
            viewpager.setCurrentItem(index2);
            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
        }
    }


    class MyViewPagerAdapter extends PagerAdapter {
        ArrayList<ImageView> images;
        public MyViewPagerAdapter( ArrayList<ImageView> images) {
            this.images=images;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(images.get(position));
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(images.get(position), 0);
            UtilsImage.load(WCImageViewpagerActivity.this, WCImageViewpagerActivity.objects.get(position), images.get(position));
            return images.get(position);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

}
