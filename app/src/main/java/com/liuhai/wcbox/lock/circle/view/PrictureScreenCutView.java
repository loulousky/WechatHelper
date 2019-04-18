package com.liuhai.wcbox.lock.circle.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

/**
 * Created by chenliangj2ee on 2017/8/15.
 */

public class PrictureScreenCutView extends FrameLayout implements View.OnTouchListener {


    protected Button centerButton;
    private int top = 0, bottom = 0, left = 0, right = 0;
    private int actionBar;

    private int lastX;
    private int lastY;
    private int x, y;
    Paint p = new Paint();

    public PrictureScreenCutView(Context context) {
        super(context);
        setWillNotDraw(false);
        p.setColor(Color.YELLOW);
        p.setStrokeWidth(5);
        p.setStyle(Paint.Style.STROKE);
        PathEffect effects = new DashPathEffect(new float[]{10, 10, 10, 10}, 1);
        p.setPathEffect(effects);
        p.setAntiAlias(true);
        this.setOnTouchListener(this);
        actionBar=  context.getSharedPreferences("LockScreed", 0).getInt("actionBarHeight",65);
    }

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;

    public void show() {
        top = 0; bottom = 0; left = 0; right = 0;
        mWindowManager = (WindowManager) getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mLayoutParams.format = PixelFormat.RGBA_8888;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.width = mWindowManager.getDefaultDisplay().getWidth();
        mLayoutParams.height =  mWindowManager.getDefaultDisplay().getHeight();
        mLayoutParams.x = 0;
        mLayoutParams.y = 0;
         setSystemUiVisibility(View.INVISIBLE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        mWindowManager.addView(this, mLayoutParams);
    }

    public void remove() {
        mWindowManager.removeView(this);
    }


    @Override
    public void postInvalidate() {
        super.postInvalidate();
        mWindowManager.updateViewLayout(this, mLayoutParams);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        p.setColor(Color.YELLOW);

        Path path = new Path();
        path.moveTo(left, top);
        path.lineTo(right, top);
        canvas.drawPath(path, p);

        path = new Path();
        path.moveTo(left, bottom);
        path.lineTo(right, bottom);
        canvas.drawPath(path, p);

        path = new Path();
        path.moveTo(left, top);
        path.lineTo(left, bottom);
        canvas.drawPath(path, p);

        path = new Path();
        path.moveTo(right, top);
        path.lineTo(right, bottom);
        canvas.drawPath(path, p);


    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            top = (int) motionEvent.getRawY();
            Log.i("AeraSelectView", "top:"+top);
            left = (int) motionEvent.getRawX();
            return false;
        }

        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            bottom = (int) motionEvent.getRawY();
            right = (int) motionEvent.getRawX();
            Log.i("PrictureScreenCutView", "top:"+top+"   bottom:"+bottom+"  left:"+left+"   right:"+right);
        }

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            Log.i("PrictureScreenCutView", "top:"+top+"   bottom:"+bottom+"  left:"+left+"   right:"+right);
            if(screenCutListener!=null){
                screenCutListener.start( top  , bottom , left  , right);
            }
            remove();
        }
        postInvalidate();
        return true;
    }
    private screenCutListener screenCutListener;
    public void setScreenCutListener(screenCutListener screenCutListener){
        this.screenCutListener=screenCutListener;
    }
    public interface screenCutListener{
        public void start(int top, int bottom, int left, int right);
    }
}
