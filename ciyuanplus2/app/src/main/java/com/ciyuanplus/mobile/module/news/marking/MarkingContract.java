package com.ciyuanplus.mobile.module.news.marking;

import android.content.Intent;

import com.ciyuanplus.mobile.module.BaseContract;

/**
 * Created by Alen on 2017/12/11.
 */

class MarkingContract {
    interface Presenter extends BaseContract.Presenter {
        void initData(Intent intent);

        void submmitMark(String content, String score);

    }

    interface View extends BaseContract.View {
    }
}
