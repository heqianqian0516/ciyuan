package com.ciyuanplus.mobile.module.video;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ciyuanplus.mobile.R;
import com.flyco.tablayout.SlidingTabLayout;
import com.kris.baselibrary.base.LazyLoadBaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends LazyLoadBaseFragment {
    @BindView(R.id.tl_video)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.vp_video)
    ViewPager mVideoViewPager;
    //Fragment集合
    private ArrayList<Fragment> mFagments = new ArrayList<>();
    //标题
    private String[] mTitles = {"推荐", "关注"};
    private MyPagerAdapter mAdapter;
    private Unbinder mUnbinder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        //初始化tab
        initTab();
        return view;
    }

    private void initTab() {
        //添加Fragment
        mFagments.add(new RecommendFragment());
        mFagments.add(new AttentionFragment());
        //new一个适配器
        mAdapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
        //设置ViewPager与适配器关联
        mVideoViewPager.setAdapter(mAdapter);
        //设置Tab与ViewPager关联
        mTabLayout.setViewPager(mVideoViewPager);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_video;
    }

    @Override
    public void lazyLoad() {
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFagments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFagments.get(position);
        }
    }

    @Override
    public void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }
}
