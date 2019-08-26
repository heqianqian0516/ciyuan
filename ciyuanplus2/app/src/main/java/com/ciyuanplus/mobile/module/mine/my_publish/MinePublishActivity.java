package com.ciyuanplus.mobile.module.mine.my_publish;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ciyuanplus.mobile.MyFragmentActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/11/16.
 * 我的发布页面
 */

public class MinePublishActivity extends MyFragmentActivity implements MinePublishContract.View, ViewPager.OnPageChangeListener {
    @BindView(R.id.m_mine_publish_back_image)
    ImageView mMinePublishBackImage;
    @BindView(R.id.m_mine_publish_tab_post_title_lp)
    RelativeLayout mMinePublishTabPostTitleLp;
    @BindView(R.id.m_mine_publish_tab_stuff_title_lp)
    RelativeLayout mMinePublishTabStuffTitleLp;
    @BindView(R.id.m_mine_publish_pager)
    ViewPager mMinePublishPager;

    @Inject

    MinePublishPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_mine_publish);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();
        mPresenter.initData();

    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
        DaggerMinePublishPresenterComponent.builder()
                .minePublishPresenterModule(new MinePublishPresenterModule(this)).build().inject(this);

        mMinePublishPager.setOnPageChangeListener(this);
    }

    @Override
    public ViewPager getPager() {
        return mMinePublishPager;
    }

    @Override
    public void switchTabSelect(int mSelectedTab) {
        if (mSelectedTab == 0) {
            mMinePublishTabPostTitleLp.setSelected(true);
            mMinePublishTabStuffTitleLp.setSelected(false);
            mMinePublishPager.setCurrentItem(0);
        } else {
            mMinePublishTabPostTitleLp.setSelected(false);
            mMinePublishTabStuffTitleLp.setSelected(true);
            mMinePublishPager.setCurrentItem(1);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.m_mine_publish_back_image, R.id.m_mine_publish_tab_post_title_lp, R.id.m_mine_publish_tab_stuff_title_lp})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {
            case R.id.m_mine_publish_back_image:
                onBackPressed();
                break;
            case R.id.m_mine_publish_tab_post_title_lp:
                mPresenter.mSelectedTab = 0;
                switchTabSelect(0);
                break;
            case R.id.m_mine_publish_tab_stuff_title_lp:
                mPresenter.mSelectedTab = 1;
                switchTabSelect(1);
                break;
        }
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            mMinePublishTabPostTitleLp.setSelected(true);
            mMinePublishTabStuffTitleLp.setSelected(false);
        } else {
            mMinePublishTabPostTitleLp.setSelected(false);
            mMinePublishTabStuffTitleLp.setSelected(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
