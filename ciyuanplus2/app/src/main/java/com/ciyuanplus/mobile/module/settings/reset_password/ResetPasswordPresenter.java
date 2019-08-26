package com.ciyuanplus.mobile.module.settings.reset_password;

import android.app.Activity;
import android.widget.Toast;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.parameter.ChangePasswordApiParameter;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import javax.inject.Inject;

/**
 * Created by Alen on 2017/12/11.
 */

public class ResetPasswordPresenter implements ResetPasswordContract.Presenter {
    private final ResetPasswordContract.View mView;

    @Inject
    public ResetPasswordPresenter(ResetPasswordContract.View mView) {
        this.mView = mView;

    }

    // 修改密码
    @Override
    public void changePassword(String mOldPassword, String mNewPassword) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_CHANGE_PASSWORD_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new ChangePasswordApiParameter(mOldPassword, mNewPassword).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                ResponseData response1 = new ResponseData(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(mView.getDefaultContext().getResources().getString(R.string.string_my_profile_change_password_success_alert), Toast.LENGTH_SHORT).show();
                    SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET,
                            Constants.SHARED_PREFERENCES_LOGIN_PASSWORD, mNewPassword);
                    ((Activity) mView).finish();
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance(getResources().getString(R.string.string_my_profile_change_password_fail_alert), Toast.LENGTH_SHORT).show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    @Override
    public void detachView() {
    }
}
