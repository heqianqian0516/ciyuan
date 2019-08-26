package com.ciyuanplus.mobile.module.mine.friends;

import android.content.Intent;

import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.module.BaseContract;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Alen on 2017/12/11.
 */

class MyFriendsContract {
    interface Presenter extends BaseContract.Presenter {
        void initData(Intent intent);//初始化数据

        void handleEvent(EventCenterManager.EventMessage eventMessage);

        void requestList(boolean reset);// 请求数据
    }

    interface View extends BaseContract.View {
        RecyclerView getListView();

        void stopRefreshAndLoadMore();

        void updateView(int size);
    }
}
