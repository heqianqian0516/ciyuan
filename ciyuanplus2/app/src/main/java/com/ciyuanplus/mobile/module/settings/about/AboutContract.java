package com.ciyuanplus.mobile.module.settings.about;

import com.ciyuanplus.mobile.module.BaseContract;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by Alen on 2017/12/11.
 */

class AboutContract {
    interface Presenter extends BaseContract.Presenter {
        void requestAbout();//请求数据
    }

    interface View extends BaseContract.View {
        WebView getAboutWebView();
    }
}
