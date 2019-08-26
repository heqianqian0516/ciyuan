package com.ciyuanplus.mobile.module.mine.search_friends;

import android.content.Intent;

import com.ciyuanplus.base.irecyclerview.IRecyclerView;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.module.BaseContract;

/**
 * Created by Alen on 2017/12/11.
 */

class SearchFriendsContract {
    interface Presenter extends BaseContract.Presenter {
        void initData(Intent intent);

        void requestList(boolean reset);

        void handleEvent(EventCenterManager.EventMessage eventMessage);

        void doSearch(String name);
    }

    interface View extends BaseContract.View {
        void updateView(int size);//

        IRecyclerView getListView();
    }
}
