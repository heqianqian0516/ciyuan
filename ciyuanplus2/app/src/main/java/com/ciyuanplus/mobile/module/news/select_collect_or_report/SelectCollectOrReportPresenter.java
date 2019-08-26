package com.ciyuanplus.mobile.module.news.select_collect_or_report;

import android.app.Activity;
import android.content.Intent;

import com.ciyuanplus.mobile.activity.news.ReportPostActivity;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.net.parameter.ItemOperaApiParameter;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import javax.inject.Inject;

/**
 * Created by Alen on 2017/12/11.
 */

public class SelectCollectOrReportPresenter implements SelectCollectOrReportContract.Presenter {
    private final SelectCollectOrReportContract.View mView;

    private FreshNewItem mItem;

    @Inject
    public SelectCollectOrReportPresenter(SelectCollectOrReportContract.View mView) {
        this.mView = mView;

    }

    @Override
    public void detachView() {
    }

    @Override
    public void doCollect() {
        if (mItem.isDislike == 1) cancelCollectPost();
        else collectPost();
    }

    @Override
    public void initData(Intent intent) {
        mItem = (FreshNewItem) intent.getSerializableExtra(Constants.INTENT_NEWS_ITEM);

        mView.updateCollectView(mItem.isDislike == 1);
    }

    // 收藏新鲜事
    private void cancelCollectPost() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_CANCEL_COLLECT);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new ItemOperaApiParameter(mItem.postUuid).getRequestBody());
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
                    CommonToast.getInstance("已取消收藏").show();
                    mItem.isDislike = 0;
                    mView.updateCollectView(false);
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED, mItem.postUuid));
                    ((Activity) mView).finish();
                }
            }

//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance("mmmmmm 操作失败").show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    // 取消收藏新鲜事
    private void collectPost() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_COLLECT);
        postRequest.setHttpBody(new ItemOperaApiParameter(mItem.postUuid).getRequestBody());
        postRequest.setMethod(HttpMethods.Post);
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
                    CommonToast.getInstance("已收藏").show();
                    mItem.isDislike = 1;
                    mItem.dislikeCount++;
                    mView.updateCollectView(true);
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED, mItem.postUuid));
                    ((Activity) mView).finish();
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                CommonToast.getInstance("操作失败").show();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    @Override
    public void goReportActivity() {
        Intent intent = new Intent(mView.getDefaultContext(), ReportPostActivity.class);
        intent.putExtra(Constants.INTENT_ACTIVITY_TYPE, 0);
        intent.putExtra(Constants.INTENT_POST_ID, mItem.postUuid);
        mView.getDefaultContext().startActivity(intent);
    }
}
