package com.justin.customview.customview;
/*
 * created by Justin on 2020-01-03
 * email: wcw1992yu@163.com
 * github: https://github.com/wangchongwei
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyChat extends View {

    //画布大小
    private int viewWidth,viewHigh;
    //画布中心点坐标
    private int mCentreX, mCentreY;
    //坐标画笔
    private Paint mCoordinatePaint;
    //控制点、数据点画笔
    private Paint mPaintPoint;
    //画圆画笔
    private Paint mPaintBezier;
    //数据点半径
    private int mControlRadius = 200;
    //放置四个数据点的集合
    private List<PointF> mPointDatas;
    //方式8个控制点的集合
    private List<PointF> mPointControlls;
    //常量0.552284749831
    private float stu = 0.552284749831f;
    //圆变心进行变化计数
    private int count = 0;
    //变化类型
    private int changeType = 0;
    //变化速率
    private float rate = 5f;

    public MyChat(Context context) {
        super(context);
    }

    public MyChat(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyChat(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint(){
        count = 0;
        //初始化坐标画笔
        mCoordinatePaint = new Paint();
        mCoordinatePaint.setStyle(Paint.Style.STROKE);
        mCoordinatePaint.setColor(Color.BLACK);
        mCoordinatePaint.setStrokeWidth(2);
        //初始化控制点、数据点画笔
        mPaintPoint = new Paint();
        mPaintPoint.setColor(Color.BLACK);
        mPaintPoint.setStrokeWidth(10);
        mPaintPoint.setStyle(Paint.Style.FILL);
        mPaintPoint.setAntiAlias(true);
        //画圆画笔
        mPaintBezier = new Paint();
        mPaintBezier.setStyle(Paint.Style.STROKE);
        mPaintBezier.setColor(Color.RED);
        mPaintBezier.setStrokeWidth(5);
        mPaintBezier.setAntiAlias(true);
        //初始化数据点
        mPointDatas = new ArrayList<>();
        //初始化控制点
        mPointControlls = new ArrayList<>();

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //测量View宽高
        viewWidth = w;
        viewHigh = h;
        //获取View中心点
        mCentreX = viewWidth/2;
        mCentreY = viewHigh/2;
        //添加数据点
        mPointDatas.add(new PointF(mCentreX, mCentreY - mControlRadius));
        mPointDatas.add(new PointF(mCentreX + mControlRadius, mCentreY));
        mPointDatas.add(new PointF(mCentreX, mCentreY + mControlRadius));
        mPointDatas.add(new PointF(mCentreX - mControlRadius, mCentreY));
        //添加控制点
        mPointControlls.add(new PointF(mCentreX + mControlRadius * stu,mCentreY - mControlRadius));
        mPointControlls.add(new PointF(mCentreX + mControlRadius,mCentreY - mControlRadius * stu));

        mPointControlls.add(new PointF(mCentreX + mControlRadius,mCentreY + mControlRadius * stu));
        mPointControlls.add(new PointF(mCentreX + mControlRadius * stu,mCentreY + mControlRadius));

        mPointControlls.add(new PointF(mCentreX - mControlRadius * stu,mCentreY + mControlRadius));
        mPointControlls.add(new PointF(mCentreX - mControlRadius,mCentreY + mControlRadius * stu));

        mPointControlls.add(new PointF(mCentreX - mControlRadius,mCentreY - mControlRadius * stu));
        mPointControlls.add(new PointF(mCentreX - mControlRadius * stu,mCentreY - mControlRadius));

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画坐标
        canvas.drawLine(0, mCentreY,viewWidth, mCentreY, mCoordinatePaint);
        canvas.drawLine(mCentreX,0, mCentreX,viewHigh, mCoordinatePaint);
        //画数据点4个
        for (int i = 0; i < mPointDatas.size(); i++) {
            canvas.drawPoint(mPointDatas.get(i).x,mPointDatas.get(i).y,mPaintPoint);
        }
        //画控制点8个
        for (int i = 0; i < mPointControlls.size(); i++) {
            canvas.drawPoint(mPointControlls.get(i).x,mPointControlls.get(i).y,mPaintPoint);
        }
        //贝塞尔三阶画圆
        for (int i = 0; i < mPointDatas.size(); i++) {
            if(i < (mPointDatas.size() - 1)) {
                Path path = new Path();
                path.moveTo(mPointDatas.get(i).x, mPointDatas.get(i).y);
                path.cubicTo(mPointControlls.get(i*2).x, mPointControlls.get(i*2).y, mPointControlls.get(i*2+1).x, mPointControlls.get(i*2+1).y,
                        mPointDatas.get(i+1).x, mPointDatas.get(i+1).y);
                //绘制路径
                canvas.drawPath(path, mPaintBezier);
            }else {
                Path path = new Path();
                path.moveTo(mPointDatas.get(i).x, mPointDatas.get(i).y);
                path.cubicTo(mPointControlls.get(i*2).x, mPointControlls.get(i*2).y, mPointControlls.get(i*2+1).x, mPointControlls.get(i*2+1).y,
                        mPointDatas.get(0).x, mPointDatas.get(0).y);
                //绘制路径
                canvas.drawPath(path, mPaintBezier);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }



    public void start(int changeType){
        this.changeType = changeType;
        handler.postDelayed(runnable, 1000);
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(changeType == 0) {//圆变化成心
                if (count * rate < 100) {
                    mPointDatas.get(0).y = mPointDatas.get(0).y + 1f * rate;

                    mPointControlls.get(2).x = mPointControlls.get(2).x - 0.2f * rate;
                    mPointControlls.get(3).y = mPointControlls.get(3).y - 0.8f * rate;

                    mPointControlls.get(4).y = mPointControlls.get(4).y - 0.8f * rate;
                    mPointControlls.get(5).x = mPointControlls.get(5).x + 0.2f * rate;

                    invalidate();
                    count ++;
                    handler.postDelayed(this, 50);
                }
            }else if(changeType == 1){//四叶草
                if (count * rate < 200) {

                    mPointDatas.get(0).y = mPointDatas.get(0).y + 1f * rate;
                    mPointDatas.get(1).x = mPointDatas.get(1).x - 1f * rate;
                    mPointDatas.get(2).y = mPointDatas.get(2).y - 1f * rate;
                    mPointDatas.get(3).x = mPointDatas.get(3).x + 1f * rate;

                    invalidate();
                    count ++;
                    handler.postDelayed(this, 100);
                }
            }else if(changeType == 2){//水滴
                if (count * rate < 100 ) {

                    mPointDatas.get(0).y = mPointDatas.get(0).y - 1f * rate;
                    mPointDatas.get(1).x = mPointDatas.get(1).x - 0.35f * rate;
                    mPointDatas.get(3).x = mPointDatas.get(3).x + 0.35f * rate;

                    for (int i = 0; i < mPointControlls.size(); i++) {
                        if(i == 0 || i == 1 || i == 6 || i ==7 ) {
                            if (mPointControlls.get(i).x > mCentreX) {
                                mPointControlls.get(i).x = mPointControlls.get(i).x - 0.4f * rate;
                            } else {
                                mPointControlls.get(i).x = mPointControlls.get(i).x + 0.4f * rate;
                            }

                            if (mPointControlls.get(i).y > mCentreY) {
                                mPointControlls.get(i).y = mPointControlls.get(i).y - 0.4f * rate;
                            } else {
                                mPointControlls.get(i).y = mPointControlls.get(i).y + 0.4f * rate;
                            }
                        }else if(i == 2 || i == 3 || i == 4 || i ==5 ) {
                            if (mPointControlls.get(i).x > mCentreX) {
                                mPointControlls.get(i).x = mPointControlls.get(i).x - 0.2f * rate;
                            } else {
                                mPointControlls.get(i).x = mPointControlls.get(i).x + 0.2f * rate;
                            }

                            if (mPointControlls.get(i).y > mCentreY) {
                                mPointControlls.get(i).y = mPointControlls.get(i).y - 0.2f * rate;
                            } else {
                                mPointControlls.get(i).y = mPointControlls.get(i).y + 0.2f * rate;
                            }
                        }
                    }

                    invalidate();
                    count ++;
                    handler.postDelayed(this, 100);
                }
            }
        }
    };
}
