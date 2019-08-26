package com.ciyuanplus.mobile.module.settings.change_name;

import com.ciyuanplus.mobile.module.BaseContract;

/**
 * Created by Alen on 2017/12/11.
 */

class ChangeNameContract {
    interface Presenter extends BaseContract.Presenter {
        void changeName(String name);

        void checkUserNickName(String name);
    }

    interface View extends BaseContract.View {
    }
}
