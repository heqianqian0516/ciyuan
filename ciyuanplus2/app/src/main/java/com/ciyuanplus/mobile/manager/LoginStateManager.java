package com.ciyuanplus.mobile.manager;

import android.content.Intent;
import android.widget.Toast;

import com.blankj.utilcode.util.StringUtils;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.MainActivityNew;
import com.ciyuanplus.mobile.module.bind_phone.LoginBindActivity;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.parameter.AutoLoginApiParameter;
import com.ciyuanplus.mobile.net.parameter.LogOutApiParameter;
import com.ciyuanplus.mobile.net.parameter.LoginApiParameter;
import com.ciyuanplus.mobile.net.parameter.RequestOtherLoginApiParameter;
import com.ciyuanplus.mobile.net.parameter.SaveDeviceTokenParameter;
import com.ciyuanplus.mobile.net.response.LoginResponse;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;
import com.orhanobut.logger.Logger;

/**
 * Created by Alen on 2017/5/5.
 */

public class LoginStateManager {
    public static boolean isLogin() {

        return SharedPreferencesManager.getBoolean(
                Constants.SHARED_PREFERENCES_SET,
                Constants.SHARED_PREFERENCES_LOGIN_ISLOGIN, false);
    }

    private static void setLoginStatus(boolean isLogin) {
        SharedPreferencesManager.putBoolean(Constants.SHARED_PREFERENCES_SET,
                Constants.SHARED_PREFERENCES_LOGIN_ISLOGIN, isLogin);
        EventCenterManager.asynSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_LOGIN_USER_INFO_UPDATE));
    }

    public static void saveLoginInfo(String account, String pwd, String userId,
                                     String sessionkey) {
        SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET,
                Constants.SHARED_PREFERENCES_LOGIN_ACCOUNT, account);
        SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET,
                Constants.SHARED_PREFERENCES_LOGIN_USER_ID, userId);
        SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET,
                Constants.SHARED_PREFERENCES_LOGIN_PASSWORD, pwd);
        SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET,
                Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, sessionkey);
        LoginStateManager.setLoginStatus(true);
    }

    //清除 session   userid   userinfo
    // 清楚 sex
    private static void clearLogStates() {
        LoginStateManager.setLoginStatus(false);
        SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET,
                Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET,
                Constants.SHARED_PREFERENCES_LOGIN_USER_ID, "");

        UserInfoData.getInstance().clearUserInfo();
    }



    // 就强制退出登录， 不请求接口， 直接清除登录信息
    public static void forceLogout() {
        clearLogStates();
//        RongIM.getInstance().logout();// 还需要退出融云
        NoticeSettingManager.removeUpushGroup();
    }

    /**
     * 正式用户退出登录， 请求退出登录接口，成功之后清除用户信息，并且退出到性别选择页面
     */
    public static void logout() {
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_LOGOUT_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new LogOutApiParameter().getRequestBody());
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(App.mContext) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                ResponseData response1 = new ResponseData(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    //
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    clearLogStates();
//                    RongIM.getInstance().logout();// 还需要退出融云
//                    NoticeSettingManager.removeUpushGroup();

                    Intent intent = new Intent(App.mContext, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    App.mContext.startActivity(intent);

                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_logout_fail_alert),
                        Toast.LENGTH_SHORT).show();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    //闪屏页登录 有可能是三方登录的用户
    public static void tryToLogin() {
        int type = SharedPreferencesManager.getInt(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_TYPE, 0);
        if (type == 0) {
            autoLogin(type);
//            requestLogin("", "", SendSmsCodeApiParameter.SEND_SMS_TYPE_LOGIN);
        } else {
            String uuid = SharedPreferencesManager.getString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_OTHER_PLATRORM_ACCOUNT, "");
            requestOtherPlatformLogin(uuid, type);
        }
    }

    private static void requestOtherPlatformLogin(String uuid, int type) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_OTHER_LOGIN_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestOtherLoginApiParameter(uuid, "", type + "","").getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(App.mContext) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                LoginResponse response1 = new LoginResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {

                    SharedPreferencesManager.putInt(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_TYPE, type);
                    SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_OTHER_PLATRORM_ACCOUNT, uuid);

                    UserInfoData.getInstance().insertOrReplace(response1.userInfoItem.uuid, response1.rawData);
                    LoginStateManager.saveLoginInfo(response1.userInfoItem.uuid, "", response1.userInfoItem.uuid, response1.token);
                    LoginStateManager.registerUmengPushDeviceToken();
                    if (StringUtils.isEmpty(response1.userInfoItem.mobile)) {

                        Intent intent = new Intent(App.mContext, LoginBindActivity.class);
                        intent.putExtra(Constants.INTENT_BIND_MOBILE, 1);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        App.mContext.startActivity(intent);
                    } else {
                        CommonToast.getInstance("登录成功").show();

                        Intent intent = new Intent(App.mContext, MainActivityNew.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        App.mContext.startActivity(intent);
                    }
                } else {// 没有登录成功
                    Intent intent = new Intent(App.mContext, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    App.mContext.startActivity(intent);
                }
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    /**
     * 自动登录，
     */

    public static void autoLogin(int type) {

        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");

        String userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;

        if (Utils.isStringEmpty(userUuid) || Utils.isStringEmpty(sessionKey)) {

            Intent intent = new Intent(App.mContext, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            App.mContext.startActivity(intent);
        } else {


            StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                    + ApiContant.REQUEST_AUTO_LOGIN);
            postRequest.setMethod(HttpMethods.Post);
            postRequest.addHeader("authToken", sessionKey);

            postRequest.setHttpBody(new AutoLoginApiParameter(userUuid, String.valueOf(type)).getRequestBody());
            postRequest.setHttpListener(new MyHttpListener<String>(App.mContext) {
                @Override
                public void onSuccess(String s, Response<String> response) {
                    super.onSuccess(s, response);
                    LoginResponse response1 = new LoginResponse(s);
                    //SharedPreferencesManager.putString("User","uuid",response1.userInfoItem.uuid);
                    if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                        StatisticsManager.onErrorInfo(response.getRawString(), "" + s);

                        //
//                        CommonToast.getInstance(response1.mMsg).show();

                        Intent intent = new Intent(App.mContext, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        App.mContext.startActivity(intent);

                    } else {
                        registerUmengPushDeviceToken();
                        CommonToast.getInstance("登录成功").show();

                        //
                        Intent intent = new Intent(App.mContext, MainActivityNew.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        App.mContext.startActivity(intent);

                    }
                }

                @Override
                public void onFailure(HttpException e, Response<String> response) {
                    super.onFailure(e, response);
                    StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), response.getRawString());
//                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_login_fail_alert),
//                        Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(App.mContext, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    App.mContext.startActivity(intent);


                }
            });
            LiteHttpManager.getInstance().executeAsync(postRequest);
        }
    }


    /**
     * 正式用户登录，
     * input：userCode  如果为空 则默认去取存在 share_Preferences 的内容
     * password  如果未空 share_Preferences 的内容
     * <p>
     * 登录成功之后  保存登录状态 并且跳转到主页面
     */

    public static void requestLogin(final String user, String password, String type) {


        String password1 = password;
        String user1 = user;
        if (Utils.isStringEmpty(password))
            password1 = SharedPreferencesManager.getString(Constants.SHARED_PREFERENCES_SET,
                    Constants.SHARED_PREFERENCES_LOGIN_PASSWORD, "");
        if (Utils.isStringEmpty(user))
            user1 = SharedPreferencesManager.getString(Constants.SHARED_PREFERENCES_SET,
                    Constants.SHARED_PREFERENCES_LOGIN_ACCOUNT, "");
        // 如果有获取不到的内容
        if (Utils.isStringEmpty(user1)) {
            CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_login_account_empty_alert), Toast.LENGTH_SHORT).show();
            return;
        }
        if (Utils.isStringEmpty(password1)) {
            CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_login_password_empty_alert), Toast.LENGTH_SHORT).show();
            return;
        }
        final String finalAccout = user1;
        final String finalPassword = password1;
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_LOGIN_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new LoginApiParameter(user1, password1, type).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(App.mContext) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                LoginResponse response1 = new LoginResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    StatisticsManager.onErrorInfo(response.getRawString(), "" + s);

                    //
                    CommonToast.getInstance(response1.mMsg).show();
                    // 直接去选择性别页面
                    if (Utils.isStringEmpty(user)) {
                        Intent intent = new Intent(App.mContext, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        App.mContext.startActivity(intent);
                    }
                } else {
                    SharedPreferencesManager.putInt(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_TYPE, 0);

                    UserInfoData.getInstance().insertOrReplace(response1.userInfoItem.uuid, response1.rawData);
                    Logger.d("登录成功 " + UserInfoData.getInstance().getUserInfoItem().mobile);
                    LoginStateManager.saveLoginInfo(finalAccout, finalPassword, response1.userInfoItem.uuid, response1.token);
                    registerUmengPushDeviceToken();
                    CommonToast.getInstance("登录成功").show();
                    SharedPreferencesManager.putString("User", "uuid", response1.userInfoItem.uuid);
                    SharedPreferencesManager.putString("Pass", "communityUuid", response1.userInfoItem.currentCommunityUuid);
                    SharedPreferencesManager.putString("MyAddress", "address", response1.userInfoItem.currentCommunityName);

                    //
                    Intent intent = new Intent(App.mContext, MainActivityNew.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    App.mContext.startActivity(intent);

                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), response.getRawString());
//                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_login_fail_alert),
//                        Toast.LENGTH_SHORT).show();
                // 直接去选择性别页面
                if (Utils.isStringEmpty(user)) {
                    Intent intent = new Intent(App.mContext, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    App.mContext.startActivity(intent);
                }

            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }


    public static void registerUmengPushDeviceToken() {
        String deviceToken = SharedPreferencesManager.getString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_UMENG_DEVICE_TOKEN, "");
        if (!isLogin() || Utils.isStringEmpty(deviceToken)) return;
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_SAVE_PUSH_DEVICE_TOKEN_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new SaveDeviceTokenParameter(deviceToken).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(App.mContext) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                ResponseData response1 = new ResponseData(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }


}
