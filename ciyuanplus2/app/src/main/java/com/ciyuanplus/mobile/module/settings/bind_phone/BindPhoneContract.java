package com.ciyuanplus.mobile.module.settings.bind_phone;

import com.ciyuanplus.mobile.module.BaseContract;

/**
 * Created by Alen on 2017/12/11.
 */

class BindPhoneContract {
    interface Presenter extends BaseContract.Presenter {
        void sendCode(String mPhone);// 发送验证码

        void changePhone(String phone, String mCode); // 修改手机号
    }

    interface View extends BaseContract.View {
        void getResetSendCode(boolean isOnclick, int timeCount);

        void showSuccessMsg();
    }
}
