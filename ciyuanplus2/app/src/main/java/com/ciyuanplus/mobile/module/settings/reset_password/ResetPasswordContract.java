package com.ciyuanplus.mobile.module.settings.reset_password;

import com.ciyuanplus.mobile.module.BaseContract;

/**
 * Created by Alen on 2017/12/11.
 */

class ResetPasswordContract {
    interface Presenter extends BaseContract.Presenter {
        void changePassword(String mOldPassword, String mNewPassword);
    }

    interface View extends BaseContract.View {
    }
}
