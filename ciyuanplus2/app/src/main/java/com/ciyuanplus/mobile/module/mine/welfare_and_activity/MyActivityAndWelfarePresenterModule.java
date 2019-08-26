package com.ciyuanplus.mobile.module.mine.welfare_and_activity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kk on 2018/5/30.
 */

@Module
class MyActivityAndWelfarePresenterModule {

    private final MyActivityAndWelfareContract.View mView;

    public MyActivityAndWelfarePresenterModule(MyActivityAndWelfareContract.View mView) {
        this.mView = mView;
    }

    @Provides
    MyActivityAndWelfareContract.View providesMyActivityAndWelfareContractView() {
        return mView;
    }
}
