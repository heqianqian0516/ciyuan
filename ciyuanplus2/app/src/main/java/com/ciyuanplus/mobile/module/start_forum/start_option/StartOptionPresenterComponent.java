package com.ciyuanplus.mobile.module.start_forum.start_option;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {StartOptionPresenterModule.class})
public interface StartOptionPresenterComponent {
    void inject(StartOptionActivity mActivity);
}
