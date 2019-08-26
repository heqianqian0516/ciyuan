package com.ciyuanplus.mobile.module.settings.bind_phone;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class BindPhonePresenterModule {
    private final BindPhoneContract.View mView;

    public BindPhonePresenterModule(BindPhoneContract.View mView) {
        this.mView = mView;
    }

    @Provides
    BindPhoneContract.View providesBindPhoneContractView() {
        return mView;
    }
}
