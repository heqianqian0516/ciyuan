package com.ciyuanplus.mobile.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;


/**
 * @auth
 * @date 2016-12-31
 */
public class XHeaderView extends LinearLayout {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;

    private final int ROTATE_ANIM_DURATION = 180;
    private ImageView mAnimationIvImage3;
    private ImageView mAnimationIvImage2;
    private ImageView mAnimationIvImage4;
    private ImageView mAnimationIvImage1;
    private ImageView mAnimationIvImage5;
    RelativeLayout mHeaderContent;

    private RelativeLayout mContainer;
    private Animation mInAnim1, mInAnim2, mInAnim3, mInAnim4, mInAnim5;
    private Animation mOutAnim1, mOutAnim2, mOutAnim3, mOutAnim4, mOutAnim5;
    private int mState = STATE_NORMAL;

    private boolean mIsFirst;

    public XHeaderView(Context context) {
        super(context);
        initView(context);
    }

    public XHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        // Initial set header view height 0
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        mContainer = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.vw_header, null);
        addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);

        mAnimationIvImage1 = mContainer.findViewById(R.id.m_animation_iv_image1);
        mAnimationIvImage2 = mContainer.findViewById(R.id.m_animation_iv_image2);
        mAnimationIvImage3 = mContainer.findViewById(R.id.m_animation_iv_image3);
        mAnimationIvImage4 = mContainer.findViewById(R.id.m_animation_iv_image4);
        mAnimationIvImage5 = mContainer.findViewById(R.id.m_animation_iv_image5);


        // 这里后期改成数组遍历的方式，  代码太丑了。。
        mInAnim1 = new TranslateAnimation(-Utils.getScreenWidth() / 2, 0, 0, 0);
        mInAnim1.setDuration(200);
        mInAnim1.setInterpolator(new DecelerateInterpolator());
        mInAnim1.setRepeatCount(0);
        mInAnim1.setFillAfter(true);
        mInAnim1.setStartOffset(0);
        mInAnim2 = new TranslateAnimation(-Utils.getScreenWidth() / 2, 0, 0, 0);
        mInAnim2.setDuration(200);
        mInAnim2.setInterpolator(new DecelerateInterpolator());
        mInAnim2.setRepeatCount(0);
        mInAnim2.setFillAfter(true);
        mInAnim2.setStartOffset(50);
        mInAnim3 = new TranslateAnimation(-Utils.getScreenWidth() / 2, 0, 0, 0);
        mInAnim3.setDuration(200);
        mInAnim3.setInterpolator(new DecelerateInterpolator());
        mInAnim3.setRepeatCount(0);
        mInAnim3.setFillAfter(true);
        mInAnim3.setStartOffset(100);
        mInAnim4 = new TranslateAnimation(-Utils.getScreenWidth() / 2, 0, 0, 0);
        mInAnim4.setDuration(200);
        mInAnim4.setInterpolator(new DecelerateInterpolator());
        mInAnim4.setRepeatCount(0);
        mInAnim4.setFillAfter(true);
        mInAnim4.setStartOffset(150);
        mInAnim5 = new TranslateAnimation(-Utils.getScreenWidth() / 2, 0, 0, 0);
        mInAnim5.setDuration(200);
        mInAnim5.setInterpolator(new DecelerateInterpolator());
        mInAnim5.setRepeatCount(0);
        mInAnim5.setFillAfter(true);
        mInAnim5.setStartOffset(200);

        mOutAnim1 = new TranslateAnimation(0, Utils.getScreenWidth() / 2, 0, 0);
        mOutAnim1.setDuration(200);
        mOutAnim1.setRepeatCount(0);
        mOutAnim1.setInterpolator(new AccelerateInterpolator());
        mOutAnim1.setFillAfter(false);
        mOutAnim1.setStartOffset(0);
        mOutAnim2 = new TranslateAnimation(0, Utils.getScreenWidth() / 2, 0, 0);
        mOutAnim2.setDuration(200);
        mOutAnim2.setRepeatCount(0);
        mOutAnim2.setInterpolator(new AccelerateInterpolator());
        mOutAnim2.setFillAfter(false);
        mOutAnim2.setStartOffset(50);
        mOutAnim3 = new TranslateAnimation(0, Utils.getScreenWidth() / 2, 0, 0);
        mOutAnim3.setDuration(200);
        mOutAnim3.setRepeatCount(0);
        mOutAnim3.setInterpolator(new AccelerateInterpolator());
        mOutAnim3.setFillAfter(false);
        mOutAnim3.setStartOffset(100);
        mOutAnim4 = new TranslateAnimation(0, Utils.getScreenWidth() / 2, 0, 0);
        mOutAnim4.setDuration(200);
        mOutAnim4.setRepeatCount(0);
        mOutAnim4.setInterpolator(new AccelerateInterpolator());
        mOutAnim4.setFillAfter(false);
        mOutAnim4.setStartOffset(150);
        mOutAnim5 = new TranslateAnimation(0, Utils.getScreenWidth() / 2, 0, 0);
        mOutAnim5.setDuration(200);
        mOutAnim5.setRepeatCount(0);
        mOutAnim5.setInterpolator(new AccelerateInterpolator());
        mOutAnim5.setFillAfter(false);
        mOutAnim5.setStartOffset(200);
        mOutAnim5.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibleHeight(0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public void setState(int state) {
        if (state == mState && mIsFirst) {
            return;
        }

//        Log.i("ssss", "state" + state);
        switch (state) {
            case STATE_NORMAL:
                if (mState == STATE_REFRESHING) {
                    mAnimationIvImage1.clearAnimation();
                    mAnimationIvImage1.startAnimation(mOutAnim1);
                    mAnimationIvImage2.clearAnimation();
                    mAnimationIvImage2.startAnimation(mOutAnim2);
                    mAnimationIvImage3.clearAnimation();
                    mAnimationIvImage3.startAnimation(mOutAnim3);
                    mAnimationIvImage4.clearAnimation();
                    mAnimationIvImage4.startAnimation(mOutAnim4);
                    mAnimationIvImage5.clearAnimation();
                    mAnimationIvImage5.startAnimation(mOutAnim5);
                    CommonToast.getInstance("刷新成功", Toast.LENGTH_SHORT).show();
                }
                break;

            case STATE_READY:
                if (mState == STATE_NORMAL) {
                    mAnimationIvImage1.clearAnimation();
                    mAnimationIvImage1.startAnimation(mInAnim1);
                    mAnimationIvImage2.clearAnimation();
                    mAnimationIvImage2.startAnimation(mInAnim2);
                    mAnimationIvImage3.clearAnimation();
                    mAnimationIvImage3.startAnimation(mInAnim3);
                    mAnimationIvImage4.clearAnimation();
                    mAnimationIvImage4.startAnimation(mInAnim4);
                    mAnimationIvImage5.clearAnimation();
                    mAnimationIvImage5.startAnimation(mInAnim5);
                }
//                if (mState != STATE_READY) {
//                    mPullDownImage.clearAnimation();
//                    mPullDownImage.startAnimation(mOutAnim);
//                    CommonToast.getInstance("刷新成功", Toast.LENGTH_SHORT).show();
//                }
                break;

            case STATE_REFRESHING:
                break;

            default:
                break;
        }

        mState = state;
    }

    /**
     * Get the header view visible height.
     *
     * @return
     */
    public int getVisibleHeight() {
        return mContainer.getHeight();
    }

    /**
     * Set the header view visible height.
     *
     * @param height
     */
    public void setVisibleHeight(int height) {
        if (height < 0) height = 0;
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

}
