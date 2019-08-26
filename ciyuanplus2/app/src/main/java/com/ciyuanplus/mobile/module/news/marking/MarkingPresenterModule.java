package com.ciyuanplus.mobile.module.news.marking;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class MarkingPresenterModule {
    private final MarkingContract.View mView;

    public MarkingPresenterModule(MarkingContract.View mView) {
        this.mView = mView;
    }

    @Provides
    MarkingContract.View providesMarkingContractView() {
        return mView;
    }
}
