package com.ciyuanplus.mobile.module.mine.mine;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.news.FullScreenImageActivity;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.UserInfoItem;
import com.ciyuanplus.mobile.net.parameter.RequestOthersInfoApiParameter;
import com.ciyuanplus.mobile.net.parameter.RequestPostDetailApiParameter;
import com.ciyuanplus.mobile.net.response.RequestOtherInfoResponse;
import com.ciyuanplus.mobile.net.response.SocialCountResponse;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import static com.ciyuanplus.mobile.module.mine.mine.MineContract.Presenter;
import static com.ciyuanplus.mobile.module.mine.mine.MineContract.View;

/**
 * Created by Alen on 2017/12/11.
 */

public class MinePresenter implements Presenter {

    public String mUserUuid;
    public UserInfoItem mUserInfo;
    private MineContract.View mView;


    @Inject
    public MinePresenter(View mView) {
        this.mView = mView;
        mUserInfo = UserInfoData.getInstance().getUserInfoItem();
        mUserUuid = mUserInfo.uuid;

//        requestPersonInfo();
    }


    @Override
    public void handleEvent(EventCenterManager.EventMessage eventMessage) {
        if (eventMessage.mEvent == Constants.EVENT_MESSAGE_LOGIN_USER_INFO_UPDATE
                || eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE) {
            requestPersonInfo();
            requestCount();
        }
    }


    @Override
    public void handleClick(int id) {
        if (id == R.id.iv_head) { // 头像点击看大图
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


    @Override
    public void requestPersonInfo() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + "/api/user/detail");
        postRequest.setMethod(HttpMethods.Post);
        String uuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        postRequest.setHttpBody(new RequestOthersInfoApiParameter(uuid).getRequestBody());
        String authToken = SharedPreferencesManager.getString(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");

        postRequest.addHeader("authToken", authToken);
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                RequestOtherInfoResponse response1 = new RequestOtherInfoResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    mUserInfo = response1.userInfoItem;
                    UserInfoData.getInstance().insertOrReplace(response1.userInfoItem.uuid, response1.rawData);
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

    @Override
    public void requestCount() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_USER_SOCIAL_COUNT);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestPostDetailApiParameter(mUserUuid).getRequestBody());
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
