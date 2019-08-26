package com.ciyuanplus.mobile.module.settings.reset_password;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class ResetPasswordPresenterModule {
    private final ResetPasswordContract.View mView;

    public ResetPasswordPresenterModule(ResetPasswordContract.View mView) {
        this.mView = mView;
    }

    @Provides
    ResetPasswordContract.View providesResetPasswordContractView() {
        return mView;
    }
}
