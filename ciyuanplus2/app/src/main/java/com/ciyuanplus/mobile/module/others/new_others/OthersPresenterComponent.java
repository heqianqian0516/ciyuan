package com.ciyuanplus.mobile.module.others.new_others;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {OthersPresenterModule.class})
public interface OthersPresenterComponent {
    void inject(OthersActivity mActivity);
}
