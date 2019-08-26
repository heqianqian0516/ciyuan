package com.ciyuanplus.mobile.module.start_forum.add_position;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {AddPositionPresenterModule.class})
public interface AddPositionPresenterComponent {
    void inject(AddPositionActivity mActivity);
}
