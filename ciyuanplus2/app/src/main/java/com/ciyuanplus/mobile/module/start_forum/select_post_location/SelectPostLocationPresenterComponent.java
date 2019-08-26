package com.ciyuanplus.mobile.module.start_forum.select_post_location;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {SelectPostLocationPresenterModule.class})
public interface SelectPostLocationPresenterComponent {
    void inject(SelectPostLocationActivity mActivity);
}
