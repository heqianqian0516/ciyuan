package com.ciyuanplus.mobile.module.found.market;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class MarketPresenterModule {
    private final MarketContract.View mView;

    public MarketPresenterModule(MarketContract.View mView) {
        this.mView = mView;
    }

    @Provides
    MarketContract.View providesMarketContractView() {
        return mView;
    }
}
