package com.ciyuanplus.mobile.module.news.select_collect_or_report;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alen on 2017/12/11.
 */
@Module
class SelectCollectOrReportPresenterModule {
    private final SelectCollectOrReportContract.View mView;

    public SelectCollectOrReportPresenterModule(SelectCollectOrReportContract.View mView) {
        this.mView = mView;
    }

    @Provides
    SelectCollectOrReportContract.View providesSelectCollectOrReportContractView() {
        return mView;
    }
}
