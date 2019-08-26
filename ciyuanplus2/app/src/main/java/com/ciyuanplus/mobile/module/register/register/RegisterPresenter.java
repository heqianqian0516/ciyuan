package com.ciyuanplus.mobile.module.register.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.MainActivityNew;
import com.ciyuanplus.mobile.manager.CacheManager;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.register.select_location.SelectLocationActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.ApiParameter;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.parameter.BindOtherPlatformUserApiParameter;
import com.ciyuanplus.mobile.net.parameter.RegisterNormalUserApiParameter;
import com.ciyuanplus.mobile.net.parameter.SendSmsCodeApiParameter;
import com.ciyuanplus.mobile.net.parameter.UpLoadFileApiParameter;
import com.ciyuanplus.mobile.net.response.GetRandomPhotoResponse;
import com.ciyuanplus.mobile.net.response.LoginResponse;
import com.ciyuanplus.mobile.net.response.UpLoadFileResponse;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.PictureUtils;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.google.gson.Gson;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.content.multi.FilePart;
import com.litesuits.http.request.content.multi.MultipartBody;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

/**
 * Created by Alen on 2017/12/11.
 */

public class RegisterPresenter implements RegisterContract.Presenter {
    private static final int MAX_TIMER_COUNT_DOWN = 60;
    private final Handler handler;
    private final RegisterContract.View mView;
    private Timer timer;
    private int mTimerCount;
    private String mNickName;
    private String mHeadIconUrl;
    private String mOtherPlatformType = "";
    private String mOtherPlatformId = "";
    private int mHasPassword = 1; // 是否有密码

    @Inject
    public RegisterPresenter(RegisterContract.View mView) {
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
//        mHeadIconUrl = UserInfoData.getInstance().getUserInfoItem().photo;
//        mNickName = UserInfoData.getInstance().getUserInfoItem().nickname;
    }

    @Override
    public void initData(Intent intent) {
        if (intent == null || Utils.isStringEmpty(intent.getStringExtra(Constants.INTENT_USER_NAME))) {
            requestRandomPhotoAndName();
        } else {
            mNickName = intent.getStringExtra(Constants.INTENT_USER_NAME);
            mHeadIconUrl = intent.getStringExtra(Constants.INTENT_USER_PHOTO);
            mOtherPlatformType = intent.getStringExtra(Constants.INTENT_OTHER_PLATFORM_TYPE);
            mOtherPlatformId = intent.getStringExtra(Constants.INTENT_OTHER_PLATFORM_ID);
            mHasPassword = intent.getIntExtra(Constants.INTENT_OTHER_PLATFORM_HAS_PASSWORD, 1);

            mView.changeHeadIcon(mHeadIconUrl);
            mView.changeName(mNickName);
        }
    }

    // 获取随机头像和昵称
    private void requestRandomPhotoAndName() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_GET_RANDOM_PHOTO_AND_NAME_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new ApiParameter().getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                GetRandomPhotoResponse response1 = new GetRandomPhotoResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    mHeadIconUrl = response1.userInfoItem.photo;
                    mNickName = response1.userInfoItem.nickname;
                    mView.changeHeadIcon(mHeadIconUrl);
                    mView.changeName(mNickName);
                } else {// 当前用户没有注册过
                    CommonToast.getInstance(response1.mMsg).show();
                }
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    @Override
    public void setNickName(String mNickName) {
        this.mNickName = mNickName;
    }

    @Override
    public void setHeadIconUrl(String mHeadIconUrl) {
        this.mHeadIconUrl = mHeadIconUrl;
    }

    @Override
    public void uploadImageFile(String mHeadIconPath) {
        if (Utils.isStringEmpty(mHeadIconPath)) return;
        mView.showLoadingDialog();
        MultipartBody body = new UpLoadFileApiParameter().getRequestBody();
        File origin = new File(mHeadIconPath);
        String fileName = origin.getName();
        String compressPath = PictureUtils.compressImage(mHeadIconPath,
                CacheManager.getInstance().getCacheDirectory() + fileName.substring(0, fileName.lastIndexOf(".")) + "compressImage.jpeg");
        File compressImage = new File(compressPath);
        if (compressImage.exists()) body.addPart(new FilePart("file", compressImage, "image/jpeg"));
        else body.addPart(new FilePart("file", new File(mHeadIconPath), "image/jpeg"));

        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_UPLOAD_FILE_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(body);
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                mView.dismissLoadingDialog();
                UpLoadFileResponse response1 = new UpLoadFileResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    mHeadIconUrl = response1.url;
                    mView.changeHeadIcon(mHeadIconUrl);
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                mView.dismissLoadingDialog();
//                CommonToast.getInstance(getResources().getString(R.string.string_my_profile_upload_head_fail_alert), Toast.LENGTH_SHORT).show();

            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    @Override
    public void detachView() {
    }

    // 发送验证码
    @Override
    public void sendCode(String accout) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_SEND_SMS_CODE_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new SendSmsCodeApiParameter(accout, SendSmsCodeApiParameter.SEND_SMS_TYPE_REGISTER).getRequestBody());
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
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_login_temp_user_fail_alert),
//                        Toast.LENGTH_SHORT).show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    // 请求注册，先判断有没有三方登录之后有没有设置过密码
    // 再进行用户注册
    @Override
    public void requestRegister(String accout1, String mPassword, String mVerify) {
        if (Utils.isStringEmpty(mNickName)) {
            CommonToast.getInstance("请输入昵称").show();
            return;
        }
        if (Utils.isStringEmpty(mHeadIconUrl)) {
            CommonToast.getInstance("请先选择头像").show();
            return;
        }
        if (mHasPassword == 0) {
            bind(accout1, mPassword, mVerify);
        } else {
            register(accout1, mPassword, mVerify);
        }

    }

    // 绑定小程序  三方登录过来的账号
    private void bind(String accout1, String mPassword, String mVerify) {
        if (!LoginStateManager.isLogin()) return;
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_BIND_OTHER_PLATFORM_URL);
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new BindOtherPlatformUserApiParameter(accout1, mPassword, mVerify,
                SendSmsCodeApiParameter.SEND_SMS_TYPE_REGISTER, mNickName, mHeadIconUrl).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                LoginResponse response1 = new LoginResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    // 需要把之前存储的临时用户清掉，因为已经没有这个临时用户了。
                    // SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_TEMP_USER_ID, "");

                    // 这里由于后台接口没有提供currentCommunityName 的字段。 需要把临时用户的小区名称直接带过来
//                    response1.userInfoItem.currentCommunityName = UserInfoData.getInstance().getUserInfoItem().currentCommunityName;
                    response1.rawData = new Gson().toJson(response1.userInfoItem);

                    UserInfoData.getInstance().insertOrReplace(response1.userInfoItem.uuid, response1.rawData);
                    LoginStateManager.saveLoginInfo(accout1, mPassword, response1.userInfoItem.uuid, response1.token);
                    SharedPreferencesManager.putString("ShaZi", "tok", response1.token);
                    CommonToast.getInstance("注册成功").show();
                    //RongManager.connect();
                    LoginStateManager.registerUmengPushDeviceToken();
                    if (Utils.isStringEmpty(response1.userInfoItem.currentCommunityUuid)) {// 没有小区
                        // 进入选择位置页面
                        Intent intent = new Intent(mView.getDefaultContext(), SelectLocationActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mView.getDefaultContext().startActivity(intent);
                    } else {
                        //
                        Intent intent = new Intent(App.mContext, MainActivityNew.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("Animeter", true);
                        App.mContext.startActivity(intent);
                    }
                    ((Activity) mView.getDefaultContext()).finish();
                }
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    // 注册， 如果成功要修改登录状态，修改用户信息，需要刷新相关页面
    private void register(String accout1, String mPassword, String mVerify) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_REGISTER_FORMAL_USER_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RegisterNormalUserApiParameter(accout1, mPassword, mVerify,
                SendSmsCodeApiParameter.SEND_SMS_TYPE_REGISTER, "0"
                , mNickName, mHeadIconUrl, mOtherPlatformId, mOtherPlatformType).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                LoginResponse response1 = new LoginResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    // 需要把之前存储的临时用户清掉，因为已经没有这个临时用户了。
                    SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_TEMP_USER_ID, "");

                    // 这里由于后台接口没有提供currentCommunityName 的字段。 需要把临时用户的小区名称直接带过来
//                    response1.userInfoItem.currentCommunityName = UserInfoData.getInstance().getUserInfoItem().currentCommunityName;
                    response1.rawData = new Gson().toJson(response1.userInfoItem);

                    UserInfoData.getInstance().insertOrReplace(response1.userInfoItem.uuid, response1.rawData);
                    LoginStateManager.saveLoginInfo(accout1, mPassword, response1.userInfoItem.uuid, response1.token);
                    CommonToast.getInstance("注册成功").show();
                    SharedPreferencesManager.putString("User", "uuid", response1.userInfoItem.uuid);
                    SharedPreferencesManager.putBoolean("Mainlayout", "main", true);
                    SharedPreferencesManager.putString("ShaZi", "tok", response1.token);
                    //RongManager.connect();
                    LoginStateManager.registerUmengPushDeviceToken();
                    if (Utils.isStringEmpty(response1.userInfoItem.currentCommunityUuid)) {// 没有小区
                        // 进入选择位置页面
                        Intent intent = new Intent(App.mContext, SelectLocationActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mView.getDefaultContext().startActivity(intent);
                    } else {
                        //
                        Intent intent = new Intent(App.mContext, MainActivityNew.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("Animeter", true);
                        App.mContext.startActivity(intent);
                    }
                    ((Activity) mView.getDefaultContext()).finish();
                }
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    // 开启倒计时
    private void timeTask() {
        if (this.timer != null)
            this.timer.cancel();
        this.mTimerCount = RegisterPresenter.MAX_TIMER_COUNT_DOWN;
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


    // 重新设置发送验证码按钮的文本
    private void getResetSendCode(boolean isOnclick) {
        if (isOnclick) {
            mView.setVerifyText(mView.getDefaultContext().getResources().getString(R.string.string_forget_password_resend_verify_alert), true);
        } else {
            mView.setVerifyText(--mTimerCount + mView.getDefaultContext().getResources().getString(R.string.string_forget_password_resend_verify_alert), false);
        }
    }

    public String getUserName() {
        return mNickName;
    }
}
