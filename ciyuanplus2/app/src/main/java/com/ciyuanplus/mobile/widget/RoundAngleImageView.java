package com.ciyuanplus.mobile.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;


/**
 * 实现圆角image
 *
 * @author Alen
 * <p>
 * 不用了 drop
 */
public class RoundAngleImageView extends AppCompatImageView {
    /*圆角的半径，依次为左上角xy半径，右上角，右下角，左下角*/
    private final float[] rids = {10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 0.0f, 0.0f,};
    private final float[] rids2 = {10.0f, 10.0f, 10.0f, 10.0f, 0.0f, 0.0f, 10.0f, 10.0f,};
    private int mType = 0; //0 左下直角   1右下直角

    public RoundAngleImageView(Context context) {
        super(context);
    }

    public RoundAngleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public RoundAngleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    /**
     * 画图
     * by Hankkin at:2015-08-30 21:15:53
     *
     * @param canvas
     */
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        int w = this.getWidth();
        int h = this.getHeight();
        /*向路径中添加圆角矩形。radii数组定义圆角矩形的四个圆角的x,y半径。radii长度必须为8*/
        path.addRoundRect(new RectF(0, 0, w, h), mType == 0 ? rids : rids2, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}