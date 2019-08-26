package com.ciyuanplus.mobile.module.news.marking;

import android.app.Activity;
import android.content.Intent;

import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.net.parameter.SubmmitPostScoreApiParameter;
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

public class MarkingPresenter implements MarkingContract.Presenter {
    private final MarkingContract.View mView;

    private FreshNewItem mItem;

    @Inject
    public MarkingPresenter(MarkingContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void detachView() {
    }

    @Override
    public void initData(Intent intent) {
        mItem = (FreshNewItem) intent.getSerializableExtra(Constants.INTENT_NEWS_ITEM);
    }

    @Override
    public void submmitMark(String content, String score) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_SUBMMIT_POST_COMMENT_SCORE_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new SubmmitPostScoreApiParameter(mItem.postUuid,
                content, mItem.userUuid, score, mItem.bizType + "").getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                ResponseData response1 = new ResponseData(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_ITEM_BEEN_MARKED, mItem.postUuid));
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY, mItem.postUuid));
                    CommonToast.getInstance("点评成功").show();
                    ((Activity) mView).finish();
                } else {
                    CommonToast.getInstance(response1.mMsg).show();
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
}
