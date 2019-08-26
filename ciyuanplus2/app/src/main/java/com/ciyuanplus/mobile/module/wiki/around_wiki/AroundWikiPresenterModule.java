package com.ciyuanplus.mobile.module.wiki.around_wiki;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class AroundWikiPresenterModule {
    private final AroundWikiContract.View mView;

    public AroundWikiPresenterModule(AroundWikiContract.View mView) {
        this.mView = mView;
    }

    @Provides
    AroundWikiContract.View providesAroundWikiContractView() {
        return mView;
    }
}
