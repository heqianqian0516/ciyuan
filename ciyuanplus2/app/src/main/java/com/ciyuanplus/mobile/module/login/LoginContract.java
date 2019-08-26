package com.ciyuanplus.mobile.module.login;

import com.ciyuanplus.mobile.module.BaseContract;


/**
 * Created by Alen on 2017/12/11.
 */

class LoginContract {
    interface Presenter extends BaseContract.Presenter {
        void requestLogin(String account, String password);


        void requestWeiChatLogin();// 请求三方登录


        void requestQQLogin();

        void requestWeiBoLogin();

        void sendCode(String mobile);
    }

    interface View extends BaseContract.View {
        void dismissDialog();

        void showLoadingDialog();

        void startCount();
    }
}
