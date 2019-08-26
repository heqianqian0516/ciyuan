package com.ciyuanplus.mobile.module.wiki.wiki_position;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class WikiPositionPresenterModule {
    private final WikiPositionContract.View mView;

    public WikiPositionPresenterModule(WikiPositionContract.View mView) {
        this.mView = mView;
    }

    @Provides
    WikiPositionContract.View providesWikiPositionContractView() {
        return mView;
    }
}
