package com.ciyuanplus.mobile.module.register.forget_password;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/26.
 */
@Module
class ForgetPasswordPresenterModule {
    private final ForgetPasswordContract.View mView;

    public ForgetPasswordPresenterModule(ForgetPasswordContract.View mView) {
        this.mView = mView;
    }

    @Provides
    ForgetPasswordContract.View providesForgetPasswordContractView() {
        return mView;
    }
}
