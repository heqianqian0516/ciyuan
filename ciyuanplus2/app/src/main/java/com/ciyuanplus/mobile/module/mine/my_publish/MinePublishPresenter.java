package com.ciyuanplus.mobile.module.mine.my_publish;

import com.ciyuanplus.mobile.MyFragmentActivity;
import com.ciyuanplus.mobile.module.mine.my_publish.MineNews.MinePostFragment;
import com.ciyuanplus.mobile.module.mine.my_publish.MineStuff.MineStuffFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by Alen on 2017/12/11.
 */

public class MinePublishPresenter implements MinePublishContract.Presenter {
    public int mSelectedTab = 0;
    private MinePostFragment mPostFragment;
    private MineStuffFragment mStuffFragment;
    private final List<Fragment> mFragments = new ArrayList<>();
    private final MinePublishContract.View mView;

    @Inject
    public MinePublishPresenter(MinePublishContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void initData() {
        if (mPostFragment == null) {
            mPostFragment = new MinePostFragment();
            mFragments.add(mPostFragment);
        }
        if (mStuffFragment == null) {
            mStuffFragment = new MineStuffFragment();
            mFragments.add(mStuffFragment);
        }
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(((MyFragmentActivity) mView).getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mFragments.get(arg0);
            }
        };
        mView.getPager().setAdapter(adapter);
        mView.switchTabSelect(0);
    }

    @Override
    public void detachView() {
    }
}
