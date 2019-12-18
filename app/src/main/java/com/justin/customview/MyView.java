package com.justin.customview;
/*
 * created by Justin on 2019-12-12
 * email: wcw1992yu@163.com
 * github: https://github.com/wangchongwei
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.icu.util.Measure;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

public class MyView extends View {

    private Context context;
    private final String TAG = MyView.class.getName();

    /** 默认尺寸 */
    private int defaultHeight = 400, defaultWidth = 100;
    // 测量尺寸
    private int width, height;
    /** 四个顶点 */
    private int left, top, right, bottom;
    /** 四个偏距 */
    private int paddingLeft, paddingTop, paddingRight, paddingBottom;
    /** 点的大小 */
    private int pointSize = 10;
    /** 画笔的颜色 */
    private int paintColor;
    /** 画笔 */
    private Paint paint;


    // 数据
    private float[] yValue;
    private String[] xValue;
    private int lineNum = 5; // 标准线条数
    // y的最大值
    private int yMax = 100;
    // 最后一个点的位置
    private float lastX = 0f, lastY =0f;

    public MyView(Context context) {
        super(context);
        this.context = context;
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initData(attrs);
    }



    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    /** 设置数据并刷新 */
    public void setData(float[]yValue, String[]xValue) {
        this.yValue = yValue;
        this.xValue = xValue;
        postInvalidate();
    }

    /** 设置标准线数目 */
    public void setData(int lineNum) {
        this.lineNum = lineNum;
        postInvalidate();
    }

    /**
     * 视图初始化之前，获取xml中配置的数据
     * @param attrs
     */
    private void initData(AttributeSet attrs) {
        Log.d(TAG, "initData: ");
        // 获取xml中配置的数据
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyView);
        paintColor = array.getColor(R.styleable.MyView_axieColor, context.getResources().getColor(R.color.black));

        // 画笔初始化
        paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.black));
        paint.setTextSize(40);
//        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(10); // 线条粗细
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure: ");
        height = measuretDimension(defaultHeight, heightMeasureSpec);
        width = measuretDimension(0, widthMeasureSpec);


        initSize();
    }

    /**
     * 尺寸数据初始化
     */
    private void initSize () {
        // 获取padding尺寸
        paddingLeft = getPaddingLeft();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();
        paddingBottom = getPaddingBottom();
        StringBuilder sb = new StringBuilder();
        sb.append("paddingLeft =").append(paddingLeft)
                .append("paddingTop =").append(paddingTop)
                .append("paddingRight =").append(paddingRight)
                .append("paddingBottom =").append(paddingBottom);
        Log.d(TAG, "initSize: ".concat(sb.toString()));
        // 得出绘制区域四个顶点
        top = paddingTop;
        left = paddingLeft;
        bottom = height - top - paddingBottom;
        right = width - left;
        // 给四个方向都留点边距绘制其他
        top = top + 0;
        left = left + 50;
        bottom = bottom - 50;
        right = right - 0;
        setMeasuredDimension(width, height);
    }

    /**
     * 测量实际尺寸
     * @param defaultSize: 默认尺寸
     * @param measureSpec: 测量规格
     * @return
     */
    public int measuretDimension(int defaultSize, int measureSpec) {
        int resultSize = defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            // 没有做限制，取默认值
            case MeasureSpec.UNSPECIFIED:
                resultSize = defaultSize;
                break;

            // WRAP
            case MeasureSpec.AT_MOST:
                // 要取默认值和测量值中较小值
                resultSize = defaultSize == 0 ? specSize :Math.min(defaultSize, specSize);
                break;

            // 具体值 或 MATCH
            case MeasureSpec.EXACTLY:
                resultSize = specSize;
                break;

            default:
                break;
        }
        return resultSize;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        StringBuilder sb = new StringBuilder();
        sb.append("layout:");
        sb.append("&left=").append(left).append("&top=").append(top).append("&right=").append(right).append("&bottom=").append(bottom);
        Log.d(TAG,  sb.toString());
        super.onLayout(changed, left, top, right, bottom);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.d(TAG, "onDraw: ");
        drawXY(canvas);
        drawArrow(canvas);
    }

    /** 绘制两条轴线 */
    private void drawXY(Canvas canvas) {
        Log.d(TAG, "drawXY: ");
        // 绘制x轴
        canvas.drawLine(left, bottom, right, bottom, paint);
        // 绘制y轴
        canvas.drawLine(left, top, left, bottom, paint);
    }

    /** 绘制两个箭头 */
    private void drawArrow(Canvas canvas) {
        Path path = new Path();
        // 先绘制x轴三角
        //先移动到三角形一个点
        path.moveTo(right-20, bottom + 20);
        path.lineTo(right-20, bottom - 20); // 画线
        path.lineTo(right, bottom); // 画线
        path.close(); // 图形闭合
        canvas.drawPath(path, paint);

        // 绘制y轴三角
        path.moveTo(left - 20, top + 20);
        path.lineTo(left + 20, top + 20);
        path.lineTo(left, top);
        path.close();
        canvas.drawPath(path, paint);

        drawPoint(canvas);
    }

    /** 绘制各个点 */
    private void drawPoint(Canvas canvas) {
        if(xValue == null || yValue == null) return;
        // 先绘制5条y轴标准线位置 取高度的90%作为图线的最高。
        float maxHeight = (float)((bottom - top) * 0.9);
        float itemHeight = maxHeight / 5;
        for(int i = 1; i <=5; i ++) {
            canvas.drawLine(left, bottom - itemHeight * i, left + 15, bottom - itemHeight * i, paint);
        }

        // 再绘制x轴的数据， x轴线的标准值就是x轴的值，数目也是xValue的值
        float maxWidth = (float)((right - left) * 0.9);
        float itemWidth = ((float) (maxWidth * 1.0)) / xValue.length;
        for (int i = 1; i <= xValue.length; i ++) {
            float x = left + itemWidth * i;
                    // 绘制轴线
            canvas.drawLine(x, bottom, x, bottom - 15, paint);
            // 绘制点
            float y = bottom - maxHeight * yValue[i-1] / yMax;
            canvas.drawCircle(x, y, 10, paint);

            if(lastX > 0f) {

            }

        }

    }


}
