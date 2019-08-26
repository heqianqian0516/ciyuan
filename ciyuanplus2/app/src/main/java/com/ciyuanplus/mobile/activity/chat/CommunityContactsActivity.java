package com.ciyuanplus.mobile.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.adapter.CommunityUserAdapter;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.others.new_others.OthersActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.CommunityUserItem;
import com.ciyuanplus.mobile.net.bean.FriendsItem;
import com.ciyuanplus.mobile.net.parameter.FollowOtherApiParameter;
import com.ciyuanplus.mobile.net.parameter.GetCommunityUsersListApiParameter;
import com.ciyuanplus.mobile.net.parameter.UnFollowOtherApiParameter;
import com.ciyuanplus.mobile.net.response.CommunityUserListResponse;
import com.ciyuanplus.mobile.net.response.RequestOtherInfoResponse;
import com.ciyuanplus.mobile.statistics.StatisticsConstant;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonTitleBar;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.SideBar;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/6/23.
 */

public class CommunityContactsActivity extends MyBaseActivity implements EventCenterManager.OnHandleEventListener {
    private String search;
    @BindView(R.id.m_community_contacts_list)
    RecyclerView mCommunityContactsList;
    @BindView(R.id.m_community_contacts_sidebar)
    SideBar mCommunityContactsSidebar;
    @BindView(R.id.m_community_contacts_null_lp)
    LinearLayout mCommunityContactsNullLp;
    @BindView(R.id.m_community_contacts_common_title)
    CommonTitleBar m_js_common_title;
    private CommunityUserItem[] mAllList;
    private final ArrayList<CommunityUserItem> mList = new ArrayList<>();
    private CommunityUserAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_community_contacts);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();
        requestCommunityContacts();
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);

        m_js_common_title.setCenterText("小区通讯录");
        m_js_common_title.setRightImage(R.mipmap.nav_icon_search);
        m_js_common_title.setLeftClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {
                onBackPressed();
            }
        });
        m_js_common_title.setRightClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View view) {
                Intent intent = new Intent(CommunityContactsActivity.this, SearchCommunityContactsActivity.class);
                startActivity(intent);
            }
        });

        mAdapter = new CommunityUserAdapter(this, mList, (v) -> {
            int position = mCommunityContactsList.getChildAdapterPosition(v);
            StatisticsManager.onEventInfo("CommunityContactsActivity", "onItemClick", mAdapter.getItem(position).uuid);
            CommunityUserItem item = mAdapter.getItem(position);
            Intent intent = new Intent(CommunityContactsActivity.this, OthersActivity.class);
            intent.putExtra(Constants.INTENT_USER_ID, item.uuid);
            CommunityContactsActivity.this.startActivity(intent);
        });
        mCommunityContactsList.setLayoutManager(new LinearLayoutManager(this));
        mCommunityContactsList.setAdapter(mAdapter);


        mCommunityContactsSidebar.setOnTouchingLetterChangedListener((s) -> {
            int pos = mAdapter.getPositionForSection(s.charAt(0));
            if (pos != -1) {
                mCommunityContactsList.scrollToPosition(pos);
            }
        });

        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, this);

    }


    // 获取联系人
    private void requestCommunityContacts() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_COMMUNITY_USERS_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new GetCommunityUsersListApiParameter().getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(this) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                CommunityUserListResponse response1 = new CommunityUserListResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK) && response1.communityUserListInfo.data != null) {
                    mAllList = response1.communityUserListInfo.data;
                    formatListData();
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    // 格式化 并且过滤数据
    private void formatListData() {
        mList.clear();
        ArrayList<String> str = new ArrayList<>();
        if (Utils.isStringEmpty(search)) {
            for (CommunityUserItem aMAllList : mAllList) {
                if (Utils.isStringEquals(aMAllList.uuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {
                    continue;
                }
                mList.add(aMAllList);
                if (!Utils.isStringEmpty(aMAllList.py)) {
                    String chars = aMAllList.py.toUpperCase();
                    if (!str.contains(chars)) str.add(chars);
                }
            }
            Collections.sort(str);
//            str.add("#");
            mCommunityContactsSidebar.setBarList(str.toArray(new String[0]));

            mCommunityContactsSidebar.setVisibility(View.VISIBLE);
        } else {
            for (CommunityUserItem aMAllList : mAllList) {
                if (aMAllList.nickname.contains(search)) {
                    mList.add(aMAllList);
                }
            }
            mCommunityContactsSidebar.setVisibility(View.GONE);
        }
        if (mList.size() > 0) {
            mCommunityContactsNullLp.setVisibility(View.GONE);
            this.mCommunityContactsList.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        } else {
            mCommunityContactsNullLp.setVisibility(View.VISIBLE);
            this.mCommunityContactsList.setVisibility(View.GONE);
        }
    }


    // 关注该用户
    public void requestFollowUser(final CommunityUserItem item) {
        StatisticsManager.onEventInfo(StatisticsConstant.MODULE_COMMUNITY_CONTACTS, StatisticsConstant.OP_COMMUNITY_CONTACTS_LIST_ITEM_OPERA_CLICK);

        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_FOLLOW_USER_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new FollowOtherApiParameter(item.uuid).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(this) {
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

                    requestCommunityContacts();// 刷新列表
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

    // 取消关注该用户
    public void requestUnFollowUser(final CommunityUserItem item) {
        StatisticsManager.onEventInfo(StatisticsConstant.MODULE_COMMUNITY_CONTACTS, StatisticsConstant.OP_COMMUNITY_CONTACTS_LIST_ITEM_OPERA_CLICK);

        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_CANCEL_FOLLOW_POST_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new UnFollowOtherApiParameter(item.uuid).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(this) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                ResponseData response1 = new ResponseData(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    CommonToast.getInstance("取消关注成功").show();
                    requestCommunityContacts();// 刷新列表

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, this);
    }

    @Override
    public void onHandleEvent(EventCenterManager.EventMessage eventMessage) {
        if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE) {
            FriendsItem friendsItem = (FriendsItem) eventMessage.mObject;
            for (int i = 0; i < mList.size(); i++) {
                if (Utils.isStringEquals(friendsItem.uuid, mList.get(i).uuid)) {
                    if (friendsItem.followType == 0) { // 取消关注
                        if (mList.get(i).isFollow == 2) mList.get(i).isFollow = 3;
                        else if (mList.get(i).isFollow == 1) mList.get(i).isFollow = 0;
                    } else if (friendsItem.followType == 1) {//关注
                        if (mList.get(i).isFollow == 0) mList.get(i).isFollow = 1;
                        else if (mList.get(i).isFollow == 3) mList.get(i).isFollow = 2;
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
