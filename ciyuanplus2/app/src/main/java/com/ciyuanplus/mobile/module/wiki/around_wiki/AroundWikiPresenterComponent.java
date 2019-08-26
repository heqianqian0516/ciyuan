package com.ciyuanplus.mobile.module.wiki.around_wiki;

import dagger.Component;


/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {AroundWikiPresenterModule.class})
public interface AroundWikiPresenterComponent {
    void inject(AroundWikiActivity mActivity);
}
