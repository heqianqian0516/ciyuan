package com.ciyuanplus.mobile.module.mine.friends;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class MyFriendsPresenterModule {
    private final MyFriendsContract.View mView;

    public MyFriendsPresenterModule(MyFriendsContract.View mView) {
        this.mView = mView;
    }

    @Provides
    MyFriendsContract.View providesMyFriendsContractView() {
        return mView;
    }
}
