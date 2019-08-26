package com.ciyuanplus.mobile.module.login;

import dagger.Component;

/**
 * Created by Alen on 2017/12/26.
 */
@Component(modules = {LoginPresenterModule.class})
public interface LoginPresenterComponent {
    void inject(LoginActivity mActivity);
}
