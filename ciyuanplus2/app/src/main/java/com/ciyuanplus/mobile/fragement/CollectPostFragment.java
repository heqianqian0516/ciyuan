package com.ciyuanplus.mobile.fragement;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ciyuanplus.base.irecyclerview.IRecyclerView;
import com.ciyuanplus.base.irecyclerview.LoadMoreFooterView;
import com.ciyuanplus.base.irecyclerview.OnLoadMoreListener;
import com.ciyuanplus.base.irecyclerview.OnRefreshListener;
import com.ciyuanplus.mobile.MyFragment;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.adapter.SquareAdapter;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.module.forum_detail.daily_detail.DailyDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.food_detail.FoodDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.note_detail.NoteDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.post_detail.PostDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.stuff_detail.StuffDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.twitter_detail.TwitterDetailActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.DeleteCount;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.net.bean.FriendsItem;
import com.ciyuanplus.mobile.net.parameter.RequestFreshNewsApiParameter;
import com.ciyuanplus.mobile.net.response.RequestFreshNewsResponse;
import com.ciyuanplus.mobile.statistics.StatisticsConstant;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.litesuits.http.exception.HttpException;
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
 * Created by Alen on 2017/8/28.
 */

public class CollectPostFragment extends MyFragment implements EventCenterManager.OnHandleEventListener, OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.m_my_collection_news_list)
    IRecyclerView mMyCollectionNewsList;
    @BindView(R.id.m_my_collection_post_null_lp)
    LinearLayout mMyCollectionPostNullLp;

    private LoadMoreFooterView loadMoreFooterView;

    private int mNextPage = 0;
    private final ArrayList<FreshNewItem> mMyLikeList = new ArrayList<>();
    private SquareAdapter mMyLikeAdapter;
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collect_post, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mMyLikeAdapter = new SquareAdapter(getActivity(), mMyLikeList, (v) -> {
            int postion = mMyCollectionNewsList.getChildAdapterPosition(v) - 2;
            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_MINE_LIKE, StatisticsConstant.OP_MINE_LIKE_LIST_ITEM_CLICK, mMyLikeAdapter.getItem(postion).postUuid);
            FreshNewItem item = mMyLikeAdapter.getItem(postion);
            if (item.bizType == FreshNewItem.FRESH_ITEM_STUFF) {//宝贝
                Intent intent = new Intent(getContext(), StuffDetailActivity.class);
                intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
                getContext().startActivity(intent);
            } else if (item.bizType == FreshNewItem.FRESH_ITEM_DAILY) {
                Intent intent = new Intent(getContext(), DailyDetailActivity.class);
                intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
                getContext().startActivity(intent);
            } else if (item.bizType == FreshNewItem.FRESH_ITEM_POST) { // 长文和说说
                if (item.renderType == 1) {
                    Intent intent = new Intent(getContext(), TwitterDetailActivity.class);
                    intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
                    getContext().startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), PostDetailActivity.class);
                    intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
                    getContext().startActivity(intent);
                }
            } else if (item.bizType == FreshNewItem.FRESH_ITEM_NEWS
                    || item.bizType == FreshNewItem.FRESH_ITEM_NOTE
                    || item.bizType == FreshNewItem.FRESH_ITEM_NEWS_COLLECTION) {
                Intent intent = new Intent(getContext(), NoteDetailActivity.class);
                intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
                getContext().startActivity(intent);
            } else if (item.bizType == FreshNewItem.FRESH_ITEM_FOOD
                    || item.bizType == FreshNewItem.FRESH_ITEM_LIVE
                    || item.bizType == FreshNewItem.FRESH_ITEM_COMMENT) {
                Intent intent = new Intent(getContext(), FoodDetailActivity.class);
                intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
                getContext().startActivity(intent);
            }
        });
        loadMoreFooterView = (LoadMoreFooterView) mMyCollectionNewsList.getLoadMoreFooterView();
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity()) {//解决显示不全的问题
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        linearLayoutManager1.setOrientation(RecyclerView.VERTICAL);
        mMyCollectionNewsList.setLayoutManager(linearLayoutManager1);
        mMyCollectionNewsList.setOnRefreshListener(this);
        mMyCollectionNewsList.setOnLoadMoreListener(this);

        this.mMyCollectionNewsList.setIAdapter(mMyLikeAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_MARKED, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_MARKED, this);

        requestMyLike();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_BEEN_MARKED, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_ITEM_CANCEL_MARKED, this);

    }

    // 获取我的喜欢
    private void requestMyLike() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_GET_MY_LIKE_POST_URL);
        postRequest.setMethod(HttpMethods.Post);
        int PAGE_SIZE = 20;
        postRequest.setHttpBody(new RequestFreshNewsApiParameter(mNextPage + "", PAGE_SIZE + "").getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(getActivity()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                // 必须要加的
                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                mMyCollectionNewsList.setRefreshing(false);

                RequestFreshNewsResponse response1 = new RequestFreshNewsResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else if (response1.freshNewsListItem.list != null && response1.freshNewsListItem.list.length > 0) {
                    if (mNextPage == 0) mMyLikeList.clear();
                    Collections.addAll(mMyLikeList, response1.freshNewsListItem.list);
                    mMyLikeAdapter.notifyDataSetChanged();
                    mNextPage++;
                }
                updateView();
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                // 必须要加的
                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
                mMyCollectionNewsList.setRefreshing(false);
                super.onFailure(e, response);
//
//                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_get_fresh_news_fail_alert),
//                        Toast.LENGTH_SHORT).show();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    private void updateView() {
        if (mMyLikeAdapter.getItemCount() > 0) {
            this.mMyCollectionPostNullLp.setVisibility(View.GONE);
            mMyCollectionNewsList.setVisibility(View.VISIBLE);
        } else {
            this.mMyCollectionPostNullLp.setVisibility(View.VISIBLE);
            mMyCollectionNewsList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        mNextPage = 0;
        requestMyLike();
    }

    @Override
    public void onLoadMore() {
        if (loadMoreFooterView.canLoadMore()) {
            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
            requestMyLike();
        }
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
            for (int i = 0; i < mMyLikeList.size(); i++) {
                if (Utils.isStringEquals(uuid, mMyLikeList.get(i).postUuid)) {
                    mMyLikeList.get(i).isLike = isLike;
                    mMyLikeList.get(i).likeCount = likeCount;
                    mMyLikeAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mMyLikeList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mMyLikeList.get(i).postUuid)) {
                    mMyLikeList.remove(i);
                    mMyLikeAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE) {
            FriendsItem friendsItem = (FriendsItem) eventMessage.mObject;
            for (int i = 0; i < mMyLikeList.size(); i++) {
                if (Utils.isStringEquals(friendsItem.uuid, mMyLikeList.get(i).userUuid)) {
                    mMyLikeList.get(i).isFollow = friendsItem.followType;
                }
            }
            mMyLikeAdapter.notifyDataSetChanged();
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mMyLikeList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mMyLikeList.get(i).postUuid)) {
                    mMyLikeList.get(i).commentCount++;
                    mMyLikeAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY) {
            String postUuid = (String) eventMessage.mObject;
//            for (int i = 0; i < mMyLikeList.size(); i++) {
//                if (Utils.isStringEquals(postUuid, mMyLikeList.get(i).postUuid)) {
//                    mMyLikeList.get(i).commentCount--;
//                    mMyLikeAdapter.notifyDataSetChanged();
//                    return;
//                }
//            }


            DeleteCount deleteCount = (DeleteCount) eventMessage.mObject;
            for (int i = 0; i < mMyLikeList.size(); i++) {
                if (Utils.isStringEquals(deleteCount.postUUID, mMyLikeList.get(i).postUuid)) {
                    mMyLikeList.get(i).commentCount -= deleteCount.deleteCount;
                    mMyLikeAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mMyLikeList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mMyLikeList.get(i).postUuid)) {
                    mMyLikeList.get(i).browseCount++;
                    mMyLikeAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mMyLikeList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mMyLikeList.get(i).postUuid)) {
                    mMyLikeList.get(i).isDislike = 1;
                    mMyLikeList.get(i).dislikeCount++;
                    mMyLikeAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mMyLikeList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mMyLikeList.get(i).postUuid)) {
                    mMyLikeList.get(i).isDislike = 0;
                    mMyLikeList.get(i).dislikeCount--;
                    mMyLikeAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_BEEN_MARKED) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mMyLikeList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mMyLikeList.get(i).postUuid)) {
                    mMyLikeList.get(i).isRated = 1;
                    mMyLikeAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_CANCEL_MARKED) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mMyLikeList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mMyLikeList.get(i).postUuid)) {
                    mMyLikeList.get(i).isRated = 0;
                    mMyLikeAdapter.notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
