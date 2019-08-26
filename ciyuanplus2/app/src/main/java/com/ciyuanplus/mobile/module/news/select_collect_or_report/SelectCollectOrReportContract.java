package com.ciyuanplus.mobile.module.news.select_collect_or_report;

import android.content.Intent;

import com.ciyuanplus.mobile.module.BaseContract;

/**
 * Created by Alen on 2017/12/11.
 */

class SelectCollectOrReportContract {
    interface Presenter extends BaseContract.Presenter {

        void initData(Intent intent);

        void doCollect();

        void goReportActivity();
    }

    interface View extends BaseContract.View {
        void updateCollectView(boolean isCollected);
    }
}
