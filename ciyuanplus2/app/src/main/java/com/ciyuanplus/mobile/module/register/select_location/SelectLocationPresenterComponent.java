package com.ciyuanplus.mobile.module.register.select_location;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {SelectLocationPresenterModule.class})
public interface SelectLocationPresenterComponent {
    void inject(SelectLocationActivity mActivity);
}
