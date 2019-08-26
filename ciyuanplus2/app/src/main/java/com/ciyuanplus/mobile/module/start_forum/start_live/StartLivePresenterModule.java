package com.ciyuanplus.mobile.module.start_forum.start_live;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class StartLivePresenterModule {
    private final StartLiveContract.View mView;

    public StartLivePresenterModule(StartLiveContract.View mView) {
        this.mView = mView;
    }

    @Provides
    StartLiveContract.View providesStartLiveContractView(){
        return mView;
    }
}
