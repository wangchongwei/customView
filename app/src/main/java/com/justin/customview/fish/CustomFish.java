package com.justin.customview.fish;
/*
 * created by Justin on 2020/12/18
 * email: wcw1992yu@163.com
 * github: https://github.com/wangchongwei
 */

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomFish extends Drawable {

    private Paint mPaint;

    private Path mPath;

    // 鱼头半径
    public final int HEAD_RADIUS = 70;
    // 鱼身长度
    private float BODY_LENGTH = HEAD_RADIUS * 3.2f;
    // 寻找鱼鳍起始点坐标的线长
    private float FIND_FINS_LENGTH = 0.9f * HEAD_RADIUS;
    // 鱼鳍的长度
    private float FINS_LENGTH = 1.3f * HEAD_RADIUS;
    // 大圆的半径
    private float BIG_CIRCLE_RADIUS = 0.7f * HEAD_RADIUS;
    // 中圆的半径
    private float MIDDLE_CIRCLE_RADIUS = 0.6f * BIG_CIRCLE_RADIUS;
    // 小圆半径
    private float SMALL_CIRCLE_RADIUS = 0.4f * MIDDLE_CIRCLE_RADIUS;
    // --寻找尾部中圆圆心的线长
    private final float FIND_MIDDLE_CIRCLE_LENGTH = BIG_CIRCLE_RADIUS * (0.6f + 1);
    // --寻找尾部小圆圆心的线长
    private final float FIND_SMALL_CIRCLE_LENGTH = MIDDLE_CIRCLE_RADIUS * (0.4f + 2.7f);
    // --寻找大三角形底边中心点的线长
    private final float FIND_TRIANGLE_LENGTH = MIDDLE_CIRCLE_RADIUS * 2.7f;


    // 透明度
    private int OTHER_ALPHA = 110;
    private int BODY_ALPHA = 160;

    // 重心点
     private PointF mindlePoint;
    // 鱼的主要朝向角度
    private float fishMainAngle = 0;

    public CustomFish() {

        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        // 设置颜色
        mPaint.setARGB(OTHER_ALPHA, 244, 92, 71);


        // 重心点应该是图形的中心点，因为长、宽都是8.38f
        mindlePoint = new PointF(HEAD_RADIUS * 4.19f, HEAD_RADIUS * 4.19f);

    }


    @Override
    public void draw(@NonNull Canvas canvas) {

        // 获取角度
        float fishAngle = fishMainAngle;
        // 绘制布局
        // 获取鱼头圆的中心点坐标
        PointF headPoint = calculatePoint(mindlePoint, BODY_LENGTH / 2, fishAngle);
        // 绘制鱼头
        canvas.drawCircle(headPoint.x, headPoint.y, HEAD_RADIUS, mPaint);
        // 绘制下鱼鳍
        // 获取下鱼鳍右起点
        PointF bottomRightPoint = calculatePoint(headPoint, FIND_FINS_LENGTH, fishAngle - 110);
        makeFins(canvas, bottomRightPoint, fishAngle, false);

        // 绘制上鱼鳍
        PointF topRightPoint = calculatePoint(headPoint, FIND_FINS_LENGTH, fishAngle + 110);
        makeFins(canvas, topRightPoint, fishAngle, true);

        // 绘制鱼尾部分的大圆
        PointF bigCirclePoint = calculatePoint(mindlePoint, BODY_LENGTH / 2, fishAngle - 180);
        canvas.drawCircle(bigCirclePoint.x, bigCirclePoint.y, BIG_CIRCLE_RADIUS, mPaint);
        // 绘制鱼尾部分的中圆
        PointF middleCiclePoint = calculatePoint(bigCirclePoint, BIG_CIRCLE_RADIUS + MIDDLE_CIRCLE_RADIUS, fishMainAngle - 180);
        canvas.drawCircle(middleCiclePoint.x, middleCiclePoint.y, MIDDLE_CIRCLE_RADIUS, mPaint);
        // 绘制两个圆构成的梯形
        PointF rightTopPoint = calculatePoint(bigCirclePoint, BIG_CIRCLE_RADIUS, fishAngle + 90);
        PointF rightBottomPoint = calculatePoint(bigCirclePoint, BIG_CIRCLE_RADIUS, fishAngle - 90);
        PointF leftTopPoint = calculatePoint(bigCirclePoint, MIDDLE_CIRCLE_RADIUS, fishAngle + 90);
        PointF lefttBottomPoint = calculatePoint(bigCirclePoint, MIDDLE_CIRCLE_RADIUS, fishAngle - 90);

        mPath.reset();
        mPath.moveTo(rightTopPoint.x, rightTopPoint.y);
        mPath.lineTo(rightBottomPoint.x, rightBottomPoint.y);
        mPath.lineTo(leftTopPoint.x, leftTopPoint.y);
        mPath.lineTo(lefttBottomPoint.x, lefttBottomPoint.y);
        canvas.drawPath(mPath, mPaint);



    }

    private void makeFins(Canvas canvas, PointF startPoint, float fishMainAngle, boolean isTop) {
        float controllAngle = 115;
        // 获取鱼鳍终点
        PointF bottomLeftPoint = calculatePoint(startPoint, FINS_LENGTH, fishMainAngle - 180);
        // 获取控制点
        PointF controllPont = calculatePoint(startPoint, FINS_LENGTH * 1.8f, isTop ? fishMainAngle + controllAngle :fishMainAngle - controllAngle);
        // 绘制鱼鳍
        mPath.reset();
        mPath.moveTo(startPoint.x, startPoint.y);
        // 绘制贝塞尔曲线
        mPath.quadTo(controllPont.x, controllPont.y, bottomLeftPoint.x, bottomLeftPoint.y);
        canvas.drawPath(mPath, mPaint);
    }


    /**
     * 获取要求点的坐标
     * @param startPoint：起始点坐标
     * @param length： 起始点到要求点的长度
     * @param angle：起始点到要求点形成线 在坐标系中的夹角
     * @return
     */
    public PointF calculatePoint(PointF startPoint, float length, float angle) {

        // Math.toRadians 是将角度转换为弧度
        float x = (float) (length * Math.cos(Math.toRadians(angle)));
        // 因为android坐标系y轴正值向下，所以要减180度
        float y = (float) (length * Math.sin(Math.toRadians(angle - 180)));

        return new PointF(x + startPoint.x, y + startPoint.y);
    }



    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) (HEAD_RADIUS * 8.38f);
    }

    @Override
    public int getIntrinsicWidth() {
        return (int)(HEAD_RADIUS * 8.38f);
    }
}
