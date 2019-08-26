package com.ciyuanplus.mobile.module.login;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/26.
 */
@Module
class LoginPresenterModule {
    private final LoginContract.View mView;

    public LoginPresenterModule(LoginContract.View mView) {
        this.mView = mView;
    }

    @Provides
    LoginContract.View providesLoginContractView() {
        return mView;
    }
}
