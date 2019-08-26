package com.ciyuanplus.mobile.module.found.market;

import com.ciyuanplus.base.irecyclerview.IRecyclerView;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.module.BaseContract;

/**
 * Created by Alen on 2017/12/11.
 */

class MarketContract {
    interface Presenter extends BaseContract.Presenter {
        void initData();// 数据初始化

        void requestStuffList(boolean b);// 请求数据

        void handleEvent(EventCenterManager.EventMessage eventMessage);
    }

    interface View extends BaseContract.View {
        IRecyclerView getRecyclerView();// 获取RecyclerView

        void stopLoadMoreAndRefresh();// 停止加载和刷新动画

        void updateView(int size);// 更新无数据 显示
    }
}
