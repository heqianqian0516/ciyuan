package com.ciyuanplus.mobile.manager;

import android.content.Intent;
import android.widget.Toast;

import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.CommunityItem;
import com.ciyuanplus.mobile.net.parameter.DeleteCommunityApiParameter;
import com.ciyuanplus.mobile.net.parameter.DeleteDetailAddressApiParameter;
import com.ciyuanplus.mobile.net.parameter.GetCommunityListApiParameter;
import com.ciyuanplus.mobile.net.parameter.SetDefaultCommunityApiParameter;
import com.ciyuanplus.mobile.net.response.GetCommunitListResponse;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * Created by Alen on 2017/3/7.
 * 小区管理
 */

public class CommunityManager {
    private static CommunityManager mCommunityManager;
    private CommunityItem[] mCommunityItems;

    public static CommunityManager getInstance() {
        if (mCommunityManager == null) mCommunityManager = new CommunityManager();
        return mCommunityManager;
    }

    public CommunityItem[] getmCommunityItems() {
        return mCommunityItems;
    }

    public void getCommunityListFromNet() {
        if (!LoginStateManager.isLogin()) {
            return;
        }
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_COMMUNITY_LIST_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new GetCommunityListApiParameter().getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(App.mContext) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                GetCommunitListResponse response1 = new GetCommunitListResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    mCommunityItems = response1.communityListItem.data;
                    for (int i = 0; i < mCommunityItems.length; i++) {
                        if (Utils.isStringEquals(mCommunityItems[i].uuid, UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid)) {
                            UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid = mCommunityItems[i].uuid;
                            UserInfoData.getInstance().getUserInfoItem().currentCommunityName = mCommunityItems[i].commName;
                            // 做个交换 把默认小区放到第一位
                            CommunityItem temp = mCommunityItems[0];
                            mCommunityItems[0] = mCommunityItems[i];
                            mCommunityItems[i] = temp;
                        }
                    }
                    //通知UI改变
                    EventCenterManager.asynSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_COMMUNITY_LIST_FINISH));

                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }

//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_get_community_list_fail_alert), Toast.LENGTH_SHORT).show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }


    // 检查某个小区是否有详细地址
    // 如果有返回true   如果没有返回false
    public boolean checkHasDetailAddress(String selectedCommunityId) {
        return true;// V1.2.0 暂时去掉了 详细地址的功能
//        for(int i = 0; i < mCommunityItems.length; i++) {
//            if(Utils.isStringEquals(mCommunityItems[i].uuid, selectedCommunityId))return mCommunityItems[i].userCommunityAddressResults != null && mCommunityItems[i].userCommunityAddressResults.length > 0;
//        }
//        return false;
    }

    public void deleteCommunity(String addressed) {
        if (!LoginStateManager.isLogin()) {
            return;
        }
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_DELETE_COMMUNITY_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new DeleteCommunityApiParameter(addressed).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(App.mContext) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                ResponseData response1 = new ResponseData(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance("删除成功").show();
                    getCommunityListFromNet();
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_delete_community_fail_alert), Toast.LENGTH_SHORT).show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    public void deleteDetailAddress(String userCommunityAddressUuid) {
        if (!LoginStateManager.isLogin()) {
            return;
        }
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_DELETE_DETAIL_ADDRESS);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new DeleteDetailAddressApiParameter(userCommunityAddressUuid).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(App.mContext) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                ResponseData response1 = new ResponseData(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance("删除成功").show();
                    getCommunityListFromNet();
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_delete_community_fail_alert), Toast.LENGTH_SHORT).show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    public void setDefaultCommunity(final CommunityItem defaultCommunity) {
        if (!LoginStateManager.isLogin()) {
            return;
        }
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_SET_DEFAULT_COMMUNITY_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new SetDefaultCommunityApiParameter(defaultCommunity.uuid).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(App.mContext) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                ResponseData response1 = new ResponseData(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    //通知需要知道的activity 有变化。
                    UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid = defaultCommunity.uuid;
                    UserInfoData.getInstance().getUserInfoItem().currentCommunityName = defaultCommunity.commName;
                    for (int i = 0; i < mCommunityItems.length; i++) {// 默认小区放在第一位
                        if (Utils.isStringEquals(mCommunityItems[i].uuid, UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid)) {
                            CommunityItem temp = mCommunityItems[0];
                            mCommunityItems[0] = mCommunityItems[i];
                            mCommunityItems[i] = temp;
                        }
                    }
                    EventCenterManager.asynSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_DEFAULT_COMMUNITY_FINISH));
                    Intent intent = new Intent("android.intent.action.refresh");
                    intent.putExtra("refresh", "lang");
                    intent.putExtra("keyi", mCommunityItems[0].uuid);
                    LocalBroadcastManager.getInstance(App.mContext).sendBroadcast(intent);

                    Intent intent2 = new Intent("android.intent.action.muhomerefresh");
                    intent2.putExtra("refresh", "lang");
                    intent2.putExtra("keyi", mCommunityItems[0].uuid);
                    LocalBroadcastManager.getInstance(App.mContext).sendBroadcast(intent2);
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }

//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_delete_community_fail_alert), Toast.LENGTH_SHORT).show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }
}
