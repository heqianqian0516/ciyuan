package com.ciyuanplus.mobile.module.settings.message_setting;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class MessageSettingPresenterModule {
    private final MessageSettingContract.View mView;

    public MessageSettingPresenterModule(MessageSettingContract.View mView) {
        this.mView = mView;
    }

    @Provides
    MessageSettingContract.View providesMessageSettingContractView() {
        return mView;
    }
}
