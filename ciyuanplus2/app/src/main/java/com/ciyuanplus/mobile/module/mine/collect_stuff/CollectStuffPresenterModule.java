package com.ciyuanplus.mobile.module.mine.collect_stuff;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class CollectStuffPresenterModule {
    private final CollectStuffContract.View mView;

    public CollectStuffPresenterModule(CollectStuffContract.View mView) {
        this.mView = mView;
    }

    @Provides
    CollectStuffContract.View providesCollectStuffContractView() {
        return mView;
    }
}
