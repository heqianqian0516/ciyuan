package com.ciyuanplus.mobile.module.settings.change_name;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.parameter.ChangeNameApiParameter;
import com.ciyuanplus.mobile.net.parameter.CheckNickNameApiParameter;
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

public class ChangeNamePresenter implements ChangeNameContract.Presenter {
    private final ChangeNameContract.View mView;

    @Inject
    public ChangeNamePresenter(ChangeNameContract.View mView) {
        this.mView = mView;

    }

    // 修改姓名
    @Override
    public void changeName(final String name) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_CHANGE_NAME_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new ChangeNameApiParameter(name).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                ResponseData response1 = new ResponseData(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(mView.getDefaultContext().getResources().getString(R.string.string_my_profile_change_name_success_alert), Toast.LENGTH_SHORT).show();
                    UserInfoData.getInstance().getUserInfoItem().nickname = name;
                    EventCenterManager.asynSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_LOGIN_USER_INFO_UPDATE));
                    ((Activity) mView.getDefaultContext()).finish();
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance(getResources().getString(R.string.string_my_profile_change_name_fail_alert), Toast.LENGTH_SHORT).show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    //注册的时候检测用户名  是否可用
    @Override
    public void checkUserNickName(String name) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_CHECK_USER_NICK_NAME_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new CheckNickNameApiParameter(name).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                ResponseData response1 = new ResponseData(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    Intent data = new Intent();
                    data.putExtra("name", name);
                    ((Activity) mView.getDefaultContext()).setResult(Activity.RESULT_OK, data);
                    ((Activity) mView.getDefaultContext()).finish();
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance(getResources().getString(R.string.string_my_profile_change_name_fail_alert), Toast.LENGTH_SHORT).show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    @Override
    public void detachView() {
    }
}
