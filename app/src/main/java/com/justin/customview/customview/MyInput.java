package com.justin.customview.customview;
/*
 * created by Justin on 2019-08-29
 * email: wcw1992yu@163.com
 * github: https://github.com/wangchongwei
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.appcompat.widget.AppCompatEditText;

import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.justin.customview.R;


public class MyInput extends AppCompatEditText {

    // 上下文
    private Context context;

    // 获取焦点、失去焦点时的标题画笔
    private Paint focusTitlePaint, noFocusTitlePaint;

    // 标题 即提示语句
    private String title = "";
    // 是否获取到输入框焦点
    private boolean isFocus = false;

    /** 四个顶点的方位 */
    private int left, top, right, bottom;

    /** 输入框的文本内容 */
    private String value = "";

    public MyInput(Context context) {
        super(context);
        this.context = context;
    }

    public MyInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(attrs);
    }



    public MyInput(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(attrs);
    }

    private int getLocalColor(int id) {
        return context.getResources().getColor(id);
    }

    /**
     * 视图初始化
     * @param attrs
     */
    private void initView(AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyInput);

        title = typedArray.getString(R.styleable.MyInput_title);
        int titleColor = typedArray.getColor(R.styleable.MyInput_titleColor, getLocalColor(R.color.black));
        int hintColor = typedArray.getColor(R.styleable.MyInput_hintColor, getLocalColor(R.color.black));
        float titleSize = typedArray.getLayoutDimension(R.styleable.MyInput_title, 40);
        float hintSize = typedArray.getLayoutDimension(R.styleable.MyInput_hintSize, 30);


        typedArray.recycle();


        focusTitlePaint = new Paint();
        focusTitlePaint.setTextSize(hintSize);
        focusTitlePaint.setColor(hintColor);

        noFocusTitlePaint = new Paint();
        noFocusTitlePaint.setTextSize(titleSize);
        noFocusTitlePaint.setColor(titleColor);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if(isFocus) {
            super.onLayout(changed, left, top + 40, right, bottom);
        } else {
            super.onLayout(changed, left, top, right, bottom);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        top = getPaddingTop();
        left = getPaddingLeft();
        right = width - left - getPaddingRight();
        bottom = height - top;
        drawTitle(canvas);

        super.onDraw(canvas);

    }

    /**
     * 绘制标题
     * @param canvas
     */
    private void drawTitle(Canvas canvas) {
        if(TextUtils.isEmpty(value)) {
            if(isFocus) {
                canvas.drawText(title, left, top + 20, focusTitlePaint);
            } else {
                canvas.drawText(title, left, bottom - 5 , noFocusTitlePaint);
            }
        } else {
            canvas.drawText(title, left, top + 20, focusTitlePaint);
        }

    }


    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        isFocus = focused;
        postInvalidate();
    }


    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        value = text.toString();
    }

}
