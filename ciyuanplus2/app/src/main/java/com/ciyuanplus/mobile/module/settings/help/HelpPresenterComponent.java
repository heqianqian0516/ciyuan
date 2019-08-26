package com.ciyuanplus.mobile.module.settings.help;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {HelpPresenterModule.class})
public interface HelpPresenterComponent {
    void inject(HelpActivity mActivity);
}
