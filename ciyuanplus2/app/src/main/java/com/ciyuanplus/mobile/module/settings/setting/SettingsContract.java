package com.ciyuanplus.mobile.module.settings.setting;

import android.widget.ListView;

import com.ciyuanplus.mobile.module.BaseContract;

/**
 * Created by Alen on 2017/12/11.
 */

class SettingsContract {
    interface Presenter extends BaseContract.Presenter {
        void initData(ListView mMySettingList);
    }

    interface View extends BaseContract.View {
    }
}
