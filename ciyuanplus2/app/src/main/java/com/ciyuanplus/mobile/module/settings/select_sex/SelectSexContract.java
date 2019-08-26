package com.ciyuanplus.mobile.module.settings.select_sex;

import com.ciyuanplus.mobile.module.BaseContract;

/**
 * Created by Alen on 2017/12/11.
 */

class SelectSexContract {
    interface Presenter extends BaseContract.Presenter {
        void changeSex(int sex);
    }

    interface View extends BaseContract.View {
    }
}
