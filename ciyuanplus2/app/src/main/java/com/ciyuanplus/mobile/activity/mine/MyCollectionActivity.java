package com.ciyuanplus.mobile.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciyuanplus.mobile.MyFragmentActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.fragement.CollectPostFragment;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.module.mine.collect_stuff.CollectStuffFragment;
import com.ciyuanplus.mobile.statistics.StatisticsConstant;
import com.ciyuanplus.mobile.statistics.StatisticsManager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/6/24.
 * 我的收藏
 */

public class MyCollectionActivity extends MyFragmentActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.m_my_collection_back_image)
    ImageView mMyCollectionBackImage;
    @BindView(R.id.m_my_collection_tab_post_title)
    TextView mMyCollectionTabPostTitle;
    @BindView(R.id.m_my_collection_tab_post_title_lp)
    RelativeLayout mMyCollectionTabPostTitleLp;
    @BindView(R.id.m_my_collection_tab_stuff_title)
    TextView mMyCollectionTabStuffTitle;
    @BindView(R.id.m_my_collection_tab_stuff_title_lp)
    RelativeLayout mMyCollectionTabStuffTitleLp;
    @BindView(R.id.m_my_collection_tab_title_lp)
    LinearLayout mMyCollectionTabTitleLp;
    @BindView(R.id.m_my_collection_pager)
    ViewPager mMyCollectionPager;
    private CollectPostFragment mPostFragment;
    private CollectStuffFragment mStuffFragment;
    private final List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_my_collection);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);

        this.initData();
    }

    private void initData() {
        if (mPostFragment == null) {
            mPostFragment = new CollectPostFragment();
            mFragments.add(mPostFragment);
        }
        if (mStuffFragment == null) {
            mStuffFragment = new CollectStuffFragment();
            mFragments.add(mStuffFragment);
        }
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(this.getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mFragments.get(arg0);
            }
        };
        mMyCollectionPager.setAdapter(adapter);
        mMyCollectionPager.setOnPageChangeListener(this);
        switchTabSelect(0);

    }

    /**
     * 切换tab
     */
    private void switchTabSelect(int type) {
        if (type == 0) {
            if (mMyCollectionTabPostTitleLp.isSelected()) return;
            mMyCollectionTabPostTitleLp.setSelected(true);
            mMyCollectionTabStuffTitleLp.setSelected(false);
            mMyCollectionPager.setCurrentItem(0);
            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_MINE_LIKE, StatisticsConstant.OP_MINE_LIKE_POST_PAGE_LOAD);

        } else {
            if (mMyCollectionTabStuffTitleLp.isSelected()) return;
            mMyCollectionTabStuffTitleLp.setSelected(true);
            mMyCollectionTabPostTitleLp.setSelected(false);
            mMyCollectionPager.setCurrentItem(1);
            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_MINE_LIKE, StatisticsConstant.OP_MINE_LIKE_STUFF_PAGE_LOAD);

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mMyCollectionTabPostTitleLp.setSelected(true);
                mMyCollectionTabStuffTitleLp.setSelected(false);
                break;
            case 1:
                mMyCollectionTabPostTitleLp.setSelected(false);
                mMyCollectionTabStuffTitleLp.setSelected(true);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @OnClick({R.id.m_my_collection_back_image, R.id.m_my_collection_tab_post_title_lp, R.id.m_my_collection_tab_stuff_title_lp})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {
            case R.id.m_my_collection_back_image:
                onBackPressed();
                break;
            case R.id.m_my_collection_tab_post_title_lp:
                switchTabSelect(0);
                break;
            case R.id.m_my_collection_tab_stuff_title_lp:
                switchTabSelect(1);
                break;
        }
    }
}
