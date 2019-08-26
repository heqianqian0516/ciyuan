package com.ciyuanplus.mobile.module.popup.main_welfare_enter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.module.webview.JsWebViewActivity;
import com.ciyuanplus.mobile.net.bean.UserBombBoxInfoItem;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/11/17.
 * <p>
 * 主页 弹出  活动入口页面, 这个页面简单的不需要使用MVP
 */

public class MainWelfareEnterActivity extends MyBaseActivity {
    @BindView(R.id.m_main_welfare_bg_img)
    ImageView mMainWelfareBgImg;
    @BindView(R.id.m_main_welfare_close_btn)
    ImageView mMainWelfareCloseBtn;
    @BindView(R.id.m_main_welfare_receive_btn)
    ImageView mMainWelfareReceiveBtn;
    @BindView(R.id.m_main_welfare_title_text)
    TextView mMainWelfareTitleText;
    @BindView(R.id.m_main_welfare_content_text)
    TextView mMainWelfareContentText;
    @BindView(R.id.m_main_welfare_layout)
    RelativeLayout mMainWelfareLayout;

    private UserBombBoxInfoItem mBombBoxInfo;
    private int mWidth;
    private int mHeight;
    private final int[] mResourceDots = {R.mipmap.img_dot1, R.mipmap.img_dot2, R.mipmap.img_dot3, R.mipmap.img_dot4,
            R.mipmap.img_dot5, R.mipmap.img_dot6, R.mipmap.img_dot7, R.mipmap.img_dot8, R.mipmap.img_dot9,
            R.mipmap.img_dot10, R.mipmap.img_dot11};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main_welfare_enter);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        mBombBoxInfo = (UserBombBoxInfoItem) getIntent().getSerializableExtra(Constants.INTENT_BOMB_ITEM);
        if (mBombBoxInfo == null) return;
        this.initView();
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
        mWidth = Utils.getScreenWidth();
        mHeight = Utils.getScreenHeight();


        mMainWelfareTitleText.setText(mBombBoxInfo.topText);
        mMainWelfareContentText.setText(mBombBoxInfo.boxText);

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.startAnim();

    }

    private void startAnim() {
        for (int i = 0; i < 100; i++) {
            final ImageView image = new ImageView(this);
            float x = (float) (Math.random() * mWidth);
            float y = (float) (Math.random() * mHeight);
            image.setX(x);
            image.setY(y);
            image.setImageResource(mResourceDots[(int) (Math.random() * 11)]);
            mMainWelfareLayout.addView(image);
            TranslateAnimation animation = new TranslateAnimation(x, x, y, mHeight);
            animation.setDuration((long) (5000 - y * 5000 / mHeight));
            image.startAnimation(animation);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mMainWelfareLayout.removeView(image);
                    addFlashImage();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

    }

    private void addFlashImage() {
        final ImageView image = new ImageView(this);
        float x = (float) (Math.random() * mWidth);
        float y = -20f;
        image.setX(x);
        image.setY(y);
        image.setImageResource(mResourceDots[(int) (Math.random() * 11)]);
        mMainWelfareLayout.addView(image);
        TranslateAnimation animation = new TranslateAnimation(x, x, y, mHeight);
        animation.setDuration(5000);
        image.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mMainWelfareLayout.removeView(image);
                addFlashImage();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainWelfareLayout.clearAnimation();
        mMainWelfareLayout.removeAllViews();
    }

    @OnClick({R.id.m_main_welfare_close_btn, R.id.m_main_welfare_receive_btn})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {
            case R.id.m_main_welfare_close_btn:
                finish();

                break;
            case R.id.m_main_welfare_receive_btn:
                if (!LoginStateManager.isLogin()) {
                    Intent intent = new Intent(MainWelfareEnterActivity.this, LoginActivity.class);
                    MainWelfareEnterActivity.this.startActivity(intent);
                    return;
                }
                //跳转页面
                Intent intent = new Intent(MainWelfareEnterActivity.this, JsWebViewActivity.class);
                intent.putExtra(Constants.INTENT_OPEN_URL, mBombBoxInfo.buttonUrl);
                startActivity(intent);
                finish();
                break;
        }
    }
}
