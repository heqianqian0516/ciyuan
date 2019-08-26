package com.ciyuanplus.mobile.module.mine.change_community;

import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.module.BaseContract;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Alen on 2017/12/11.
 */

class ChangeCommunityContract {
    interface Presenter extends BaseContract.Presenter {
        void initData();// 初始化数据

        void handleEvent(EventCenterManager.EventMessage eventMessage);// 处理
    }

    interface View extends BaseContract.View {
        RecyclerView getListView();
    }
}
