package com.ciyuanplus.mobile.module.start_forum.start_seek_help;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class StartSeekHelpPresenterModule {
    private final StartSeekHelpContract.View mView;

    public StartSeekHelpPresenterModule(StartSeekHelpContract.View mView) {
        this.mView = mView;
    }

    @Provides
    StartSeekHelpContract.View providesStartPostContractView(){
        return mView;
    }
}
