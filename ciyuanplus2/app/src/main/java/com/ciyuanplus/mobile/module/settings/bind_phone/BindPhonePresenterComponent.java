package com.ciyuanplus.mobile.module.settings.bind_phone;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {BindPhonePresenterModule.class})
public interface BindPhonePresenterComponent {
    void inject(BindPhoneActivity mActivity);
}
