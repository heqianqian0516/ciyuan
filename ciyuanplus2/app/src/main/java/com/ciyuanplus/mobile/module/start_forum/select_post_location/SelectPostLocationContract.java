package com.ciyuanplus.mobile.module.start_forum.select_post_location;

import android.content.Intent;

import com.ciyuanplus.base.irecyclerview.IRecyclerView;
import com.ciyuanplus.mobile.module.BaseContract;

/**
 * Created by Alen on 2017/12/11.
 */

class SelectPostLocationContract {
    interface Presenter extends BaseContract.Presenter{
        void doSearchQuery(String s);

        void initData(Intent intent);
    }

    interface View extends BaseContract.View{
        IRecyclerView getListView();
    }
}
