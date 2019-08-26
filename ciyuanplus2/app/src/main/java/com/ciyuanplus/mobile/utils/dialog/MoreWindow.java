package com.ciyuanplus.mobile.utils.dialog;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.module.start_forum.start_food.StartFoodActivity;
import com.ciyuanplus.mobile.module.start_forum.start_note.StartNoteActivity;
import com.ciyuanplus.mobile.module.start_forum.start_seek_help.StartSeekHelpActivity;
import com.ciyuanplus.mobile.module.start_forum.start_stuff.StartStuffActivity;

public class MoreWindow extends PopupWindow implements OnClickListener {

    private final Activity mContext;
    private final String TAG = MoreWindow.class.getSimpleName();
    private int statusBarHeight;
    private Bitmap mBitmap = null;
    private Bitmap overlay = null;

    private final Handler mHandler = new Handler();

    public MoreWindow(Activity context) {
        mContext = context;
    }

    public void init() {
        Rect frame = new Rect();
        mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;
        DisplayMetrics metrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //因为某些机型是虚拟按键的,所以要加上以下设置防止挡住按键.
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private Bitmap blur() {
        if (null != overlay) {
            return overlay;
        }
        long startMs = System.currentTimeMillis();

        View view = mContext.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        mBitmap = view.getDrawingCache();

        float scaleFactor = 8;//图片缩放比例；
        float radius = 10;//模糊程度
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        overlay = Bitmap.createBitmap((int) (width / scaleFactor), (int) (height / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(mBitmap, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int) radius, true);
        Log.i(TAG, "blur time is:" + (System.currentTimeMillis() - startMs));
        return overlay;
    }

    private Animation showAnimation1(final View view, int fromY, int toY) {
        AnimationSet set = new AnimationSet(true);
        TranslateAnimation go = new TranslateAnimation(0, 0, fromY, toY);
        go.setDuration(300);
        TranslateAnimation go1 = new TranslateAnimation(0, 0, -10, 2);
        go1.setDuration(100);
        go1.setStartOffset(250);
        set.addAnimation(go1);
        set.addAnimation(go);

        set.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationStart(Animation animation) {

            }

        });
        return set;
    }


    public void showMoreWindow(View anchor, int bottomMargin) {
        final RelativeLayout layout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.center_music_more_window, null);
        layout.getBackground().mutate().setAlpha(240);
        setContentView(layout);

        ImageView close = layout.findViewById(R.id.iv_close);
        setTextImage(R.mipmap.launch_btn_qiuzhu2x, R.id.tv_seek_help, layout);
        setTextImage(R.mipmap.launch_btn_tuijian2x, R.id.tv_recommend, layout);
        setTextImage(R.mipmap.launch_btn_zatan2x, R.id.tv_talk, layout);
        setTextImage(R.mipmap.launch_btn_maimai2x, R.id.tv_business, layout);
        close.setOnClickListener(v -> {
            if (isShowing()) {
                closeAnimation(layout);
            }
        });

        showAnimation(layout);
        //setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), blur()));
        setOutsideTouchable(true);
        setFocusable(true);
        showAtLocation(anchor, Gravity.BOTTOM, 0, statusBarHeight);
    }

    private void setTextImage(int imgId, int resID, View layoutID) {
        TextView textView = layoutID.findViewById(resID);
        Drawable drawable1 = mContext.getResources().getDrawable(imgId);
        drawable1.setBounds(0, 0, 170, 170);
        textView.setCompoundDrawables(null, drawable1, null, null);
    }

    private void showAnimation(ViewGroup layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            final View child = layout.getChildAt(i);
            if (child.getId() == R.id.iv_close || child.getId() == R.id.iv_title) {
                continue;
            }

            child.setOnClickListener(this);
            child.setVisibility(View.INVISIBLE);
            mHandler.postDelayed(() -> {
                child.setVisibility(View.VISIBLE);
                ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 600, 0);
                fadeAnim.setDuration(300);
                KickBackAnimator kickAnimator = new KickBackAnimator();
                kickAnimator.setDuration(150);
                fadeAnim.setEvaluator(kickAnimator);
                fadeAnim.start();
            }, i * 50);

        }

    }

    private void closeAnimation(ViewGroup layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            final View child = layout.getChildAt(i);
            if (child.getId() == R.id.iv_close || child.getId() == R.id.iv_title) {
                continue;
            }
            child.setOnClickListener(this);
            mHandler.postDelayed(() -> {
                child.setVisibility(View.VISIBLE);
                ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 0, 600);
                fadeAnim.setDuration(200);
                KickBackAnimator kickAnimator = new KickBackAnimator();
                kickAnimator.setDuration(100);
                fadeAnim.setEvaluator(kickAnimator);
                fadeAnim.start();
                fadeAnim.addListener(new AnimatorListener() {

                    @Override
                    public void onAnimationStart(Animator animation) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        child.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        // TODO Auto-generated method stub

                    }
                });
            }, (layout.getChildCount() - i - 1) * 30);

            if (child.getId() == R.id.tv_seek_help) {
                mHandler.postDelayed(() -> dismiss(), (layout.getChildCount() - i) * 30 + 80);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_seek_help:
                Intent intent = new Intent(mContext, StartSeekHelpActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.tv_recommend:
                Intent intent2 = new Intent(mContext, StartFoodActivity.class);
                mContext.startActivity(intent2);
                break;
            case R.id.tv_talk:
                Intent intent3 = new Intent(mContext, StartNoteActivity.class);
                intent3.putExtra("LWV", "talk");
                mContext.startActivity(intent3);
                break;
            case R.id.tv_business:
                Intent intent4 = new Intent(mContext, StartStuffActivity.class);
                mContext.startActivity(intent4);
                break;
            default:
                break;


        }
        News();
    }


    public void destroy() {
        if (null != overlay) {
            overlay.recycle();
            overlay = null;
            System.gc();
        }
        if (null != mBitmap) {
            mBitmap.recycle();
            mBitmap = null;
            System.gc();
        }
    }

    private void News() {

        mHandler.postDelayed(() -> MoreWindow.this.dismiss(), 2000);
    }
}
