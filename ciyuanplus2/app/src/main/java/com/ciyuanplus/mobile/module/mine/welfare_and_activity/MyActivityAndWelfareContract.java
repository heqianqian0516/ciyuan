package com.ciyuanplus.mobile.module.mine.welfare_and_activity;

import com.ciyuanplus.mobile.module.BaseContract;
import com.ciyuanplus.mobile.pulltorefresh.XRecyclerView;

/**
 * Created by kk on 2018/5/30.
 */

class MyActivityAndWelfareContract implements BaseContract {

    interface View extends BaseContract.View {

        XRecyclerView getMainList();

        void updatePage(boolean haveData);
    }

    interface Presenter extends BaseContract.Presenter {

    }
}
