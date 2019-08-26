package com.ciyuanplus.mobile.module.forum_detail.forum_detail;


import dagger.Component;
/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {ForumDetailPresenterModule.class})
public interface ForumDetailPresenterComponent {
    void inject(ForumDetailActivity mActivity);
}
