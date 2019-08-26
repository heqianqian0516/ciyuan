package com.ciyuanplus.mobile.module.news.news;

import dagger.Component;

/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {NewsFragmentPresenterModule.class})
public interface NewsFragmentPresenterComponent {
    void inject(NewsFragment mFragment);
}
