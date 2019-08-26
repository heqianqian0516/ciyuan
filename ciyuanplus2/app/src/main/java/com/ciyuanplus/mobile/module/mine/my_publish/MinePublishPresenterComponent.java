package com.ciyuanplus.mobile.module.mine.my_publish;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {MinePublishPresenterModule.class})
public interface MinePublishPresenterComponent {
    void inject(MinePublishActivity mActivity);
}
