package com.ciyuanplus.mobile.module.mine.change_community;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {ChangeCommunityPresenterModule.class})
public interface ChangeCommunityPresenterComponent {
    void inject(ChangeCommunityActivity mActivity);
}
