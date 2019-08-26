package com.ciyuanplus.mobile.module.mine.my_publish;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class MinePublishPresenterModule {
    private final MinePublishContract.View mView;

    public MinePublishPresenterModule(MinePublishContract.View mView) {
        this.mView = mView;
    }

    @Provides
    MinePublishContract.View providesMinePublishContractView() {
        return mView;
    }
}
