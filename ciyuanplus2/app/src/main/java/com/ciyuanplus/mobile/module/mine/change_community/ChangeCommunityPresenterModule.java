package com.ciyuanplus.mobile.module.mine.change_community;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class ChangeCommunityPresenterModule {
    private final ChangeCommunityContract.View mView;

    public ChangeCommunityPresenterModule(ChangeCommunityContract.View mView) {
        this.mView = mView;
    }

    @Provides
    ChangeCommunityContract.View providesChangeCommunityContractView() {
        return mView;
    }
}
