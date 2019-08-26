package com.ciyuanplus.mobile.module.settings.select_sex;

import android.app.Activity;
import android.widget.Toast;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.parameter.ChangeSexApiParameter;
import com.ciyuanplus.mobile.net.response.ChangeProfileResponse;
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

public class SelectSexPresenter implements SelectSexContract.Presenter {
    private final SelectSexContract.View mView;

    @Inject
    public SelectSexPresenter(SelectSexContract.View mView) {
        this.mView = mView;

    }

    // 修改姓别
    @Override
    public void changeSex(final int sex) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_CHANGE_SEX_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new ChangeSexApiParameter(sex).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                ChangeProfileResponse response1 = new ChangeProfileResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(mView.getDefaultContext().getResources().getString(R.string.string_my_profile_change_sex_success_alert), Toast.LENGTH_SHORT).show();
                    UserInfoData.getInstance().getUserInfoItem().sex = sex;
                    UserInfoData.getInstance().getUserInfoItem().photo = response1.userInfoItem.photo;
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
//                CommonToast.getInstance(getResources().getString(R.string.string_my_profile_change_sex_fail_alert), Toast.LENGTH_SHORT).show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    @Override
    public void detachView() {
    }
}
