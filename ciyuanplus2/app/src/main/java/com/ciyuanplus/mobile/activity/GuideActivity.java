package com.ciyuanplus.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.widget.IndicatorView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 第一次打开APP 的引导页面
 */

public class GuideActivity extends MyBaseActivity {
    @BindView(R.id.m_guide_viewPager)
    ViewPager mGuideViewPager;
    @BindView(R.id.m_guide_indicator)
    IndicatorView mGuideIndicator;
    @BindView(R.id.m_guide_skip_bt)
    ImageView mGuideSkipBt;

    private ArrayList<ImageView> mList;
    private MyViewPagerAdapter mAdapter;
    private final int[] images = new int[]{R.mipmap.guide_one, R.mipmap.guide_two, R.mipmap.guide_three};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_guide);

        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();
        this.initData();
    }

    private void initView() {
        ButterKnife.bind(this);

        mGuideViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if (position == mList.size() - 1) {

                    mList.get(position).setOnClickListener(v -> {
                        Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
                        GuideActivity.this.startActivity(intent);
                        finish();
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initData() {
        mList = new ArrayList<>();
        initArrayList();
        mAdapter = new MyViewPagerAdapter();
        mGuideViewPager.setAdapter(mAdapter);
        mGuideIndicator.setState(mList.size(), 0);
    }

    private void initArrayList() {
        LinearLayout.LayoutParams mParams
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                , LinearLayout.LayoutParams.MATCH_PARENT);

        for (int i = 0; i < images.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(mParams);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            mList.add(i, imageView);

        }
    }

    @Override
    protected void onDestroy() {
        mGuideViewPager = null;
        mAdapter = null;
        mList = null;
        super.onDestroy();
    }


    @Override
    @OnClick(R.id.m_guide_skip_bt)
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        int id = view.getId();
        if (id == R.id.m_guide_skip_bt) {
            Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
            GuideActivity.this.startActivity(intent);
            finish();
        }
    }

    private class MyViewPagerAdapter extends PagerAdapter {

        MyViewPagerAdapter() {

        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(View container, int position) {
            mList.get(position).setImageResource(images[position]);
            ((ViewPager) container).addView(mList.get(position));
            return mList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            mList.get(position).setImageResource(0);
            container.removeView(mList.get(position));
        }
    }
}
