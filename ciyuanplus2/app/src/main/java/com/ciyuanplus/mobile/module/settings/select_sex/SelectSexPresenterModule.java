package com.ciyuanplus.mobile.module.settings.select_sex;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class SelectSexPresenterModule {
    private final SelectSexContract.View mView;

    public SelectSexPresenterModule(SelectSexContract.View mView) {
        this.mView = mView;
    }

    @Provides
    SelectSexContract.View providesSelectSexContractView() {
        return mView;
    }
}
