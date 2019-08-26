package com.ciyuanplus.mobile.module.register.search_community;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class SearchCommunityPresenterModule {
    private final SearchCommunityContract.View mView;

    public SearchCommunityPresenterModule(SearchCommunityContract.View mView) {
        this.mView = mView;
    }

    @Provides
    SearchCommunityContract.View providesSearchCommunityContractView() {
        return mView;
    }
}
