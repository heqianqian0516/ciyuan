package com.ciyuanplus.base.irecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ciyuanplus.base.R;

/**
 * Created by aspsine on 16/3/14.
 */
public class ClassicRefreshHeaderView extends FrameLayout implements RefreshTrigger {
    private final int[] mIds = {R.id.m_animation_iv_image1, R.id.m_animation_iv_image2, R.id.m_animation_iv_image3,
            R.id.m_animation_iv_image4, R.id.m_animation_iv_image5};
    private final ImageView[] mAnimationIvImage = new ImageView[5];
    private final Animation[] mInAnim = new Animation[5];
    private final Animation[] mOutAnim = new Animation[5];
    private boolean rotated = false;

    public ClassicRefreshHeaderView(Context context) {
        this(context, null);
    }

    public ClassicRefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClassicRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = wm != null ? wm.getDefaultDisplay().getWidth() : 0;
        for (int i = 0; i < 5; i++) {
            mAnimationIvImage[i] = findViewById(mIds[i]);

            mInAnim[i] = new TranslateAnimation(-width / 2, 0, 0, 0);
            mInAnim[i].setDuration(200);
            mInAnim[i].setInterpolator(new DecelerateInterpolator());
            mInAnim[i].setRepeatCount(0);
            mInAnim[i].setFillAfter(true);
            mInAnim[i].setStartOffset(i * 50);

            mOutAnim[i] = new TranslateAnimation(0, width / 2, 0, 0);
            mOutAnim[i].setDuration(200);
            mOutAnim[i].setRepeatCount(0);
            mOutAnim[i].setInterpolator(new AccelerateInterpolator());
            mOutAnim[i].setFillAfter(false);
            mOutAnim[i].setStartOffset(i * 50);
        }
    }

    @Override
    public void onStart(boolean automatic, int headerHeight, int finalHeight) {
        int height = headerHeight;
    }

    @Override
    public void onMove(boolean isComplete, boolean automatic, int moved) {
        if (!rotated && moved >= 200) {
            rotated = true;
            for (int i = 0; i < 5; i++) {
                mAnimationIvImage[i].clearAnimation();
                mAnimationIvImage[i].startAnimation(mInAnim[i]);
            }
        }
    }

    @Override
    public void onRefresh() {
//        ivSuccess.setVisibility(GONE);
//        ivArrow.clearAnimation();
//        ivArrow.setVisibility(GONE);
//        progressBar.setVisibility(VISIBLE);
//        tvRefresh.setText("REFRESHING");
    }

    @Override
    public void onRelease() {
        for (int i = 0; i < 5; i++) {
            mAnimationIvImage[i].clearAnimation();
            mAnimationIvImage[i].startAnimation(mOutAnim[i]);
        }
    }

    @Override
    public void onComplete() {
        rotated = false;
//        ivSuccess.setVisibility(VISIBLE);
//        ivArrow.clearAnimation();
//        ivArrow.setVisibility(GONE);
//        progressBar.setVisibility(GONE);
//        tvRefresh.setText("COMPLETE");
    }

    @Override
    public void onReset() {
        rotated = false;
//        ivSuccess.setVisibility(GONE);
//        ivArrow.clearAnimation();
//        ivArrow.setVisibility(GONE);
//        progressBar.setVisibility(GONE);
    }
}
