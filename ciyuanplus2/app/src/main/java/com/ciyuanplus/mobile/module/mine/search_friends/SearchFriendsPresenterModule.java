package com.ciyuanplus.mobile.module.mine.search_friends;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class SearchFriendsPresenterModule {
    private final SearchFriendsContract.View mView;

    public SearchFriendsPresenterModule(SearchFriendsContract.View mView) {
        this.mView = mView;
    }

    @Provides
    SearchFriendsContract.View providesSearchFriendsContractView() {
        return mView;
    }
}
