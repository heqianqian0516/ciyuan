package com.ciyuanplus.mobile.module.register.register;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class RegisterPresenterModule {
    private final RegisterContract.View mView;

    public RegisterPresenterModule(RegisterContract.View mView) {
        this.mView = mView;
    }

    @Provides
    RegisterContract.View providesRegisterContractView() {
        return mView;
    }
}
