package com.ciyuanplus.mobile.module.settings.setting;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class SettingsPresenterModule {
    private final SettingsContract.View mView;

    public SettingsPresenterModule(SettingsContract.View mView) {
        this.mView = mView;
    }

    @Provides
    SettingsContract.View providesSettingsContractView() {
        return mView;
    }
}
