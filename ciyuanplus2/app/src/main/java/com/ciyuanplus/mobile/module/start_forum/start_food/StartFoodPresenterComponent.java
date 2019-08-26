package com.ciyuanplus.mobile.module.start_forum.start_food;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {StartFoodPresenterModule.class})
public interface StartFoodPresenterComponent {
    void inject(StartFoodActivity mActivity);
}
