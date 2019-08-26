package com.ciyuanplus.mobile.module.start_forum.start_stuff;

import dagger.Component;

/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {StartStuffPresenterModule.class})
public interface StartStuffPresenterComponent {
    void inject(StartStuffActivity mActivity);
}
