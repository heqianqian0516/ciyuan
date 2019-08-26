package com.ciyuanplus.mobile.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ciyuanplus.mobile.R;

/**
 * @author markmjw
 * @date 2013-10-08
 */
public class XFooterView extends LinearLayout {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_LOADING = 2;

    private ProgressBar mFooterProgressbar;
    private TextView mFooterHintText;

    private View mLayout;

//    private ImageView mHintImage;

    private int mState = STATE_NORMAL;

    public XFooterView(Context context) {
        super(context);
        initView(context);
    }

    public XFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mLayout = LayoutInflater.from(context).inflate(R.layout.vw_footer, null);
        mLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        addView(mLayout);

        mFooterProgressbar = mLayout.findViewById(R.id.m_footer_progressbar);
        mFooterHintText = mLayout.findViewById(R.id.m_footer_hint_text);

//        mHintImage = (ImageView) mLayout.findViewById(R.id.footer_arrow);

        Animation rotateUpAnim = new RotateAnimation(0.0f, 180.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        int ROTATE_ANIM_DURATION = 180;
        rotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        rotateUpAnim.setFillAfter(true);

        Animation rotateDownAnim = new RotateAnimation(180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        rotateDownAnim.setFillAfter(true);
    }

    /**
     * Set footer view state
     *
     * @param state
     * @see #STATE_LOADING
     * @see #STATE_NORMAL
     * @see #STATE_READY
     */
    public void setState(int state) {
        if (state == mState) return;

        if (state == STATE_LOADING) {
//            mHintImage.clearAnimation();
//            mHintImage.setVisibility(View.INVISIBLE);
            mFooterProgressbar.setVisibility(View.VISIBLE);
            mFooterHintText.setVisibility(View.INVISIBLE);
        } else {
            //mFooterHintText.setVisibility(View.VISIBLE);
//            mHintImage.setVisibility(View.VISIBLE);
            mFooterProgressbar.setVisibility(View.INVISIBLE);
        }

        switch (state) {
            case STATE_NORMAL:
//                if (mState == STATE_READY) {
//                    mHintImage.startAnimation(mRotateDownAnim);
//                }
//                if (mState == STATE_LOADING) {
//                    mHintImage.clearAnimation();
//                }
                mFooterHintText.setText("");
                break;

            case STATE_READY:
                if (mState != STATE_READY) {
//                    mHintImage.clearAnimation();
//                    mHintImage.startAnimation(mRotateUpAnim);
                    mFooterHintText.setText(R.string.footer_hint_load_ready);
                }
                break;

            case STATE_LOADING:
                break;
        }

        mState = state;
    }

    /**
     * Get footer view bottom margin.
     *
     * @return
     */
    public int getBottomMargin() {
        LayoutParams lp = (LayoutParams) mLayout.getLayoutParams();
        return lp.bottomMargin;
    }

    /**
     * Set footer view bottom margin.
     *
     * @param margin
     */
    public void setBottomMargin(int margin) {
        if (margin < 0) return;
        LayoutParams lp = (LayoutParams) mLayout.getLayoutParams();
        lp.bottomMargin = margin;
        mLayout.setLayoutParams(lp);
    }

    /**
     * normal status
     */
    public void normal() {
        //mFooterHintText.setVisibility(View.VISIBLE);
        mFooterProgressbar.setVisibility(View.GONE);
    }

    /**
     * loading status
     */
    public void loading() {
        mFooterHintText.setVisibility(View.GONE);
        mFooterProgressbar.setVisibility(View.VISIBLE);
    }

    /**
     * hide footer when disable pull load more
     */
    public void hide() {
        LayoutParams lp = (LayoutParams) mLayout.getLayoutParams();
        lp.height = 0;
        mLayout.setLayoutParams(lp);
    }

    /**
     * show footer
     */
    public void show() {
        LayoutParams lp = (LayoutParams) mLayout.getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        mLayout.setLayoutParams(lp);
    }

}
