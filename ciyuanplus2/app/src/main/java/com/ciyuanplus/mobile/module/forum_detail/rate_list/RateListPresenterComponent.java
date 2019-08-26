package com.ciyuanplus.mobile.module.forum_detail.rate_list;

import dagger.Component;

/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {RateListPresenterModule.class})
public interface RateListPresenterComponent {
    void inject(RateListActivity mActivity);
}
