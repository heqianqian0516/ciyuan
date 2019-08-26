package com.ciyuanplus.mobile.module.settings.help;

import com.ciyuanplus.mobile.module.BaseContract;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Alen on 2017/12/11.
 */

class HelpContract {
    interface Presenter extends BaseContract.Presenter {
        void initData();// 初始化数据
    }

    interface View extends BaseContract.View {
        RecyclerView getListView();
    }
}
