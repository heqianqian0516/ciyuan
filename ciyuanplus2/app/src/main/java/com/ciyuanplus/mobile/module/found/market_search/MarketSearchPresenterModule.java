package com.ciyuanplus.mobile.module.found.market_search;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class MarketSearchPresenterModule {
    private final MarketSearchContract.View mView;

    public MarketSearchPresenterModule(MarketSearchContract.View mView) {
        this.mView = mView;
    }

    @Provides
    MarketSearchContract.View providesMarketSearchContractView() {
        return mView;
    }
}
