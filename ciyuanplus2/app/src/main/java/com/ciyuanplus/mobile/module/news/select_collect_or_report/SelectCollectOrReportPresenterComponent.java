package com.ciyuanplus.mobile.module.news.select_collect_or_report;

import dagger.Component;

/**
 * Created by Alen on 2017/12/11.
 */
@Component(modules = {SelectCollectOrReportPresenterModule.class})
public interface SelectCollectOrReportPresenterComponent {
    void inject(SelectCollectOrReportActivity mActivity);
}
