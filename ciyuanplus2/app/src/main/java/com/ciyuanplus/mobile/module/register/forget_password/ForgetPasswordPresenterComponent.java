package com.ciyuanplus.mobile.module.register.forget_password;

import dagger.Component;

/**
 * Created by Alen on 2017/12/26.
 */
@Component(modules = {ForgetPasswordPresenterModule.class})
public interface ForgetPasswordPresenterComponent {
    void inject(ForgetPasswordActivity mActivity);
}
