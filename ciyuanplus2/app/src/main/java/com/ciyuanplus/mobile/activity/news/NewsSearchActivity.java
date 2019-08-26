package com.ciyuanplus.mobile.activity.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ciyuanplus.base.irecyclerview.LoadMoreFooterView;
import com.ciyuanplus.base.irecyclerview.OnRefreshListener;
import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.adapter.SquareAdapter;
import com.ciyuanplus.mobile.adapter.WorldUserAdapter;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.forum_detail.food_detail.FoodDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.note_detail.NoteDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.post_detail.PostDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.stuff_detail.StuffDetailActivity;
import com.ciyuanplus.mobile.module.others.new_others.OthersActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.DeleteCount;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.net.bean.FriendsItem;
import com.ciyuanplus.mobile.net.bean.WorldUserItem;
import com.ciyuanplus.mobile.net.parameter.RequestCommunityPostsApiParameter;
import com.ciyuanplus.mobile.net.parameter.RequestSearchAllUserApiParameter;
import com.ciyuanplus.mobile.net.parameter.RequestWorldPostsApiParameter;
import com.ciyuanplus.mobile.net.response.RequestFreshNewsResponse;
import com.ciyuanplus.mobile.net.response.SearchUserResponse;
import com.ciyuanplus.mobile.statistics.StatisticsConstant;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.DisplayUtils;
import com.ciyuanplus.mobile.utils.RecyclerViewItem;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.ClearEditText;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/7/6.
 */

public class NewsSearchActivity extends MyBaseActivity implements EventCenterManager.OnHandleEventListener,
        OnRefreshListener, com.ciyuanplus.base.irecyclerview.OnLoadMoreListener,
        OnLoadMoreListener, com.scwang.smartrefresh.layout.listener.OnRefreshListener {
    public String mSearchValue = "";
    @BindView(R.id.m_search_news_cancel)
    TextView mSearchNewsCancel;
    @BindView(R.id.m_search_news_search_edit)
    ClearEditText mSearchNewsSearchEdit;
    @BindView(R.id.m_search_news_search_drop_img)
    ImageView mSearchNewsSearchDropImg;
    @BindView(R.id.m_search_news_top_lp)
    RelativeLayout mSearchNewsTopLp;
    @BindView(R.id.m_search_news_list)
    RecyclerView mSearchNewsList;
    @BindView(R.id.m_imageView)
    ImageView mImageView;
    @BindView(R.id.m_search_news_null_lp)
    LinearLayout mSearchNewsNullLp;
    @BindView(R.id.m_search_news_contacts_null_lp)
    LinearLayout mSearchNewsContactsNullLp;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private LoadMoreFooterView loadMoreFooterView;

    private final int PAGE_SIZE = 20;
    private int mNextPage;
    private int mSearchType = 0;
    private String mLastId = "";

    private final ArrayList<FreshNewItem> mNewsList = new ArrayList<>();
    private final ArrayList<WorldUserItem> mUserList = new ArrayList<>();


    private SquareAdapter mNewsAdapter;
    private WorldUserAdapter mWorldUserAdapter;
    private PopupWindow popupWindow;


    //显示虚拟键盘
    private static void ShowKeyboard(View v) {
        v.requestFocus();
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_news_search);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        //mSearchType = getIntent().getIntExtra(Constants.INTENT_SEARCH_NEWS_TYPE, 0);
        if (!SharedPreferencesManager.getBoolean(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_HAS_OPEM_SEARCH_VIEW, false)) {
            Intent intent = new Intent(this, NewsSearchPopUpActivity.class);
            intent.putExtra(Constants.INTENT_SEARCH_NEWS_TYPE, mSearchType);
            startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_SEARCH_TYPE);
        }


//        D
        this.initView();
        ShowKeyboard(mSearchNewsSearchEdit);
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);


        mNewsAdapter = new SquareAdapter(this, mNewsList, (v) -> {
            int postion = mSearchNewsList.getChildAdapterPosition(v);
            FreshNewItem item = mNewsAdapter.getItem(postion);
            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_SEARCH_NEWS, StatisticsConstant.OP_SEARCH_NEWS_ITEM_ENTER_CLICK, item.postUuid);
            if (item.bizType == 1) {//宝贝
                Intent intent = new Intent(this, StuffDetailActivity.class);
                intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
                startActivity(intent);
            } else if (item.bizType == 4) { // 长文和说说

                Intent intent = new Intent(this, PostDetailActivity.class);
                intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
                startActivity(intent);

            } else if (item.bizType == FreshNewItem.FRESH_ITEM_NEWS
                    || item.bizType == FreshNewItem.FRESH_ITEM_NOTE
                    || item.bizType == FreshNewItem.FRESH_ITEM_NEWS_COLLECTION
                    || item.bizType == 0
            ) {
                Intent intent = new Intent(this, NoteDetailActivity.class);
                intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
                startActivity(intent);
            } else if (item.bizType == FreshNewItem.FRESH_ITEM_FOOD
                    || item.bizType == FreshNewItem.FRESH_ITEM_LIVE
                    || item.bizType == FreshNewItem.FRESH_ITEM_COMMENT) {
                Intent intent = new Intent(this, FoodDetailActivity.class);
                intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
                startActivity(intent);
            } else if (item.bizType == FreshNewItem.FRESH_ITEM_ANSWER) {

                Intent intent = new Intent(this, PostDetailActivity.class); //问答
                intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
                intent.putExtra(Constants.INTENT_BIZE_TYPE, item.bizType);
                startActivity(intent);
            }
        });
        mWorldUserAdapter = new WorldUserAdapter(this, mUserList, (v) -> {
            int postion = mSearchNewsList.getChildAdapterPosition(v);
            WorldUserItem item = mWorldUserAdapter.getItem(postion);
            if (Utils.isStringEquals(item.uuid, UserInfoData.getInstance().getUserInfoItem().uuid))
                return;

            Intent intent = new Intent(NewsSearchActivity.this, OthersActivity.class);
            intent.putExtra(Constants.INTENT_USER_ID, item.uuid);
            NewsSearchActivity.this.startActivity(intent);
        });
        mSearchNewsList.setAdapter(mNewsAdapter);
//        loadMoreFooterView = (LoadMoreFooterView) mSearchNewsList.getLoadMoreFooterView();
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this) {//解决显示不全的问题
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        linearLayoutManager1.setOrientation(RecyclerView.VERTICAL);
        mSearchNewsList.setLayoutManager(linearLayoutManager1);

        mSearchNewsList.addItemDecoration(new DisplayUtils.SpacesItemDecoration());
        mSearchNewsList.addItemDecoration(new RecyclerViewItem(this, DividerItemDecoration.VERTICAL, 1));

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
//        mSearchNewsList.setOnRefreshListener(this);
//        mSearchNewsList.setOnLoadMoreListener(this);

        mSearchNewsSearchEdit.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                StatisticsManager.onEventInfo(StatisticsConstant.MODULE_SEARCH_NEWS, StatisticsConstant.OP_SEARCH_NEWS_SEARCH);
                HideKeyboard(mSearchNewsSearchEdit);
                mSearchValue = mSearchNewsSearchEdit.getText().toString();
                if (!Utils.isStringEmpty(mSearchValue)) {
                    mNextPage = 0;
                    mLastId = "";
                    mNewsList.clear();
                    mUserList.clear();
                    doSearchQuery();
                }
                return true;
            }
            return false;
        });

        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED, this);
    }

    private void doSearchQuery() {
        mSearchValue = mSearchNewsSearchEdit.getText().toString();
        if (Utils.isStringEmpty(mSearchValue)) return;
        if (mSearchType == 0) {
            requestWorldPost();
        } else if (mSearchType == 1) {
            requestZonePost();
        } else if (mSearchType == 2) {
            requestSearchUser();
        }
    }

    //隐藏虚拟键盘
    private void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_SELECT_SEARCH_TYPE && resultCode == RESULT_OK && data != null) {
            // 如果选择搜索某个内容
            mSearchType = data.getIntExtra(NewsSearchPopUpActivity.SELECTED, 0);
            if (mSearchType == 0) {
                StatisticsManager.onEventInfo(StatisticsConstant.MODULE_SEARCH_NEWS, "search_community_world_click");

                mSearchNewsSearchEdit.setHint(R.string.string_main_search_all_alert);
                mSearchNewsList.setAdapter(mNewsAdapter);
            } else if (mSearchType == 1) {
                StatisticsManager.onEventInfo(StatisticsConstant.MODULE_SEARCH_NEWS, "search_community_zone_click");

                mSearchNewsSearchEdit.setHint(R.string.string_main_search_zone_alert);
                mSearchNewsList.setAdapter(mNewsAdapter);
            } else if (mSearchType == 2) {
                StatisticsManager.onEventInfo(StatisticsConstant.MODULE_SEARCH_NEWS, "search_community_user_click");

                mSearchNewsSearchEdit.setHint(R.string.string_main_search_user_alert);
                mSearchNewsList.setAdapter(mWorldUserAdapter);
            }
            mNextPage = 0;
            mLastId = "";
            mNewsList.clear();
            mUserList.clear();
            doSearchQuery();
        }
    }

    private void requestZonePost() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_plot);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestCommunityPostsApiParameter(UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid,
                mSearchValue, mNextPage + "", PAGE_SIZE + "", mLastId, "").getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(this) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                // 必须要加的
//                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
//                mSearchNewsList.setRefreshing(false);
                finishRefreshAndLoadMore();

                RequestFreshNewsResponse response1 = new RequestFreshNewsResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else if (response1.freshNewsListItem.list != null && response1.freshNewsListItem.list.length > 0) {
                    if (mNextPage == 0) mNewsList.clear();
                    Collections.addAll(mNewsList, response1.freshNewsListItem.list);
                    mLastId = mNewsList.get(mNewsList.size() - 1).id;
                    mNextPage++;
                }
                mNewsAdapter.notifyDataSetChanged();
                updateList();
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                // 必须要加的
//                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
//                mSearchNewsList.setRefreshing(false);
                finishRefreshAndLoadMore();
                super.onFailure(e, response);
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    private void requestWorldPost() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_all);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestWorldPostsApiParameter(mSearchValue, mNextPage + "", PAGE_SIZE + "", mLastId, "", "").getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(this) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                // 必须要加的
//                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
//                mSearchNewsList.setRefreshing(false);
                finishRefreshAndLoadMore();

                RequestFreshNewsResponse response1 = new RequestFreshNewsResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else if (response1.freshNewsListItem.list != null && response1.freshNewsListItem.list.length > 0) {
                    if (mNextPage == 0) mNewsList.clear();
                    Collections.addAll(mNewsList, response1.freshNewsListItem.list);
                    mLastId = mNewsList.get(mNewsList.size() - 1).id;
                    mNextPage++;
                }
                mNewsAdapter.notifyDataSetChanged();
                updateList();
            }


            @Override
            public void onFailure(HttpException e, Response<String> response) {
                // 必须要加的
//                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
//                mSearchNewsList.setRefreshing(false);
                finishRefreshAndLoadMore();
                super.onFailure(e, response);

            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    private void requestSearchUser() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_SEARCH_ALL_USER);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestSearchAllUserApiParameter(mSearchValue, mNextPage + "", PAGE_SIZE + "").getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(this) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                // 必须要加的
//                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
//                mSearchNewsList.setRefreshing(false);
                finishRefreshAndLoadMore();

                SearchUserResponse response1 = new SearchUserResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else if (response1.worldUserListInfo.list != null && response1.worldUserListInfo.list.length > 0) {
                    if (mNextPage == 0) mNewsList.clear();
                    Collections.addAll(mUserList, response1.worldUserListInfo.list);
                    mNextPage++;
                }
                mWorldUserAdapter.notifyDataSetChanged();
                updateList();
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                // 必须要加的
//                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
//                mSearchNewsList.setRefreshing(false);

                finishRefreshAndLoadMore();
                super.onFailure(e, response);
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    private void updateList() {
        if (mNewsList.size() > 0 || mUserList.size() > 0) {
            mSearchNewsNullLp.setVisibility(View.GONE);
            mSearchNewsContactsNullLp.setVisibility(View.GONE);
            mSearchNewsList.setVisibility(View.VISIBLE);
            mNewsAdapter.notifyDataSetChanged();
        } else {
            CommonToast.getInstance("未搜索到内容").show();
            if (mSearchType == 2) {
                mSearchNewsContactsNullLp.setVisibility(View.VISIBLE);
                mSearchNewsNullLp.setVisibility(View.GONE);
            } else {
                mSearchNewsContactsNullLp.setVisibility(View.GONE);
                mSearchNewsNullLp.setVisibility(View.VISIBLE);
            }
            mSearchNewsList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
//        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        mNextPage = 0;
        mLastId = "";
        mNewsList.clear();
        mUserList.clear();
        doSearchQuery();
    }

    @Override
    public void onLoadMore() {
        if (loadMoreFooterView.canLoadMore()) {
//            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
            doSearchQuery();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED, this);
    }

    @Override
    public void onHandleEvent(EventCenterManager.EventMessage eventMessage) {
        if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT) {
            String uuid;
            int isLike;
            int likeCount;
            if (eventMessage.mObject instanceof FreshNewItem) {
                FreshNewItem item = (FreshNewItem) eventMessage.mObject;
                uuid = item.postUuid;
                isLike = item.isLike;
                likeCount = item.likeCount;
            } else {
                FreshNewItem item = (FreshNewItem) eventMessage.mObject;
                uuid = item.postUuid;
                isLike = item.isLike;
                likeCount = item.likeCount;
            }
            for (int i = 0; i < mNewsList.size(); i++) {
                if (Utils.isStringEquals(uuid, mNewsList.get(i).postUuid)) {
                    mNewsList.get(i).isLike = isLike;
                    mNewsList.get(i).likeCount = likeCount;
                    mNewsAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mNewsList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mNewsList.get(i).postUuid)) {
                    mNewsList.remove(i);
                    mNewsAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE) {
            FriendsItem friendsItem = (FriendsItem) eventMessage.mObject;
            for (int i = 0; i < mNewsList.size(); i++) {
                if (Utils.isStringEquals(friendsItem.uuid, mNewsList.get(i).userUuid)) {
                    mNewsList.get(i).isFollow = friendsItem.followType;
                }
            }
            mNewsAdapter.notifyDataSetChanged();
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mNewsList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mNewsList.get(i).postUuid)) {
                    mNewsList.get(i).commentCount++;
                    mNewsAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY) {
            DeleteCount deleteCount = (DeleteCount) eventMessage.mObject;
            for (int i = 0; i < mNewsList.size(); i++) {
                if (Utils.isStringEquals(deleteCount.postUUID, mNewsList.get(i).postUuid)) {
                    mNewsList.get(i).commentCount -= deleteCount.deleteCount;
                    mNewsAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mNewsList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mNewsList.get(i).postUuid)) {
                    mNewsList.get(i).browseCount++;
                    mNewsAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mNewsList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mNewsList.get(i).postUuid)) {
                    mNewsList.get(i).isDislike = 1;
                    mNewsList.get(i).dislikeCount++;
                    mNewsAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mNewsList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mNewsList.get(i).postUuid)) {
                    mNewsList.get(i).isDislike = 0;
                    mNewsList.get(i).dislikeCount--;
                    mNewsAdapter.notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    @OnClick({R.id.m_search_news_cancel, R.id.m_search_news_search_drop_img})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {
            case R.id.m_search_news_cancel:
                onBackPressed();
                break;
            case R.id.m_search_news_search_drop_img:
                Intent intent = new Intent(NewsSearchActivity.this, NewsSearchPopUpActivity.class);
                intent.putExtra(Constants.INTENT_SEARCH_NEWS_TYPE, mSearchType);
                startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_SEARCH_TYPE);
                break;
        }
    }

    @Override
    public void onLoadMore(RefreshLayout refreshlayout) {

        doSearchQuery();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {

        mNextPage = 0;
        mLastId = "";
        mNewsList.clear();
        mUserList.clear();
        doSearchQuery();
    }

    private void finishRefreshAndLoadMore() {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        Glide.get(this).clearMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).clearMemory();
        }
        Glide.get(this).trimMemory(level);
    }
}
