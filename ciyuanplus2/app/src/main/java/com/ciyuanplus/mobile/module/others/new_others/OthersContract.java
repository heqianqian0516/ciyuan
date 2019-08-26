package com.ciyuanplus.mobile.module.others.new_others;

import android.content.Intent;

import com.ciyuanplus.mobile.inter.LoadMoreStatusInterface;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.module.BaseContract;
import com.ciyuanplus.mobile.net.response.SocialCountItem;
import com.ciyuanplus.mobile.widget.ViewPagerForScrollView;
import com.flyco.tablayout.SlidingTabLayout;

/**
 * Created by Alen on 2017/12/11.
 */

class OthersContract {
    interface Presenter extends BaseContract.Presenter {
        void initData(Intent intent);// 初始化数据

        void handleEvent(EventCenterManager.EventMessage eventMessage);

        void requestList(boolean reset);// 请求数据

        void handleClick(int id);
    }

    interface View extends BaseContract.View, LoadMoreStatusInterface {
        void updateView();

        void stopLoadMoreAndRefresh();

        void switchTabSelect(int i);
        void updateInfo(SocialCountItem item);

        @Override
        void onFinishLoadMore(boolean enable);

        @Override
        void onLoadMoreError();

        void setLoadMoreEnable(boolean enable);
    }
}
