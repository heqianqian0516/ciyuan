package com.ciyuanplus.mobile.module.settings.message_setting;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {MessageSettingPresenterModule.class})
public interface MessageSettingPresenterComponent {
    void inject(MessageSettingActivity mActivity);
}
