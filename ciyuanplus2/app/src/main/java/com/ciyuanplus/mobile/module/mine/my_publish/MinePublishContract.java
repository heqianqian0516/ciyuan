package com.ciyuanplus.mobile.module.mine.my_publish;

import com.ciyuanplus.mobile.module.BaseContract;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by Alen on 2017/12/11.
 */

class MinePublishContract {
    interface Presenter extends BaseContract.Presenter {
        void initData();// 初始化数据
    }

    interface View extends BaseContract.View {

        ViewPager getPager();

        void switchTabSelect(int i);
    }
}
