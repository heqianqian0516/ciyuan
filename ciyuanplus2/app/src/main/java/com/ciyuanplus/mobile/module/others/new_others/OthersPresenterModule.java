package com.ciyuanplus.mobile.module.others.new_others;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class OthersPresenterModule {
    private final OthersContract.View mView;

    public OthersPresenterModule(OthersContract.View mView) {
        this.mView = mView;
    }

    @Provides
    OthersContract.View providesOthersContractView() {
        return mView;
    }
}
