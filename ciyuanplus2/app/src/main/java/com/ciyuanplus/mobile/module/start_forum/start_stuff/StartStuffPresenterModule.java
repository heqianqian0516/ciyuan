package com.ciyuanplus.mobile.module.start_forum.start_stuff;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class StartStuffPresenterModule {
    private final StartStuffContract.View mView;

    public StartStuffPresenterModule(StartStuffContract.View mView) {
        this.mView = mView;
    }

    @Provides
    StartStuffContract.View providesStartStuffContractView(){
        return mView;
    }
}
