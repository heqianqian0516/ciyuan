package com.ciyuanplus.mobile.module.start_forum.add_position;

import dagger.Module;
import dagger.Provides;
/**
 * Created by Alen on 2017/12/11.
 */
@Module
class AddPositionPresenterModule {
    private final AddPositionContract.View mView;

    public AddPositionPresenterModule(AddPositionContract.View mView) {
        this.mView = mView;
    }

    @Provides
    AddPositionContract.View providesAddPositionContractView(){
        return mView;
    }
}
