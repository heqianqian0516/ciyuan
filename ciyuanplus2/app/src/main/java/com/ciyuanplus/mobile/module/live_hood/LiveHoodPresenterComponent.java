package com.ciyuanplus.mobile.module.live_hood;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {LiveHoodPresenterModule.class})
public interface LiveHoodPresenterComponent {
    void inject(LiveHoodActivity mActivity);
}
