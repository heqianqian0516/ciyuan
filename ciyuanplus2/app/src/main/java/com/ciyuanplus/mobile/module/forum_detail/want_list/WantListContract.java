package com.ciyuanplus.mobile.module.forum_detail.want_list;

import android.content.Intent;

import com.ciyuanplus.mobile.module.BaseContract;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Alen on 2017/12/11.
 */

class WantListContract {
    interface Presenter extends BaseContract.Presenter{
        void initData(Intent intent);

        void getWantList();
    }

    interface View extends BaseContract.View{
        RecyclerView getGridView();

        void updateNullView(boolean visible);

        void setCenterTitle(String title);
    }
}
