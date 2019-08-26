package com.ciyuanplus.mobile.module.mine.my_order;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kk on 2018/5/24.
 */
@Module
class MyOrderListPresenterModule {

    private final MyOrderListContract.View mView;

    public MyOrderListPresenterModule(MyOrderListContract.View mView) {
        this.mView = mView;
    }

    @Provides
    MyOrderListContract.View providesContractView() {
        return mView;
    }
}
