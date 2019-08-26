package com.ciyuanplus.mobile.module.settings.about;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class AboutPresenterModule {
    private final AboutContract.View mView;

    public AboutPresenterModule(AboutContract.View mView) {
        this.mView = mView;
    }

    @Provides
    AboutContract.View providesAboutContractView() {
        return mView;
    }
}
