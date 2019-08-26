package com.ciyuanplus.mobile.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.ciyuanplus.mobile.utils.Utils;

/**
 * Created by Alen on 2016/3/13.
 */
public class IndicatorView extends View {
    private static final int MAX_NUM_DOT = 25;
    // state
    private int mAll = 0;
    // 0 based
    private int mCur = 0;
    private final int mDiameter = Utils.dip2px(32);
    private int mMargin = 10;
    private final Paint mNow;
    private final Paint mNormal;

    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mNow = new Paint();
        mNow.setColor(0xff333333);
        mNormal = new Paint();
        mNormal.setColor(0xffbbbbbb);
        // load resource
        // update relatied variable
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // judge part
        if (mAll < 0)
            return;
        if (mAll > MAX_NUM_DOT)
            mAll = MAX_NUM_DOT;
        if (mCur < 0)
            mCur = 0;
        if (mCur >= mAll)
            mCur = mAll - 1;

        // when the number of point is less than 2, we will draw nothing
        if (mAll <= 1)
            return;

        // begin draw
        int length = mDiameter * mAll;
        int xPoint = (getWidth() - length + mDiameter) / 2 - Utils.dip2px(25) / 2;
        int radius = 8;
        int yPoint = radius;
        for (int i = 0; i < mAll; ++i) {
            canvas.drawRect(new Rect(xPoint, yPoint, xPoint + Utils.dip2px(25), yPoint + Utils.dip2px(2)), (mCur == i ? mNow : mNormal));
            xPoint += mDiameter;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /**
     * @param all
     * @param cur, 0 based
     */
    public void setState(int all, int cur) {
        mAll = all;
        mCur = cur;
        invalidate();
    }

    public void setState(int all) {
        mAll = all;
        invalidate();
    }
}
