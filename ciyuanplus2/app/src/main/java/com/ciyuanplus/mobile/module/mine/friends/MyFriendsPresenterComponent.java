package com.ciyuanplus.mobile.module.mine.friends;

import dagger.Component;

/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {MyFriendsPresenterModule.class})
public interface MyFriendsPresenterComponent {
    void inject(MyFriendsActivity mActivity);
}
