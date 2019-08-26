package com.ciyuanplus.mobile.module.forum_detail.want_list;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {WantListPresenterModule.class})
public interface WantListPresenterComponent {
    void inject(WantListActivity mActivity);
}
