package com.ciyuanplus.mobile.module.settings.reset_password;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {ResetPasswordPresenterModule.class})
public interface ResetPasswordPresenterComponent {
    void inject(ResetPasswordActivity mActivity);
}
