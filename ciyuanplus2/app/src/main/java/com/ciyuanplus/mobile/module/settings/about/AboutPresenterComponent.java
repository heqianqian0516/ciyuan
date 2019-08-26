package com.ciyuanplus.mobile.module.settings.about;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {AboutPresenterModule.class})
public interface AboutPresenterComponent {
    void inject(AboutActivity mActivity);
}
