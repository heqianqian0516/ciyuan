package com.ciyuanplus.mobile.module.news.news;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */


@Module
class NewsFragmentPresenterModule {
    private final NewsFragmentContract.View mView;

    public NewsFragmentPresenterModule(NewsFragmentContract.View mView) {
        this.mView = mView;
    }

    @Provides
    NewsFragmentContract.View providesNewsFragmentContractView() {
        return mView;
    }
}
