package com.ciyuanplus.mobile.module.start_forum.start_news;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {StartNewsPresenterModule.class})
public interface StartNewsPresenterComponent {
    void inject(StartNewsActivity mActivity);
}
