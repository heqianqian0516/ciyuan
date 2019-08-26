package com.ciyuanplus.mobile.module.found.market_search;

import dagger.Component;

/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {MarketSearchPresenterModule.class})
public interface MarketSearchPresenterComponent {
    void inject(MarketSearchActivity mActivity);
}
