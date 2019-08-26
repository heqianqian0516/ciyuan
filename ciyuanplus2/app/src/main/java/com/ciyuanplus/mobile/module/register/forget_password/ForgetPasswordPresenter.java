package com.ciyuanplus.mobile.module.register.forget_password;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.parameter.ForgetPasswordApiParameter;
import com.ciyuanplus.mobile.net.parameter.SendSmsCodeApiParameter;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

/**
 * Created by Alen on 2017/12/26.
 */

public class ForgetPasswordPresenter implements ForgetPasswordContract.Presenter {
    private static final int MAX_TIMER_COUNT_DOWN = 60;
    private final Handler handler;
    private final ForgetPasswordContract.View mView;
    private Timer timer;
    private int mTimerCount;

    @Inject
    public ForgetPasswordPresenter(ForgetPasswordContract.View mView) {
        this.mView = mView;

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (mTimerCount > 0) {
                    getResetSendCode(false);
                } else {
                    getResetSendCode(true);
                }
                super.handleMessage(msg);
            }
        };
    }

    // 发送验证码
    public void sendCode(String phone) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_SEND_SMS_CODE_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new SendSmsCodeApiParameter(phone, SendSmsCodeApiParameter.SEND_SMS_TYPE_FORGET).getRequestBody());
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


    // 修改姓名
    public void forgetPassword(String phone, String password, String verify) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_FORGET_PASSWORD_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new ForgetPasswordApiParameter(phone, verify, password, SendSmsCodeApiParameter.SEND_SMS_TYPE_FORGET).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                ResponseData response1 = new ResponseData(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(mView.getDefaultContext().getResources().getString(R.string.string_forget_password_change_success_alert), Toast.LENGTH_SHORT).show();
//                    SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET,
//                            Constants.SHARED_PREFERENCES_LOGIN_PASSWORD, mPassword);// 不需要保存它的密码
                    mView.closeCurrentActivity();
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance(getResources().getString(R.string.string_forget_password_change_fail_alert), Toast.LENGTH_SHORT).show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    private void timeTask() {
        if (this.timer != null)
            this.timer.cancel();
        this.mTimerCount = ForgetPasswordPresenter.MAX_TIMER_COUNT_DOWN;
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

    private void getResetSendCode(boolean isOnclick) {
        if (isOnclick) {
            mView.setVerifyTextState(mView.getDefaultContext().getResources().getString(R.string.string_forget_password_resend_verify_alert)
                    , true);
        } else {
            mView.setVerifyTextState(--mTimerCount + mView.getDefaultContext().getResources().getString(R.string.string_forget_password_resend_verify_hint)
                    , false);
        }
    }

    @Override
    public void detachView() {

    }
}
