package com.liuhai.wcbox.lock.circle.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.liuhai.wcbox.R;


public class QQSecureView extends View {
    /** 绘制渐变呼吸环的画笔 */
    private Paint mGradientPaint;
    /** 绘制3段弧形的画笔 */
    private Paint mArcPaint;
    /** 绘制中心透明圆的画笔 */
    private Paint mBackPaint;
    /** 绘制正三角形的画笔 */
    private Paint mTrianglePaint;
    /** 绘制正三角形内虚线的画笔 */
    private Paint mLinePaint;
    /** 绘制中心图片的画笔 */
    private Paint mBitmapPaint;
    /** 绘制虚线上移动游标的画笔 */
    private Paint mLightPaint;
    /** 外圆半径 */
    private float mRadius;
    /** 中心透明圆的半径 */
    private float mBackRadius;
    /** View的宽高 */
    private int width, height;
    /** 渐变呼吸环的宽 */
    private int mGradientStrokeWidth = 50;
    /** 3段环的宽 */
    private int mArcStrokeWidth = mGradientStrokeWidth / 2;
    /** 画三角形路径 */
    private Path mTrianglePath;
    /** 画三角形内部虚线路径 */
    private Path mLinePath1, mLinePath2, mLinePath3;
    /** 中心图片 */
    private Bitmap bitmap;
    /** 动画 */
    private ValueAnimator valueAnimator;

    private SweepGradient sweepGradient;//扫描渲染
    private LinearGradient linearGradient;//线性渲染
    private RadialGradient radialGradient;//环形渲染

    private RectF mGradientRectF;
    private RectF mBorderRectF;
    private Rect mBitmapRect;

    /** 3段环的弧形角度 */
    private float mSweepAngle;
    /** 3段环的间隔角度 */
    private float mGapAngle = 3f;
    /** 呼吸环的弧形角度 */
    private float mGradientSweepAngle;
    /** 呼吸环的间隔角度 */
    private float mGradientGapAngle = 0.1f;
    /** 游标长度 */
    private int mLightLen = 10;
    /** 动画执行的当前值 */
    private float mCurrentValue;
    /** 是否是呼吸动画 */
    private boolean isBreath;
    /** 记录动画执行的次数来判断呼吸动画和游标动画的执行 */
    private int step;

    public QQSecureView(Context context) {
        super(context);
        init();
    }

    public QQSecureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QQSecureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mGradientPaint = new Paint();
        mGradientPaint.setDither(true);
        mGradientPaint.setAntiAlias(true);
        mGradientPaint.setStyle(Paint.Style.FILL);
        mGradientPaint.setStrokeWidth(mGradientStrokeWidth);

        mArcPaint = new Paint();
        mArcPaint.setDither(true);
        mArcPaint.setAntiAlias(true);
        mArcPaint.setColor(Color.argb(160, 255, 255, 255));
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(mArcStrokeWidth);

        mBackPaint = new Paint();
        mBackPaint.setDither(true);
        mBackPaint.setAntiAlias(true);
        mBackPaint.setColor(Color.argb(25, 0, 0, 0));
        mBackPaint.setStyle(Paint.Style.FILL);

        mTrianglePaint = new Paint();
        mTrianglePaint.setDither(true);
        mTrianglePaint.setAntiAlias(true);
        mTrianglePaint.setStrokeWidth(1);
        mTrianglePaint.setColor(Color.argb(80, 255, 255, 255));
        mTrianglePaint.setStyle(Paint.Style.STROKE);

        mLinePaint = new Paint();
        mLinePaint.setDither(true);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(1);
        mLinePaint.setColor(Color.argb(80, 255, 255, 255));
        mLinePaint.setStyle(Paint.Style.STROKE);
        PathEffect effects = new DashPathEffect(new float[]{10, 5, 10, 5}, 1);
        mLinePaint.setPathEffect(effects);

        mBitmapPaint = new Paint();
        mBitmapPaint.setDither(true);
        mBitmapPaint.setAntiAlias(true);

        mLightPaint = new Paint();
        mLightPaint.setDither(true);
        mLightPaint.setAntiAlias(true);
        mLightPaint.setStrokeWidth(2);
        mLightPaint.setColor(Color.WHITE);
        mLightPaint.setStyle(Paint.Style.STROKE);
        mLightPaint.setStrokeCap(Paint.Cap.ROUND);

        mTrianglePath = new Path();
        mLinePath1 = new Path();
        mLinePath2 = new Path();
        mLinePath3 = new Path();

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_gui);

        valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setInterpolator(new AccelerateInterpolator());//先慢后快插值器
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);//无限循环
        valueAnimator.setDuration(2000);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentValue = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if (isBreath) {//呼吸动画重复两次，一次由淡到深，一次逆向
                    ++step;
                    if (step == 2) {
                        isBreath = false;
                    }
                } else {
                    step = 0;
                    isBreath = true;
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        width = getMeasuredWidth();
        height = getMeasuredHeight();

        mRadius = width / 2f;
        mBackRadius = mRadius - mGradientStrokeWidth / 4f - mArcStrokeWidth * 2;

        /*
        *环形渲染实现外圈渐变的呼吸环
        *颜色分布由内到外：全透明、半透明1、白色、半透明2
        *全透明色渐变终止位置：0
        *半透明1渐变终止位置：1 - (mGradientStrokeWidth / mRadius)
        *白色渐变终止位置：1 - (mGradientStrokeWidth / (2 * mRadius))
        *半透明2渐变终止位置：1
        */
        if (radialGradient == null) {
            radialGradient = new RadialGradient(width / 2, height / 2, mRadius, new int[]{Color.TRANSPARENT, Color.argb(20, 255, 255, 255), Color.argb(255, 255, 255, 255), Color.argb(20, 255, 255, 255)},
                    new float[]{0f, 1 - (mGradientStrokeWidth / mRadius), 1 - (mGradientStrokeWidth / (2 * mRadius)), 1f}, Shader.TileMode.CLAMP);
            mGradientPaint.setShader(radialGradient);
        }

        if (mGradientRectF == null) {
            mGradientRectF = new RectF(0, 0, width, height);
        }

        if (mBorderRectF == null) {
            mBorderRectF = new RectF(mGradientStrokeWidth / 4f + mArcStrokeWidth / 2f, mGradientStrokeWidth / 4f + mArcStrokeWidth / 2f, width - mGradientStrokeWidth / 4f - mArcStrokeWidth / 2f, height - mGradientStrokeWidth / 4f - mArcStrokeWidth / 2f);
        }

        mSweepAngle = (360 - 3 * mGapAngle) / 3f;
        mGradientSweepAngle = (360 - 3 * mGradientGapAngle) / 3f;

        mTrianglePath.moveTo(width / 2, height / 2 - mBackRadius);
        mTrianglePath.lineTo((float) (width / 2 + Math.cos(Math.toRadians(30)) * mBackRadius), height / 2 + mBackRadius / 2f);
        mTrianglePath.lineTo((float) (width / 2 - Math.cos(Math.toRadians(30)) * mBackRadius), height / 2 + mBackRadius / 2f);
        mTrianglePath.close();

        mLinePath1.moveTo(width / 2, height / 2);
        mLinePath1.lineTo(width / 2, height / 2 - mBackRadius);

        mLinePath2.moveTo(width / 2, height / 2);
        mLinePath2.lineTo((float) (width / 2 + Math.cos(Math.toRadians(30)) * mBackRadius), height / 2 + mBackRadius / 2f);

        mLinePath3.moveTo(width / 2, height / 2);
        mLinePath3.lineTo((float) (width / 2 - Math.cos(Math.toRadians(30)) * mBackRadius), height / 2 + mBackRadius / 2f);

        if (mBitmapRect == null) {
            float rectX1 = (float) (width / 2 - Math.cos(Math.toRadians(30)) * mBackRadius / 2);
            float rectX2 = (float) (width / 2 + Math.cos(Math.toRadians(30)) * mBackRadius / 2);
            float rectY2 = height / 2 + mBackRadius / 2f;
            float rectY1 = rectY2 - (rectX2 - rectX1) * 322 / 284f;
            mBitmapRect = new Rect((int) rectX1, (int) rectY1, (int) rectX2, (int) rectY2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //呼吸动画重复两次，一次由淡到深，一次逆向，游标动画直接从新执行
        valueAnimator.setRepeatMode(isBreath ? ValueAnimator.REVERSE : ValueAnimator.RESTART);

        if (isBreath) {
            canvas.save();
            int aph = (int) (255 * mCurrentValue);
            mGradientPaint.setAlpha(aph);//通过改变透明度来实现渐变环呼吸的动画效果
            for (int i = 0; i < 3; i++) {//绘制渐变的呼吸环
                canvas.drawArc(mGradientRectF, mGradientGapAngle / 2 - 90 + (mGradientSweepAngle + mGradientGapAngle) * i, mGradientSweepAngle, true, mGradientPaint);
            }
            canvas.restore();
        }

        for (int i = 0; i < 3; i++) {//绘制3段环
            canvas.drawArc(mBorderRectF, mGapAngle / 2 - 90 + (mSweepAngle + mGapAngle) * i, mSweepAngle, false, mArcPaint);
        }

        //绘制中心的半透明圆
        canvas.drawCircle(width / 2, height / 2, mBackRadius, mBackPaint);
        //绘制正三角形
        canvas.drawPath(mTrianglePath, mTrianglePaint);
        //绘制虚线
        canvas.drawPath(mLinePath1, mLinePaint);
        canvas.drawPath(mLinePath2, mLinePaint);
        canvas.drawPath(mLinePath3, mLinePaint);

        if (!isBreath) {
            //绘制从正三角形的上顶点到中心点的那条虚线上游标
            canvas.drawLine(width / 2, height / 2 - mBackRadius * mCurrentValue - mLightLen, width / 2, height / 2 - mBackRadius * mCurrentValue, mLightPaint);
            //把上面绘制的游标以中心点为中心按顺时针旋转120度则为第二个游标
            canvas.save();
            canvas.rotate(120, width / 2, height / 2);
            canvas.drawLine(width / 2, height / 2 - mBackRadius * mCurrentValue - mLightLen, width / 2, height / 2 - mBackRadius * mCurrentValue, mLightPaint);
            canvas.restore();
            //按顺时针旋转240度则为第二个游标
            canvas.save();
            canvas.rotate(240, width / 2, height / 2);
            canvas.drawLine(width / 2, height / 2 - mBackRadius * mCurrentValue - mLightLen, width / 2, height / 2 - mBackRadius * mCurrentValue, mLightPaint);
            canvas.restore();

            //正三角形底边上的两个左右方向移动的游标
            canvas.drawLine((float) (width / 2 - mBackRadius * Math.sin(Math.toRadians(60)) * mCurrentValue), height / 2 + mBackRadius / 2f, (float) (width / 2 - mBackRadius * Math.sin(Math.toRadians(60)) * mCurrentValue) - mLightLen, height / 2 + mBackRadius / 2f, mLightPaint);
            canvas.drawLine((float) (width / 2 + mBackRadius * Math.sin(Math.toRadians(60)) * mCurrentValue), height / 2 + mBackRadius / 2f, (float) (width / 2 + mBackRadius * Math.sin(Math.toRadians(60)) * mCurrentValue) + mLightLen, height / 2 + mBackRadius / 2f, mLightPaint);
            //以正三角形的左下顶点为中心按逆时针旋转60度则为第二个游标
            canvas.save();
            canvas.rotate(-60, (float) (width / 2 - Math.cos(Math.toRadians(30)) * mBackRadius), height / 2 + mBackRadius / 2f);
            canvas.drawLine((float) (width / 2 - mBackRadius * Math.sin(Math.toRadians(60)) * mCurrentValue), height / 2 + mBackRadius / 2f, (float) (width / 2 - mBackRadius * Math.sin(Math.toRadians(60)) * mCurrentValue) - mLightLen, height / 2 + mBackRadius / 2f, mLightPaint);
            canvas.drawLine((float) (width / 2 + mBackRadius * Math.sin(Math.toRadians(60)) * mCurrentValue), height / 2 + mBackRadius / 2f, (float) (width / 2 + mBackRadius * Math.sin(Math.toRadians(60)) * mCurrentValue) + mLightLen, height / 2 + mBackRadius / 2f, mLightPaint);
            canvas.restore();
            //以正三角形的右下顶点为中心按逆顺时针旋转60度则为第三个游标
            canvas.save();
            canvas.rotate(60, (float) (width / 2 + Math.cos(Math.toRadians(30)) * mBackRadius), height / 2 + mBackRadius / 2f);
            canvas.drawLine((float) (width / 2 - mBackRadius * Math.sin(Math.toRadians(60)) * mCurrentValue), height / 2 + mBackRadius / 2f, (float) (width / 2 - mBackRadius * Math.sin(Math.toRadians(60)) * mCurrentValue) - mLightLen, height / 2 + mBackRadius / 2f, mLightPaint);
            canvas.drawLine((float) (width / 2 + mBackRadius * Math.sin(Math.toRadians(60)) * mCurrentValue), height / 2 + mBackRadius / 2f, (float) (width / 2 + mBackRadius * Math.sin(Math.toRadians(60)) * mCurrentValue) + mLightLen, height / 2 + mBackRadius / 2f, mLightPaint);
            canvas.restore();
        }

        canvas.drawBitmap(bitmap, null, mBitmapRect, mBitmapPaint);

        startAnimator();
    }

    @Override
    protected void onDetachedFromWindow() {
        valueAnimator.cancel();
        super.onDetachedFromWindow();
    }

    private void startAnimator() {
        if (!valueAnimator.isStarted()) {
            valueAnimator.start();
        }
    }
}
