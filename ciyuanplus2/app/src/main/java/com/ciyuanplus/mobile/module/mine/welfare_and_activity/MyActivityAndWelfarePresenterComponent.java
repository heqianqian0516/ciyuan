package com.ciyuanplus.mobile.module.mine.welfare_and_activity;

import dagger.Component;

/**
 * Created by kk on 2018/5/30.
 */
@Component(modules = {MyActivityAndWelfarePresenterModule.class})
public interface MyActivityAndWelfarePresenterComponent {

    void inject(MyActivityAndWelfareActivity myActivityAndWelfareActivity);
}
