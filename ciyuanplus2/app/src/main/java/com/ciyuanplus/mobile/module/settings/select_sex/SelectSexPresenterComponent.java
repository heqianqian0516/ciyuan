package com.ciyuanplus.mobile.module.settings.select_sex;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {SelectSexPresenterModule.class})
public interface SelectSexPresenterComponent {
    void inject(SelectSexPopActivity mActivity);
}
