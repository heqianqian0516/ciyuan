package com.ciyuanplus.mobile.module.mine.my_order_detail;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kk on 2018/5/24.
 */

@Module
class MyOrderDetailPresenterModule {

    private final MyOrderDetailContract.View mView;

    public MyOrderDetailPresenterModule(MyOrderDetailContract.View mView) {
        this.mView = mView;
    }

    @Provides
    MyOrderDetailContract.View providesFormDetailContractView() {
        return mView;
    }
}
