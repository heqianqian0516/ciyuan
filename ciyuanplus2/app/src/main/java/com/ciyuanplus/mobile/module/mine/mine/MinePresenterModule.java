package com.ciyuanplus.mobile.module.mine.mine;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class MinePresenterModule {
    private final MineContract.View mView;

    public MinePresenterModule(MineContract.View mView) {
        this.mView = mView;
    }

    @Provides
    MineContract.View providesMineContractView() {
        return mView;
    }
}
