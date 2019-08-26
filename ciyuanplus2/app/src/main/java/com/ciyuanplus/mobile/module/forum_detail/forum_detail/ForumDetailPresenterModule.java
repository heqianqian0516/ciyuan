package com.ciyuanplus.mobile.module.forum_detail.forum_detail;


import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
public
class ForumDetailPresenterModule {
    private final ForumDetailContract.View mView;

    public ForumDetailPresenterModule(ForumDetailContract.View mView) {
        this.mView = mView;
    }

    @Provides
    ForumDetailContract.View providesForumDetailContractView(){
        return mView;
    }
}
