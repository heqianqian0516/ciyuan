package com.ciyuanplus.mobile.module.forum_detail.want_list;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class WantListPresenterModule {
    private final WantListContract.View mView;

    public WantListPresenterModule(WantListContract.View mView) {
        this.mView = mView;
    }

    @Provides
    WantListContract.View providesWantListContractView(){
        return mView;
    }
}
