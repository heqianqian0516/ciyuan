package com.ciyuanplus.mobile.module.settings.change_name;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class ChangeNamePresenterModule {
    private final ChangeNameContract.View mView;

    public ChangeNamePresenterModule(ChangeNameContract.View mView) {
        this.mView = mView;
    }

    @Provides
    ChangeNameContract.View providesChangeNameContractView() {
        return mView;
    }
}
