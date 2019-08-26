package com.ciyuanplus.mobile.module.settings.setting;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {SettingsPresenterModule.class})
public interface SettingsPresenterComponent {
    void inject(SettingsActivity mActivity);
}
