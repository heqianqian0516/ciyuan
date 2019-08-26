package com.ciyuanplus.mobile.module.mine.collect_stuff;

import com.ciyuanplus.base.irecyclerview.IRecyclerView;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.module.BaseContract;

/**
 * Created by Alen on 2017/12/11.
 */

class CollectStuffContract {
    interface Presenter extends BaseContract.Presenter {
        void initData();// 初始化数据

        void handleEvent(EventCenterManager.EventMessage eventMessage);

        void requestStuffList(boolean reset);// 请求网络数据
    }

    interface View extends BaseContract.View {
        IRecyclerView getRecyclerView();

        void updateView(int size);// 更新界面

        void stopLoadMoreAndRefresh();
    }
}
