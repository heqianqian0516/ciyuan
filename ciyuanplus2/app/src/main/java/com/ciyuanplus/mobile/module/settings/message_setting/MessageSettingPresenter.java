package com.ciyuanplus.mobile.module.settings.message_setting;

import javax.inject.Inject;

/**
 * Created by Alen on 2017/12/11.
 */

public class MessageSettingPresenter implements MessageSettingContract.Presenter {

    @Inject
    public MessageSettingPresenter(MessageSettingContract.View mView) {
        MessageSettingContract.View view = mView;

    }

    @Override
    public void detachView() {
    }
}
