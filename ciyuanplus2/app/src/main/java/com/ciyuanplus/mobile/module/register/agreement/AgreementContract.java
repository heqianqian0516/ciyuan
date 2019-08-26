package com.ciyuanplus.mobile.module.register.agreement;

import com.ciyuanplus.mobile.module.BaseContract;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by Alen on 2017/12/11.
 */

class AgreementContract {
    interface Presenter extends BaseContract.Presenter {
        void requestAgreementContract();
    }

    interface View extends BaseContract.View {
        WebView getWebView();
    }
}
