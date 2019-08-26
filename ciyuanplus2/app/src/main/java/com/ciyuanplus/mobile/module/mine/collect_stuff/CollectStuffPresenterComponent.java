package com.ciyuanplus.mobile.module.mine.collect_stuff;

import dagger.Component;

/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {CollectStuffPresenterModule.class})
public interface CollectStuffPresenterComponent {
    void inject(CollectStuffFragment mActivity);
}
