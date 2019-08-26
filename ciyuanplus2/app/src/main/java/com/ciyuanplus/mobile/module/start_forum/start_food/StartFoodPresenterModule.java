package com.ciyuanplus.mobile.module.start_forum.start_food;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class StartFoodPresenterModule {
    private final StartFoodContract.View mView;

    public StartFoodPresenterModule(StartFoodContract.View mView) {
        this.mView = mView;
    }

    @Provides
    StartFoodContract.View providesStartFoodContractView(){
        return mView;
    }
}
