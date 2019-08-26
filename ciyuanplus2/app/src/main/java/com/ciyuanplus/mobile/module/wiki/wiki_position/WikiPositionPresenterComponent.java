package com.ciyuanplus.mobile.module.wiki.wiki_position;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {WikiPositionPresenterModule.class})
public interface WikiPositionPresenterComponent {
    void inject(WikiPositionActivity mActivity);
}
