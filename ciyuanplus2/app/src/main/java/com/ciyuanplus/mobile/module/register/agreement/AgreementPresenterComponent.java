package com.ciyuanplus.mobile.module.register.agreement;

import dagger.Component;

/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {AgreementPresenterModule.class})
public interface AgreementPresenterComponent {
    void inject(AgreementActivity mActivity);
}
