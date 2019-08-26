package com.ciyuanplus.mobile.module.start_forum.start_live;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {StartLivePresenterModule.class})
public interface StartLivePresenterComponent {
    void inject(StartLiveActivity mActivity);
}
