package com.ciyuanplus.mobile.module.popup.daily_popup;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {DailyPopupPresenterModule.class})
public interface DailyPopupPresenterComponent {
    void inject(DailyPopupActivity mActivity);
}
