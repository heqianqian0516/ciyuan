package com.ciyuanplus.mobile.module.register.register;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {RegisterPresenterModule.class})
public interface RegisterPresenterComponent {
    void inject(RegisterActivity mActivity);
}
