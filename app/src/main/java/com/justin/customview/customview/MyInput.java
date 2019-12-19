package com.justin.customview.customview;
/*
 * created by Justin on 2019-12-19
 * email: wcw1992yu@163.com
 * github: https://github.com/wangchongwei
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;

public class MyInput extends AppCompatEditText {


    public MyInput(Context context) {
        super(context);
    }

    public MyInput(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyInput(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }



}
