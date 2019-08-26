package com.ciyuanplus.mobile.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.ciyuanplus.mobile.utils.Utils;


/**
 * Created by Alen on 2016/9/20.
 */
public class SideBar extends View {
    private String[] mBarList;
    private int mChoosed = -1;
    private final Paint paint = new Paint();
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

    public SideBar(Context context) {
        super(context);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBarList(String[] barList) {
        mBarList = barList;
        postInvalidate();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取焦点改变背景颜色.
        if (mBarList == null || mBarList.length == 0) return;
        int height = Utils.getScreenHeight();// 获取对应高度
        ViewGroup.LayoutParams params = getLayoutParams();
        int SIGNLE_HEIGHT = 40;
        params.height = mBarList.length * SIGNLE_HEIGHT;
        setLayoutParams(params);
        setY((height - params.height) / 2);
        int width = getWidth(); // 获取对应宽度
        int singleHeight = 40;// 获取每一个字母的高度height / mBarList.length;

        for (int i = 0; i < mBarList.length; i++) {
            paint.setColor(Color.parseColor("#666666"));
            // paint.setColor(Color.WHITE);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(30);
            // 选中的状态
            if (i == mChoosed) {
                paint.setColor(Color.parseColor("#3399ff"));
                paint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - paint.measureText(mBarList[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(mBarList[i], xPos, yPos, paint);
            paint.reset();// 重置画笔
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (mBarList == null || mBarList.length == 0) {
            return super.dispatchTouchEvent(event);
        }
        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final int oldChoose = mChoosed;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * mBarList.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.

        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundDrawable(new ColorDrawable(0x00000000));
                mChoosed = -1;//
                invalidate();
//                if (mTextDialog != null) {
//                    mTextDialog.setVisibility(View.INVISIBLE);
//                }
                break;

            default:
                setBackgroundColor(Color.WHITE);
                if (oldChoose != c) {
                    if (c >= 0 && c < mBarList.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(mBarList[c]);
                        }
//                        if (mTextDialog != null) {
//                            mTextDialog.setText(b[c]);
//                            mTextDialog.setVisibility(View.VISIBLE);
//                        }
                        mChoosed = c;
                        invalidate();
                    }
                }

                break;
        }
        return true;
    }


    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }


    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }
}
