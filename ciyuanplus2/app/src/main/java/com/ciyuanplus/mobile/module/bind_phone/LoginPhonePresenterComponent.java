package com.ciyuanplus.mobile.module.bind_phone;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {LoginPhonePresenterModule.class})
public interface LoginPhonePresenterComponent {
    void inject(LoginBindActivity mActivity);
}
