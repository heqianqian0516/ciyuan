package com.ciyuanplus.mobile.module.news.marking;

import dagger.Component;

/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {MarkingPresenterModule.class})
public interface MarkingPresenterComponent {
    void inject(MarkingActivity mActivity);
}
