package com.ciyuanplus.mobile.module.start_forum.start_news;

import dagger.Module;
import dagger.Provides;
/**
 * Created by Alen on 2017/12/11.
 */
@Module
class StartNewsPresenterModule {
    private final StartNewsContract.View mView;

    public StartNewsPresenterModule(StartNewsContract.View mView) {
        this.mView = mView;
    }

    @Provides
    StartNewsContract.View providesStartNewsContractView(){
        return mView;
    }
}
