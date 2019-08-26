package com.ciyuanplus.mobile.module.register.select_location;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class SelectLocationPresenterModule {
    private final SelectLocationContract.View mView;

    public SelectLocationPresenterModule(SelectLocationContract.View mView) {
        this.mView = mView;
    }

    @Provides
    SelectLocationContract.View providesSelectLocationContractView() {
        return mView;
    }
}
