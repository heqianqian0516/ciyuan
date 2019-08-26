package com.ciyuanplus.mobile.module.start_forum.start_option;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class StartOptionPresenterModule {
    private final StartOptionContract.View mView;

    public StartOptionPresenterModule(StartOptionContract.View mView) {
        this.mView = mView;
    }

    @Provides
    StartOptionContract.View providesStartOptionContractView(){
        return mView;
    }
}
