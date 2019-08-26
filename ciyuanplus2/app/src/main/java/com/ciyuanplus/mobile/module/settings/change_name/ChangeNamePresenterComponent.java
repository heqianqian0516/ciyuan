package com.ciyuanplus.mobile.module.settings.change_name;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {ChangeNamePresenterModule.class})
public interface ChangeNamePresenterComponent {
    void inject(ChangeNameActivity mActivity);
}
