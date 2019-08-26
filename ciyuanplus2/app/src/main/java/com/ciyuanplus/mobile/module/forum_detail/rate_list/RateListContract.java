package com.ciyuanplus.mobile.module.forum_detail.rate_list;

import android.content.Intent;

import com.ciyuanplus.base.irecyclerview.IRecyclerView;
import com.ciyuanplus.mobile.module.BaseContract;

/**
 * Created by Alen on 2017/12/11.
 */

class RateListContract {
    interface Presenter extends BaseContract.Presenter{
        void initData(Intent intent);

        void getRateList(boolean reset);
    }

    interface View extends BaseContract.View{
        IRecyclerView getGridView();

        void updateView();

        void updateMineCommentView();

        void stopRereshAndLoad();

        void setTitleString(String title);
    }
}
