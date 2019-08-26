package com.ciyuanplus.mobile.module.start_forum.select_post_location;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class SelectPostLocationPresenterModule {
    private final SelectPostLocationContract.View mView;

    public SelectPostLocationPresenterModule(SelectPostLocationContract.View mView) {
        this.mView = mView;
    }

    @Provides
    SelectPostLocationContract.View providesSelectPostLocationContractView(){
        return mView;
    }
}
