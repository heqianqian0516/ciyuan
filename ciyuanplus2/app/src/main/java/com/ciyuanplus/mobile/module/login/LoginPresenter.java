package com.ciyuanplus.mobile.module.login;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.blankj.utilcode.util.StringUtils;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.MainActivityNew;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.bind_phone.LoginBindActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.parameter.RequestOtherLoginApiParameter;
import com.ciyuanplus.mobile.net.parameter.SendSmsCodeApiParameter;
import com.ciyuanplus.mobile.net.response.LoginResponse;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

/**
 * @author Alen
 * @date 2017/12/26
 */

public class LoginPresenter implements LoginContract.Presenter {
    private final LoginContract.View mView;

    private int mType;
    private String mOtherPlatformId;

    private String mOtherPlatName;
    private String mOtherPlatHeadUrl;


    private final UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
            Logger.d(platform.toString());
        }


        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

            Logger.d(data.toString());
            // 授权成功之后先去获取头像并且上传到服务器

            CommonToast.getInstance("授权成功", Toast.LENGTH_SHORT).show();

            Set<Map.Entry<String, String>> entries = data.entrySet();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < entries.size(); i++) {
                for (Map.Entry<String, String> next : entries) {
                    sb.append(next.getKey()).append("  ,  ").append(next.getValue()).append("\n");
                }
            }

            Logger.d(sb.toString());

            if (platform == SHARE_MEDIA.WEIXIN) {
                mType = 1;
                mOtherPlatformId = data.get("unionid");
                mOtherPlatName = data.get("screen_name");
                mOtherPlatHeadUrl = data.get("iconurl");
//                downloadHeadIcon(data.get("iconurl"));
            } else if (platform == SHARE_MEDIA.QQ) {
                mType = 2;
                mOtherPlatformId = data.get("openid");
                mOtherPlatName = data.get("screen_name");
                mOtherPlatHeadUrl = data.get("iconurl");
//                downloadHeadIcon(data.get("iconurl"));

            } else if (platform == SHARE_MEDIA.SINA) {
                mType = 3;
                mOtherPlatformId = data.get("id");
                mOtherPlatName = data.get("screen_name");
                mOtherPlatHeadUrl = data.get("iconurl");
//                downloadHeadIcon(data.get("iconurl"));
            }

            requestLoginOtherPlatform();

        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            mView.dismissDialog();
            CommonToast.getInstance("授权失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Logger.d(platform.toString());
            mView.dismissDialog();
            CommonToast.getInstance("已取消授权", Toast.LENGTH_SHORT).show();
        }
    };

    @Inject
    public LoginPresenter(LoginContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void requestLogin(String account, String password) {
        LoginStateManager.requestLogin(account, password, SendSmsCodeApiParameter.SEND_SMS_TYPE_LOGIN);
    }


    @Override
    public void detachView() {

    }

    @Override
    public void requestWeiChatLogin() {
        mView.showLoadingDialog();
        UMShareAPI.get(mView.getDefaultContext()).getPlatformInfo((Activity) mView, SHARE_MEDIA.WEIXIN, umAuthListener);
    }

    @Override
    public void requestQQLogin() {
        mView.showLoadingDialog();
        UMShareAPI.get(mView.getDefaultContext()).getPlatformInfo((Activity) mView, SHARE_MEDIA.QQ, umAuthListener);
    }

    @Override
    public void requestWeiBoLogin() {
        mView.showLoadingDialog();
        UMShareAPI.get(mView.getDefaultContext()).getPlatformInfo((Activity) mView, SHARE_MEDIA.SINA, umAuthListener);
    }

    // 请求后台接口  第三方登录
    private void requestLoginOtherPlatform() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_OTHER_LOGIN_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestOtherLoginApiParameter(mOtherPlatformId, mOtherPlatName, mType + "", mOtherPlatHeadUrl).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                mView.dismissDialog();
                LoginResponse response1 = new LoginResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    SharedPreferencesManager.putInt(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_TYPE, mType);
                    SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_OTHER_PLATRORM_ACCOUNT, mOtherPlatformId);
                    SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, response1.token);

                    UserInfoData.getInstance().insertOrReplace(response1.userInfoItem.uuid, response1.rawData);
                    LoginStateManager.saveLoginInfo(response1.userInfoItem.uuid, "", response1.userInfoItem.uuid, response1.token);
                    LoginStateManager.registerUmengPushDeviceToken();

                    if (StringUtils.isEmpty(response1.userInfoItem.mobile)) {

                        Intent intent = new Intent(App.mContext, LoginBindActivity.class);
                        intent.putExtra(Constants.INTENT_BIND_MOBILE, 1);
                        intent.putExtra(Constants.SHARED_PREFERENCES_LOGIN_TYPE, mType);
                        intent.putExtra(Constants.SHARED_PREFERENCES_OTHER_PLATRORM_ACCOUNT, mOtherPlatformId);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        App.mContext.startActivity(intent);
                    } else {

                        CommonToast.getInstance("登录成功").show();
                        SharedPreferencesManager.putString("User", "uuid", response1.userInfoItem.uuid);
                        SharedPreferencesManager.putString("Pass", "communityUuid", response1.userInfoItem.currentCommunityUuid);
                        SharedPreferencesManager.putString("ShaZi", "tok", response1.token);
                        SharedPreferencesManager.putString("MyAddress", "address", response1.userInfoItem.currentCommunityName);

                        Intent intent = new Intent(App.mContext, MainActivityNew.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        App.mContext.startActivity(intent);
                    }

                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                mView.dismissDialog();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }


    @Override
    public void sendCode(String accout) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_SEND_SMS_CODE_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new SendSmsCodeApiParameter(accout, SendSmsCodeApiParameter.SEND_SMS_TYPE_LOGIN).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                ResponseData response1 = new ResponseData(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    CommonToast.getInstance(mView.getDefaultContext().getResources().getString(R.string.string_send_verify_code_success_alert)).show();
                    mView.startCount();
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                CommonToast.getInstance(response.getRawString(), Toast.LENGTH_SHORT).show();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }
}
