package com.ciyuanplus.mobile.activity.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.adapter.CommunityUserAdapter;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
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
import com.ciyuanplus.mobile.widget.ClearEditText;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/7/6.
 */

public class SearchCommunityContactsActivity extends MyBaseActivity implements EventCenterManager.OnHandleEventListener {
    private String search;
    @BindView(R.id.m_search_community_contacts_cancel)
    TextView mSearchCommunityContactsCancel;
    @BindView(R.id.m_search_community_contacts_search_edit)
    ClearEditText mSearchCommunityContactsSearchEdit;
    @BindView(R.id.m_search_community_contacts_top_lp)
    RelativeLayout mSearchCommunityContactsTopLp;
    @BindView(R.id.m_search_community_contacts_list)
    RecyclerView mSearchCommunityContactsList;
    @BindView(R.id.m_imageView)
    ImageView mImageView;
    @BindView(R.id.m_search_community_contacts_null_lp)
    LinearLayout mSearchCommunityContactsNullLp;
    private CommunityUserItem[] mAllList;
    private final ArrayList<CommunityUserItem> mList = new ArrayList<>();
    private CommunityUserAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_search_community_contacts);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();
        requestCommunityContacts();

    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);

        mAdapter = new CommunityUserAdapter(this, mList, (v) -> {
            int position = mSearchCommunityContactsList.getChildAdapterPosition(v);
            StatisticsManager.onEventInfo("SearchCommunityContactsActivity", "onItemClick", mAdapter.getItem(position).uuid);
            CommunityUserItem item = mAdapter.getItem(position);
            Intent intent = new Intent(SearchCommunityContactsActivity.this, OthersActivity.class);
            intent.putExtra(Constants.INTENT_USER_ID, item.uuid);
            SearchCommunityContactsActivity.this.startActivity(intent);
        });
        mSearchCommunityContactsList.setLayoutManager(new LinearLayoutManager(this));
        mSearchCommunityContactsList.setAdapter(mAdapter);

        mSearchCommunityContactsSearchEdit.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                StatisticsManager.onEventInfo(StatisticsConstant.MODULE_COMMUNITY_CONTACTS, StatisticsConstant.OP_COMMUNITY_CONTACTS_SEARCH_FINISH_CLICK);
                HideKeyboard(mSearchCommunityContactsSearchEdit);
                search = mSearchCommunityContactsSearchEdit.getText().toString();
                if (!Utils.isStringEmpty(search)) {
                    formatListData();
                }
                return true;
            }
            return false;
        });

        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, this);

    }

    // 获取联系人
    private void requestCommunityContacts() {
        StatisticsManager.onEventInfo(StatisticsConstant.MODULE_COMMUNITY_CONTACTS, StatisticsConstant.OP_COMMUNITY_CONTACTS_SEARCH_LIST_ITEM_OPERA_CLICK);

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
                    //formatListData();
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }


    // 取消关注该用户
    public void requestUnFollowUser(final CommunityUserItem item) {
        StatisticsManager.onEventInfo(StatisticsConstant.MODULE_COMMUNITY_CONTACTS, StatisticsConstant.OP_COMMUNITY_CONTACTS_SEARCH_LIST_ITEM_OPERA_CLICK);

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

    // 格式化 并且过滤数据
    private void formatListData() {
        if (mAllList == null) return;
        mList.clear();
        ArrayList<String> str = new ArrayList<>();
        if (Utils.isStringEmpty(search)) {
            mList.clear();
        } else {
            for (CommunityUserItem aMAllList : mAllList) {
                if (Utils.isStringEquals(aMAllList.uuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {
                    continue;
                }
                if (aMAllList.nickname.contains(search)) {
                    mList.add(aMAllList);
                }
            }
        }
        if (mList.size() > 0) {
            mSearchCommunityContactsNullLp.setVisibility(View.GONE);
            this.mSearchCommunityContactsList.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        } else {
            CommonToast.getInstance("未搜索到该用户").show();
            mSearchCommunityContactsNullLp.setVisibility(View.VISIBLE);
            this.mSearchCommunityContactsList.setVisibility(View.GONE);
        }
    }

    //隐藏虚拟键盘
    private void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

        }
    }


    // 关注该用户
    public void requestFollowUser(final CommunityUserItem item) {
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

    @OnClick(R.id.m_search_community_contacts_cancel)
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        onBackPressed();
    }
}
