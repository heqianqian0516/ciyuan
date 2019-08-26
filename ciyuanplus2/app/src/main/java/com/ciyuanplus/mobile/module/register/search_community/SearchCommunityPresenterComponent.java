package com.ciyuanplus.mobile.module.register.search_community;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {SearchCommunityPresenterModule.class})
public interface SearchCommunityPresenterComponent {
    void inject(SearchCommunityActivity mActivity);
}
