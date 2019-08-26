package com.ciyuanplus.mobile.module.found.market_search;

import com.ciyuanplus.base.irecyclerview.IRecyclerView;
import com.ciyuanplus.mobile.module.BaseContract;

/**
 * Created by Alen on 2017/12/11.
 */

class MarketSearchContract {
    interface Presenter extends BaseContract.Presenter {
        void initData();// 初始化数据

        void requestStuffList(boolean b);// 请求数据


    }

    interface View extends BaseContract.View {
        void stopLoadMoreAndRefresh();

        void updateView(int size);

        IRecyclerView getRecyclerView();
    }
}
