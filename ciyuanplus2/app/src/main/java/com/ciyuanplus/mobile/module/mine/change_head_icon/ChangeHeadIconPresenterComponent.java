package com.ciyuanplus.mobile.module.mine.change_head_icon;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {ChangeHeadIconPresenterModule.class})
public interface ChangeHeadIconPresenterComponent {
    void inject(ChangeHeadIconActivity mActivity);
}
