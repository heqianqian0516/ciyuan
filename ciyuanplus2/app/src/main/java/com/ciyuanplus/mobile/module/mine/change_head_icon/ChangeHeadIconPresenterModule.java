package com.ciyuanplus.mobile.module.mine.change_head_icon;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class ChangeHeadIconPresenterModule {
    private final ChangeHeadIconContract.View mView;

    public ChangeHeadIconPresenterModule(ChangeHeadIconContract.View mView) {
        this.mView = mView;
    }

    @Provides
    ChangeHeadIconContract.View providesChangeHeadIconContractView() {
        return mView;
    }
}
