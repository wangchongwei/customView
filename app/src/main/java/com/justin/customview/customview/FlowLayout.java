package com.justin.customview.customview;
/*
 * created by Justin on 2020/12/4
 * email: wcw1992yu@163.com
 * github: https://github.com/wangchongwei
 * 自定义一个自动换行组件
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

class FlowLayout extends ViewGroup {

    private int mHorizontalSpacing = 18;
    private int mVerticalSpacing = 10;

    private List<List<View>> allView = new ArrayList<>(); // 记录所有的行 所有的view
    List<Integer> lineHeights = new ArrayList<>(); // 记录每一行的行高，用于layout

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        // 先获取父容器能给的尺寸大小
        int selfWidth = MeasureSpec.getSize(widthMeasureSpec);
        int selfHeight = MeasureSpec.getSize(heightMeasureSpec);
        // 获取子视图的数量
        int childViewCount = getChildCount();
        // 记录每行中的子view
        List<View>lineViews = new ArrayList<>();
        int lineWidthUsed = 0; // 每行已用宽度
        int lineHeight = 0; // 行高

        int parentNeededWidth = 0;  // measure过程中，子View要求的父ViewGroup的宽
        int parentNeededHeight = 0; // measure过程中，子View要求的父ViewGroup的高

        // 遍历所有的子View
        for (int i = 0 ; i < childViewCount; i ++) {
            View childView = getChildAt(i);
            // 获取子视图的宽、高
            LayoutParams lp = childView.getLayoutParams();
            // 如果View存在 因为GONE的视图不占位置
            if(childView.getVisibility() != GONE) {
                // 获取子视图的测量规格
                int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                        paddingLeft + paddingRight,
                        lp.width);
                int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                        paddingTop+paddingTop,
                        lp.height);
                // 设置子View的尺寸
                childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);

                // 当执行完measure函数后，即可获取到该子view的尺寸
                int childViewWidth = childView.getMeasuredWidth();
                int childVieeHeight = childView.getMeasuredHeight();


                // 当所用宽度 + 宽度间隔 + 当前子视图宽度 > 容器View的宽度，则需要换行
                if(lineWidthUsed + mHorizontalSpacing + childViewWidth > selfWidth) {
                    // 记录当前行
                    allView.add(lineViews);
                    lineHeights.add(lineHeight);

                    parentNeededHeight = parentNeededHeight + lineHeight + mVerticalSpacing;
                    parentNeededWidth = Math.max(parentNeededWidth, lineWidthUsed + mHorizontalSpacing);

                    // 清空当前行数据
                    lineViews = new ArrayList<>();
                    lineHeight = 0;
                    lineWidthUsed = 0;
                } else {
                    lineViews.add(childView);
                    lineHeight = Math.max(lineHeight, childVieeHeight);
                    lineWidthUsed = lineWidthUsed + childViewWidth + mHorizontalSpacing;
                }

                //处理最后一行数据
                if (i == childViewCount - 1) {
                    allView.add(lineViews);
                    lineHeights.add(lineHeight);
                    parentNeededHeight = parentNeededHeight + lineHeight + mVerticalSpacing;
                    parentNeededWidth = Math.max(parentNeededWidth, lineWidthUsed + mHorizontalSpacing);
                }
            }
        }
        // 通过子View的计算得出了需要的最小具体值 再根据spec一起得出最后应该的值
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int realWidth = widthMode == MeasureSpec.EXACTLY ? selfWidth : parentNeededWidth;
        int realHeight = heightMode == MeasureSpec.EXACTLY ? selfHeight : parentNeededHeight;

        setMeasuredDimension(realWidth, realHeight);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
