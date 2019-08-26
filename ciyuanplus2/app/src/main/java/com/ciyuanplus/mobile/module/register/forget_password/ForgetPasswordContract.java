package com.ciyuanplus.mobile.module.register.forget_password;


import com.ciyuanplus.mobile.module.BaseContract;

/**
 * Created by Alen on 2017/12/26.
 */

class ForgetPasswordContract {
    interface Presenter extends BaseContract.Presenter {
        void forgetPassword(String phone, String password, String verify);

        void sendCode(String phone);
    }

    interface View extends BaseContract.View {
        void setVerifyTextState(String tt, boolean clickable);

        void closeCurrentActivity();
    }
}
