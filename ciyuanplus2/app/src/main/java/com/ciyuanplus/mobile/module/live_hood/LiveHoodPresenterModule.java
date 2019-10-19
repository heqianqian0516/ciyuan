package com.ciyuanplus.mobile.module.live_hood;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class LiveHoodPresenterModule {
    private final LiveHoodContract.View mView;

    public LiveHoodPresenterModule(LiveHoodContract.View mView) {
        this.mView = mView;
    }

    @Provides
    LiveHoodContract.View providesLiveHoodContractView() {
            return mView;
    }
}
