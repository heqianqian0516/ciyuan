package com.ciyuanplus.mobile.module.settings.help;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class HelpPresenterModule {
    private final HelpContract.View mView;

    public HelpPresenterModule(HelpContract.View mView) {
        this.mView = mView;
    }

    @Provides
    HelpContract.View providesHelpContractView() {
        return mView;
    }
}
