package com.ciyuanplus.mobile.module.popup.daily_popup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.news.ShareDailyPopupActivity;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.module.forum_detail.daily_detail.DailyDetailActivity;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.RoundImageView;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/12/14.
 * <p>
 * 日签的弹出框
 */

public class DailyPopupActivity extends MyBaseActivity implements DailyPopupContract.View {
    @BindView(R.id.m_daily_pop_up_day)
    TextView mDailyPopUpDay;
    @BindView(R.id.m_daily_pop_up_week)
    TextView mDailyPopUpWeek;
    @BindView(R.id.m_daily_pop_up_month)
    TextView mDailyPopUpMonth;
    @BindView(R.id.m_daily_pop_up_weather)
    TextView mDailyPopUpWeather;
    @BindView(R.id.m_daily_pop_up_share)
    ImageView mDailyPopUpShare;
    @BindView(R.id.m_daily_pop_up_image)
    ImageView mDailyPopUpImage;
    @BindView(R.id.m_daily_pop_up_head)
    RoundImageView mDailyPopUpHead;
    @BindView(R.id.m_daily_pop_up_user_name)
    TextView mDailyPopUpUserName;
    @BindView(R.id.m_daily_pop_up_content)
    TextView mDailyPopUpContent;
    @BindView(R.id.m_daily_pop_up_close_btn)
    ImageView mDailyPopUpCloseBtn;
    @BindView(R.id.m_daily_pop_up_lp)
    RelativeLayout mDailyPopUpLp;

    @Inject

    DailyPopupPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_daily_pop_up);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();
        mPresenter.initData(getIntent());
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
        DaggerDailyPopupPresenterComponent.builder()
                .dailyPopupPresenterModule(new DailyPopupPresenterModule(this)).build().inject(this);

        mDailyPopUpDay.setText(mPresenter.mDailyLabelInfo.dayValue);
        mDailyPopUpWeek.setText(mPresenter.mDailyLabelInfo.weekValue);
        mDailyPopUpMonth.setText(mPresenter.mDailyLabelInfo.yearMonthValue);

        mDailyPopUpWeather.setText(mPresenter.mDailyLabelInfo.cityValue + "  " + mPresenter.mDailyLabelInfo.weatherValue);
        mDailyPopUpUserName.setText("摄影：" + mPresenter.mDailyLabelInfo.authorNickname);
        mDailyPopUpContent.setText(mPresenter.mDailyLabelInfo.content);

        RequestOptions ops = new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
                .dontAnimate().centerCrop();

        Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + mPresenter.mDailyLabelInfo.image)
                .apply(ops).into(mDailyPopUpImage);

        Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + mPresenter.mDailyLabelInfo.authorPhoto)
                .apply(ops).into(mDailyPopUpHead);

    }

    @Override
    public RelativeLayout getPopupLayout() {
        return mDailyPopUpLp;
    }

    @OnClick({R.id.m_daily_pop_up_share, R.id.m_daily_pop_up_close_btn, R.id.m_daily_pop_up_lp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.m_daily_pop_up_share:
                // 先生成截图，添加 水印
                String path = mPresenter.createShareImage();
                if (Utils.isStringEmpty(path)) return;
                Intent intent = new Intent(this, ShareDailyPopupActivity.class);
                intent.putExtra(Constants.INTENT_DAILY_LABEL, mPresenter.mDailyLabelInfo);
                intent.putExtra(Constants.INTENT_FILE_PATH, path);
                startActivity(intent);
                break;
            case R.id.m_daily_pop_up_close_btn:
                finish();
                break;
            case R.id.m_daily_pop_up_lp:
                Intent intent2 = new Intent(DailyPopupActivity.this, DailyDetailActivity.class);
                intent2.putExtra(Constants.INTENT_NEWS_ID_ITEM, mPresenter.mDailyLabelInfo.postUuid);
                startActivity(intent2);
                finish();
                break;
        }
    }


    @Override
    public Context getDefaultContext() {
        return this;
    }
}
