package com.ciyuanplus.mobile.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

import java.security.MessageDigest;

import androidx.annotation.NonNull;


/**
 * <pre>
 *     author : xiaweizi
 *     class  : com.xiaweizi.cornerslibrary.RoundCornersTransformation
 *     e-mail : 1012126908@qq.com
 *     time   : 2017/08/18
 *     desc   : 根据需求，对图片定制指定的圆角
 * </pre>
 */

public class RoundCornersTransformation implements Transformation<Bitmap> {


    private final BitmapPool mBitmapPool;
    private final int mRadius;
    private final int mDiameter;
    private CornerType mCornerType;


    public RoundCornersTransformation(Context context, int radius, CornerType type) {
        mBitmapPool = Glide.get(context).getBitmapPool();
        mCornerType = type;
        mRadius = radius;
        mDiameter = 2 * mRadius;
    }


    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }

    @NonNull
    @Override
    public Resource<Bitmap> transform(@NonNull Context context, @NonNull Resource<Bitmap> resource, int outWidth, int outHeight) {

        Bitmap source = resource.get();
        int width = source.getWidth();
        int height = source.getHeight();
        Bitmap bitmap = mBitmapPool.get(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        drawRoundRect(canvas, paint, width, height);
        return BitmapResource.obtain(bitmap, mBitmapPool);
    }

    private void drawRoundRect(Canvas canvas, Paint paint, float width, float height) {
        switch (mCornerType) {
            case LEFT_TOP:
                drawLeftTopCorner(canvas, paint, width, height);
                break;
            case LEFT_BOTTOM:
                drawLeftBottomCorner(canvas, paint, width, height);
                break;
            case RIGHT_TOP:
                drawRightTopCorner(canvas, paint, width, height);
                break;
            case RIGHT_BOTTOM:
                drawRightBottomCorner(canvas, paint, width, height);
                break;
            case LEFT:
                drawLeftCorner(canvas, paint, width, height);
                break;
            case RIGHT:
                drawRightCorner(canvas, paint, width, height);
                break;
            case BOTTOM:
                drawBottomCorner(canvas, paint, width, height);
                break;
            case TOP:
                drawTopCorner(canvas, paint, width, height);
                break;
            case EXCEPT_BOTTOM_LEFT:
                drawExceptBottomLeftCorner(canvas, paint, width, height);
                break;
            case EXCEPT_BOTTOM_RIGHT:
                drawExceptBottomRightCorner(canvas, paint, width, height);
                break;
            case ALL:
            default:
                canvas.drawRoundRect(new RectF(0, 0, width, height), mRadius, mRadius, paint);
                break;
        }
    }

    /**
     * 画左上角
     */
    private void drawLeftTopCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(mRadius, 0, width, height), paint);
        canvas.drawRect(new RectF(0, mRadius, mRadius, height), paint);
        canvas.drawArc(new RectF(0, 0, mDiameter, mDiameter), 180, 90, true, paint);
    }

    /**
     * 画左下角
     */
    private void drawLeftBottomCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(0, 0, width, height - mRadius), paint);
        canvas.drawRect(new RectF(mRadius, height - mRadius, width, height), paint);
        canvas.drawArc(new RectF(0, height - mDiameter, mDiameter, height), 90, 90, true, paint);
    }

    /**
     * 画右上角
     */
    private void drawRightTopCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(0, 0, width - mRadius, height), paint);
        canvas.drawRect(new RectF(width - mRadius, mRadius, width, height), paint);
        canvas.drawArc(new RectF(width - mDiameter, 0, width, mDiameter), 270, 90, true, paint);
    }

    /**
     * 画右下角
     */
    private void drawRightBottomCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(0, 0, width, height - mRadius), paint);
        canvas.drawRect(new RectF(0, height - mRadius, width - mRadius, height), paint);
        canvas.drawArc(new RectF(width - mDiameter, height - mDiameter, width, height), 0, 90, true, paint);
    }

    /**
     * 画左 角
     */
    private void drawLeftCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(mRadius, 0, width, height), paint);
        canvas.drawRect(new RectF(0, mRadius, mRadius, height - mRadius), paint);
        canvas.drawArc(new RectF(0, 0, mDiameter, mDiameter), 180, 90, true, paint);
        canvas.drawArc(new RectF(0, height - mDiameter, mDiameter, height), 90, 90, true, paint);
    }

    /**
     * 画右角
     */
    private void drawRightCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(0, 0, width - mRadius, height), paint);
        canvas.drawRect(new RectF(width - mRadius, mRadius, width, height - mRadius), paint);
        canvas.drawArc(new RectF(width - mDiameter, 0, width, mDiameter), 270, 90, true, paint);
        canvas.drawArc(new RectF(width - mDiameter, height - mDiameter, width, height), 0, 90, true, paint);
    }

    /**
     * 画上 角
     */
    private void drawTopCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(0, mRadius, width, height), paint);
        canvas.drawRect(new RectF(mRadius, 0, width - mRadius, mRadius), paint);
        canvas.drawArc(new RectF(0, 0, mDiameter, mDiameter), 180, 90, true, paint);
        canvas.drawArc(new RectF(width - mDiameter, 0, width, mDiameter), 270, 90, true, paint);
    }

    /**
     * 画下 角
     */
    private void drawBottomCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(0, 0, width, height - mRadius), paint);
        canvas.drawRect(new RectF(mRadius, height - mRadius, width - mRadius, height), paint);
        canvas.drawArc(new RectF(0, height - mDiameter, mDiameter, height), 90, 90, true, paint);
        canvas.drawArc(new RectF(width - mDiameter, height - mDiameter, width, height), 0, 90, true, paint);
    }

    /**
     * 画除去左下的三个角
     */
    private void drawExceptBottomLeftCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(0, mRadius, width, height - mRadius), paint);
        canvas.drawRect(new RectF(mRadius, 0, width - mRadius, mRadius), paint);
        canvas.drawRect(new RectF(0, height - mRadius, width - mRadius, height), paint);

        canvas.drawArc(new RectF(0, 0, mDiameter, mDiameter), 180, 90, true, paint);
        canvas.drawArc(new RectF(width - mDiameter, 0, width, mDiameter), 270, 90, true, paint);
        canvas.drawArc(new RectF(width - mDiameter, height - mDiameter, width, height), 0, 90, true, paint);

    }

    /**
     * 画除去右下的三个角
     */
    private void drawExceptBottomRightCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawRect(new RectF(0, mRadius, width, height - mRadius), paint);
        canvas.drawRect(new RectF(mRadius, 0, width - mRadius, mRadius), paint);
        canvas.drawRect(new RectF(mRadius, height - mRadius, width, height), paint);

        canvas.drawArc(new RectF(0, 0, mDiameter, mDiameter), 180, 90, true, paint);
        canvas.drawArc(new RectF(width - mDiameter, 0, width, mDiameter), 270, 90, true, paint);
        canvas.drawArc(new RectF(0, height - mDiameter, mDiameter, height), 90, 90, true, paint);


    }

    public String getId() {
        return "RoundedTransformation(radius=" + mRadius + ", diameter=" + mDiameter + ")";
    }

    public enum CornerType {
        /**
         * 所有角
         */
        ALL,
        /**
         * 左上
         */
        LEFT_TOP,
        /**
         * 左下
         */
        LEFT_BOTTOM,
        /**
         * 右上
         */
        RIGHT_TOP,
        /**
         * 右下
         */
        RIGHT_BOTTOM,
        /**
         * 左侧
         */
        LEFT,
        /**
         * 右侧
         */
        RIGHT,
        /**
         * 下侧
         */
        BOTTOM,
        /**
         * 上侧
         */
        TOP,
        /**
         * 除去左下 其余三个
         */
        EXCEPT_BOTTOM_LEFT,
        /**
         * 除去右下 其余三个
         */
        EXCEPT_BOTTOM_RIGHT,
    }


}

