package com.ciyuanplus.mobile.module.others.new_others;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.news.FullScreenImageActivity;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.FriendsItem;
import com.ciyuanplus.mobile.net.bean.UserInfoItem;
import com.ciyuanplus.mobile.net.parameter.DefaultParameter;
import com.ciyuanplus.mobile.net.parameter.FollowOtherApiParameter;
import com.ciyuanplus.mobile.net.parameter.RequestOthersInfoApiParameter;
import com.ciyuanplus.mobile.net.parameter.UnFollowOtherApiParameter;
import com.ciyuanplus.mobile.net.response.RequestOtherInfoResponse;
import com.ciyuanplus.mobile.net.response.SocialCountResponse;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.CustomDialog;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;
import com.orhanobut.logger.Logger;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * Created by Alen on 2017/12/11.
 */

public class OthersPresenter implements OthersContract.Presenter {
    public String mUserUuid;
    public UserInfoItem mUserInfo;

    private final OthersContract.View mView;

    @Inject
    public OthersPresenter(OthersContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void requestList(boolean reset) {

    }


    @Override
    public void initData(Intent intents) {

        mUserUuid = intents.getStringExtra(Constants.INTENT_USER_ID);
        mView.switchTabSelect(0);

        requestPersonInfo();
        requestCount();
    }

    @Override
    public void handleEvent(EventCenterManager.EventMessage eventMessage) {
        if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE) {
            if (mUserInfo == null) return;
            FriendsItem friendsItem = (FriendsItem) eventMessage.mObject;
            if (Utils.isStringEquals(mUserUuid, friendsItem.uuid))
                mUserInfo.isFollow = friendsItem.followType;
            requestPersonInfo();
            requestCount();
//            mView.updateView();
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_LOGIN_USER_INFO_UPDATE) {
            requestPersonInfo();
            requestCount();
//                mView.updateView();
        }
    }

    @Override
    public void handleClick(int id) {
        if (id == R.id.tv_add) {// 关注

            if (Utils.isStringEquals(mUserInfo.uuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {
                return;
            }

            if (!LoginStateManager.isLogin()) {
                Intent intent = new Intent(mView.getDefaultContext(), LoginActivity.class);
                mView.getDefaultContext().startActivity(intent);
                return;
            }
            if (mUserInfo.isFollow == 0) {
                requestFollowUser();
            } else {
                CustomDialog.Builder builder = new CustomDialog.Builder(mView.getDefaultContext());
                builder.setMessage("确定要取消关注吗？");
                builder.setPositiveButton("确定", (dialog, which) -> {
                    requestUnFollowUser();
                    dialog.dismiss();
                });
                builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
                CustomDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }

        } else if (id == R.id.iv_head) { // 头像点击看大图
            if (mUserInfo == null) return;
            String[] images = new String[1];
            images[0] = mUserInfo.photo;
            Intent intent = new Intent(App.mContext, FullScreenImageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle b = new Bundle();
            b.putStringArray(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_IMAGES, images);
            intent.putExtras(b);
            intent.putExtra(Constants.INTENT_FULL_SCREEN_IMAGE_VIEWER_INDEX, 0);
            App.mContext.startActivity(intent);
        }
    }

    // 取消关注该用户
    private void requestUnFollowUser() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_CANCEL_FOLLOW_POST_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new UnFollowOtherApiParameter(mUserInfo.uuid).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                ResponseData response1 = new ResponseData(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    mUserInfo.isFollow = 0;
                    mView.updateView();

                    // 更新其他页面
                    FriendsItem friendsItem = new FriendsItem();
                    friendsItem.uuid = mUserInfo.uuid;
                    friendsItem.followType = 0;
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, friendsItem));
                }
            }
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_get_fresh_news_fail_alert),
//                        Toast.LENGTH_SHORT).show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    // 关注该用户
    private void requestFollowUser() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_FOLLOW_USER_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new FollowOtherApiParameter(mUserUuid).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                RequestOtherInfoResponse response1 = new RequestOtherInfoResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    CommonToast.getInstance("关注成功").show();
                    // 更新其他页面
                    FriendsItem friendsItem = new FriendsItem();
                    friendsItem.uuid = mUserInfo.uuid;
                    friendsItem.followType = 1;
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, friendsItem));


                    mUserInfo.isFollow = 1;// 手动设置为已经关注，不在请求接口
                    mView.updateView();
                }
            }
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_get_fresh_news_fail_alert),
//                        Toast.LENGTH_SHORT).show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    private void requestPersonInfo() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_GET_OTHER_INFO_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestOthersInfoApiParameter(mUserUuid).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                RequestOtherInfoResponse response1 = new RequestOtherInfoResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    mUserInfo = response1.userInfoItem;
                    mView.updateView();
                }
            }

//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_get_fresh_news_fail_alert),
//                        Toast.LENGTH_SHORT).show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }


    private void requestCount() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_USER_SOCIAL_COUNT);

        HashMap<String, String> map = new HashMap<>(1);
        map.put("userUuid", mUserUuid);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new DefaultParameter(map).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);

                Logger.d(s);
                SocialCountResponse countResponse = new SocialCountResponse(s);
                if (Utils.isStringEquals(countResponse.mCode, ResponseData.CODE_OK)) {

                    Logger.d(countResponse.mSocialCountItem.isFans() + "");
                    mView.updateInfo(countResponse.mSocialCountItem);

                } else {
                    CommonToast.getInstance(countResponse.mMsg).show();
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_get_fresh_news_fail_alert),
                        Toast.LENGTH_SHORT).show();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }


    @Override
    public void detachView() {
    }
}
