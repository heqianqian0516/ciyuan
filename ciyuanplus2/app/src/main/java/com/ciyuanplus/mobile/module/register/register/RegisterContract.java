package com.ciyuanplus.mobile.module.register.register;

import android.content.Intent;

import com.ciyuanplus.mobile.module.BaseContract;

/**
 * Created by Alen on 2017/12/11.
 */

class RegisterContract {
    interface Presenter extends BaseContract.Presenter {
        void sendCode(String accout);

        void requestRegister(String accout1, String mPassword, String mVerify);

        void setNickName(String mNickName);

        void setHeadIconUrl(String mHeadIconUrl);

        void uploadImageFile(String mHeadIconPath);// 上传图片文件

        void initData(Intent intent);
    }

    interface View extends BaseContract.View {
        void setVerifyText(String string, boolean b);

        void dismissLoadingDialog();

        void showLoadingDialog();

        void changeHeadIcon(String mHeadIconUrl);

        void changeName(String mNickName);
    }
}
