package com.ciyuanplus.mobile.module.found.market;

import dagger.Component;

/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {MarketPresenterModule.class})
public interface MarketPresenterComponent {
    void inject(MarketActivity mActivity);
}
