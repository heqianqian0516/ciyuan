package com.ciyuanplus.mobile.module.settings.bind_phone;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.parameter.ChangePhoneApiParameter;
import com.ciyuanplus.mobile.net.parameter.SendSmsCodeApiParameter;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;


/**
 * Created by Alen on 2017/12/11.
 */

public class BindPhonePresenter implements BindPhoneContract.Presenter {
    private static final int MAX_TIMER_COUNT_DOWN = 60;
    private final BindPhoneContract.View mView;
    private Timer timer;
    private int mTimerCount;
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mTimerCount > 0) {
                mView.getResetSendCode(false, --mTimerCount);
            } else {
                mView.getResetSendCode(true, --mTimerCount);
            }
            super.handleMessage(msg);
        }
    };

    @Inject
    public BindPhonePresenter(BindPhoneContract.View mView) {
        this.mView = mView;
    }

    private void timeTask() {
        if (this.timer != null)
            this.timer.cancel();
        this.mTimerCount = BindPhonePresenter.MAX_TIMER_COUNT_DOWN;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                handler.sendMessage(message);
            }
        };
        this.timer = new Timer();
        this.timer.schedule(task, 1000, 1000);
    }

    // 发送验证码
    @Override
    public void sendCode(String mPhone) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_SEND_SMS_CODE_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new SendSmsCodeApiParameter(mPhone, SendSmsCodeApiParameter.SEND_SMS_TYPE_REGISTER).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                ResponseData response1 = new ResponseData(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    CommonToast.getInstance(mView.getDefaultContext().getResources().getString(R.string.string_send_verify_code_success_alert)).show();
                    timeTask();
                }
            }

//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_login_temp_user_fail_alert),
//                        Toast.LENGTH_SHORT).show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }


    // 修改手机号
    @Override
    public void changePhone(String phone, String mCode) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_CHANGE_PHONE_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new ChangePhoneApiParameter(phone, mCode, SendSmsCodeApiParameter.SEND_SMS_TYPE_CHANGE).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                ResponseData response1 = new ResponseData(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {

                    mView.showSuccessMsg();
                    UserInfoData.getInstance().getUserInfoItem().mobile = phone;
                    SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET,
                            Constants.SHARED_PREFERENCES_LOGIN_ACCOUNT, phone);
                    EventCenterManager.asynSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_LOGIN_USER_INFO_UPDATE));
                    ((Activity) mView).finish();
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance(getResources().getString(R.string.string_my_profile_change_phone_fail_alert), Toast.LENGTH_SHORT).show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    @Override
    public void detachView() {
    }
}
