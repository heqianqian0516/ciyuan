package com.ciyuanplus.mobile.module.settings.about;

import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.parameter.RequestContractApiParameter;
import com.ciyuanplus.mobile.net.response.RequestContractResponse;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import javax.inject.Inject;

/**
 * Created by Alen on 2017/12/11.
 */

public class AboutPresenter implements AboutContract.Presenter {
    private final AboutContract.View mView;

    @Inject
    public AboutPresenter(AboutContract.View mView) {
        this.mView = mView;

    }

    // 获取关于我们
    public void requestAbout() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_ABOUT_US_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestContractApiParameter().getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                RequestContractResponse response1 = new RequestContractResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    mView.getAboutWebView().loadDataWithBaseURL(null, ApiContant.WEB_HEAD_STSRT + response1.data1 + ApiContant.WEB_HEAD_END, "text/html", "utf-8", null);
                }
            }

//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_request_register_contract_fail_alert),
//                        Toast.LENGTH_SHORT).show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    @Override
    public void detachView() {
    }
}
