package com.ciyuanplus.mobile.module.forum_detail.rate_list;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class RateListPresenterModule {
    private final RateListContract.View mView;

    public RateListPresenterModule(RateListContract.View mView) {
        this.mView = mView;
    }

    @Provides
    RateListContract.View providesRateListContractView(){
        return mView;
    }
}
