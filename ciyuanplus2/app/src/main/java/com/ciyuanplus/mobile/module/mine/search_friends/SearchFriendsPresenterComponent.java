package com.ciyuanplus.mobile.module.mine.search_friends;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {SearchFriendsPresenterModule.class})
public interface SearchFriendsPresenterComponent {
    void inject(SearchFriendsActivity mActivity);
}
