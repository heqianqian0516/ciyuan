package com.ciyuanplus.mobile.module.start_forum.start_seek_help;

import dagger.Component;

/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {StartSeekHelpPresenterModule.class})
public interface StartSeekHelpPresenterComponent {
    void inject(StartSeekHelpActivity mActivity);
}
