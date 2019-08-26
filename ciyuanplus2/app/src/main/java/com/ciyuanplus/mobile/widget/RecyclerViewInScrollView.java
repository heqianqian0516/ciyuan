package com.ciyuanplus.mobile.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Alen on 2017/12/19.
 * <p>
 * 解决ScrollView与RecyclerView横向滚动时的事件冲突
 */

public class RecyclerViewInScrollView extends RecyclerView {

    private float lastX, lastY;

    public RecyclerViewInScrollView(Context context) {
        super(context);
    }

    public RecyclerViewInScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewInScrollView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {

        boolean intercept = super.onInterceptTouchEvent(e);

        switch (e.getAction()) {

            case MotionEvent.ACTION_DOWN:
                lastX = e.getX();
                lastY = e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // 只要横向大于竖向，就拦截掉事件。
                float slopX = Math.abs(e.getX() - lastX);
                float slopY = Math.abs(e.getY() - lastY);
                if (slopX < 10.0f && slopY < 10.0f) {
                    intercept = false;
                    break;
                }
                //  Log.log("slopX=" + slopX + ", slopY="  + slopY);
                if (slopX >= slopY) {
                    requestDisallowInterceptTouchEvent(true);
                    intercept = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
        }
        // Log.log("intercept"+e.getAction()+"=" + intercept);
        return intercept;
    }

}