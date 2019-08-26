package com.ciyuanplus.mobile.module.mine.search_friends;

import android.app.Activity;
import android.content.Intent;

import com.ciyuanplus.mobile.adapter.MyFriendsAdapter;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.module.mine.friends.MyFriendsPresenter;
import com.ciyuanplus.mobile.module.others.new_others.OthersActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.FriendsItem;
import com.ciyuanplus.mobile.net.parameter.FollowOtherApiParameter;
import com.ciyuanplus.mobile.net.parameter.RequestFriendsApiParameter;
import com.ciyuanplus.mobile.net.parameter.UnFollowOtherApiParameter;
import com.ciyuanplus.mobile.net.response.RequestFriendsResponse;
import com.ciyuanplus.mobile.net.response.RequestOtherInfoResponse;
import com.ciyuanplus.mobile.statistics.StatisticsConstant;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

/**
 * Created by Alen on 2017/12/11.
 */

public class SearchFriendsPresenter implements SearchFriendsContract.Presenter {
    public String mPageType;
    private final SearchFriendsContract.View mView;
    private int mNextPage = 0;
    private String mSearchName = "";
    private String mRequestUrl;
    private MyFriendsAdapter mAdapter;
    private final ArrayList<FriendsItem> mList = new ArrayList<>();

    @Inject
    public SearchFriendsPresenter(SearchFriendsContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void initData(Intent intent) {
        mPageType = intent.getStringExtra(Constants.INTENT_MY_FRIENDS_TYPE);
        mRequestUrl = Utils.isStringEquals(MyFriendsPresenter.FOLLOW_TYPE, mPageType) ? ApiContant.REQUEST_FOLLOW_LIST_URL : ApiContant.REQUEST_FAN_LIST_URL;

        mAdapter = new MyFriendsAdapter((Activity) mView, mList, (v) -> {
            if (Utils.isStringEquals(mPageType, MyFriendsPresenter.FOLLOW_TYPE)) {
                StatisticsManager.onEventInfo(StatisticsConstant.MODULE_MINE_FOLLOW_SEARCH, StatisticsConstant.OP_MINE_FOLLOW_SEARCH_LIST_ITEM_CLICK);
            } else {
                StatisticsManager.onEventInfo(StatisticsConstant.MODULE_MINE_FANS_SEARCH, StatisticsConstant.OP_MINE_FANS_SEARCH_LIST_ITEM_CLICK);
            }
            int postion = mView.getListView().getChildLayoutPosition(v) - 2;
            FriendsItem item = mAdapter.getItem(postion);
            Intent intent1 = new Intent(mView.getDefaultContext(), OthersActivity.class);
            intent1.putExtra(Constants.INTENT_USER_ID, item.uuid);
            mView.getDefaultContext().startActivity(intent1);
        });
        mView.getListView().setIAdapter(mAdapter);
    }

    @Override
    public void requestList(boolean reset) {
        if (reset) mNextPage = 0;
        requestFriendsList();
    }

    @Override
    public void doSearch(String name) {
        if (Utils.isStringEquals(name, mSearchName)) {
            return;
        }
        mSearchName = name;
        mNextPage = 0;
        requestFriendsList();
    }

    @Override
    public void handleEvent(EventCenterManager.EventMessage eventMessage) {
        if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE) {
            FriendsItem friendsItem = (FriendsItem) eventMessage.mObject;
            for (int i = 0; i < mList.size(); i++) {
                if (Utils.isStringEquals(friendsItem.uuid, mList.get(i).uuid)) {
                    if (friendsItem.followType == 0) { // 取消关注
                        if (Utils.isStringEquals(MyFriendsPresenter.FOLLOW_TYPE, mPageType))
                            mList.remove(i);
                        else mList.get(i).followType = 0;// 只有被关注
                    } else if (friendsItem.followType == 1) {//关注
                        mList.get(i).followType = 2;// 互相关注
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

        }
    }

    private void requestFriendsList() {

        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + mRequestUrl);
        postRequest.setMethod(HttpMethods.Post);
        int PAGE_SIZE = 20;
        postRequest.setHttpBody(new RequestFriendsApiParameter(mSearchName, mNextPage + "", PAGE_SIZE + "").getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                RequestFriendsResponse response1 = new RequestFriendsResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    if (mNextPage == 0) mList.clear();
                    if (response1.friendsListItem != null && response1.friendsListItem.list.length > 0) {
                        Collections.addAll(mList, response1.friendsListItem.list);
                        mNextPage++;
                    }
                }
                mAdapter.notifyDataSetChanged();
                mView.updateView(mList.size());
            }
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
////                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_get_fridends_error_alert)).show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    // 取消关注该用户
    public void requestUnFollowUser(final FriendsItem item) {
        if (Utils.isStringEquals(mPageType, MyFriendsPresenter.FOLLOW_TYPE)) {
            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_MINE_FOLLOW_SEARCH, StatisticsConstant.OP_MINE_FOLLOW_SEARCH_LIST_ITEM_OPERA_CLICK);
        } else {
            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_MINE_FANS_SEARCH, StatisticsConstant.OP_MINE_FANS_SEARCH_LIST_ITEM_OPERA_CLICK);
        }
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_CANCEL_FOLLOW_POST_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new UnFollowOtherApiParameter(item.uuid).getRequestBody());
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
                    CommonToast.getInstance("取消关注成功").show();
                    if (Utils.isStringEquals(MyFriendsPresenter.FOLLOW_TYPE, mPageType))
                        mList.remove(item);
                    mAdapter.notifyDataSetChanged();

                    // 更新其他页面
                    FriendsItem friendsItem = new FriendsItem();
                    friendsItem.uuid = item.uuid;
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

    public void requestFollowUser(final FriendsItem item) {
        if (Utils.isStringEquals(mPageType, MyFriendsPresenter.FOLLOW_TYPE)) {
            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_MINE_FOLLOW_SEARCH, StatisticsConstant.OP_MINE_FOLLOW_SEARCH_LIST_ITEM_OPERA_CLICK);
        } else {
            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_MINE_FANS_SEARCH, StatisticsConstant.OP_MINE_FANS_SEARCH_LIST_ITEM_OPERA_CLICK);
        }
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_FOLLOW_USER_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new FollowOtherApiParameter(item.uuid).getRequestBody());
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
                    friendsItem.uuid = item.uuid;
                    friendsItem.followType = 1;
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, friendsItem));

                    item.followType = 2;
                    mAdapter.notifyDataSetChanged();
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

    @Override
    public void detachView() {
    }
}
