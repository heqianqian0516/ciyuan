package com.ciyuanplus.mobile.module.mine.mine;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {MinePresenterModule.class})
public interface MinePresenterComponent {
    void inject(MineFragmentNew mActivity);
}
