package com.ciyuanplus.mobile.module.bind_phone;

import com.ciyuanplus.mobile.module.BaseContract;
import com.ciyuanplus.mobile.net.response.LoginResponse;

/**
 * Created by Alen on 2017/12/11.
 */

class LoginBindPhoneContract {
    interface Presenter extends BaseContract.Presenter {
        void sendCode(String mPhone);// 发送验证码

        void changePhone(String phone, String mCode); // 修改手机号
    }

    interface View extends BaseContract.View {
        void getResetSendCode(boolean isOnclick, int timeCount);

        void showSuccessMsg();

        void jumpToMain(LoginResponse response);
    }
}
