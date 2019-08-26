package com.ciyuanplus.mobile.module.start_forum.start_option;

import javax.inject.Inject;

/**
 * Created by Alen on 2017/12/11.
 */

public class StartOptionPresenter implements StartOptionContract.Presenter {
    @Inject
    public StartOptionPresenter(StartOptionContract.View mView) {
        StartOptionContract.View view = mView;

    }

    @Override
    public void detachView() {
    }
}
