package com.ciyuanplus.mobile.module.bind_phone;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class LoginPhonePresenterModule {
    private final LoginBindPhoneContract.View mView;

    public LoginPhonePresenterModule(LoginBindPhoneContract.View mView) {
        this.mView = mView;
    }

    @Provides
    LoginBindPhoneContract.View providesBindPhoneContractView() {
        return mView;
    }
}
