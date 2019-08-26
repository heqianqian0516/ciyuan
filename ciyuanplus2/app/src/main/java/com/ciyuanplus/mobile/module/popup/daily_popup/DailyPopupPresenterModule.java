package com.ciyuanplus.mobile.module.popup.daily_popup;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class DailyPopupPresenterModule {
    private final DailyPopupContract.View mView;

    public DailyPopupPresenterModule(DailyPopupContract.View mView) {
        this.mView = mView;
    }

    @Provides
    DailyPopupContract.View providesDailyPopupContractView() {
        return mView;
    }
}
