package com.ciyuanplus.mobile.module.mine.my_publish.MineNews;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ciyuanplus.mobile.MyFragment;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.adapter.MineNewsAdapter;
import com.ciyuanplus.mobile.manager.CacheManager;
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
import com.ciyuanplus.mobile.net.parameter.RequestFreshNewsApiParameter;
import com.ciyuanplus.mobile.net.response.RequestFreshNewsResponse;
import com.ciyuanplus.mobile.pulltorefresh.XListView;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.CacheMode;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Alen on 2018/1/26.
 */

public class MinePostFragment extends MyFragment implements EventCenterManager.OnHandleEventListener, XListView.IXListViewListener, OnLoadMoreListener, OnRefreshListener {
    @BindView(R.id.m_mine_publish_post_list)
    XListView mMinePublishPostList;
    @BindView(R.id.m_mine_publish_post_null_lp)
    LinearLayout mMinePublishPostNullLp;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private int mPostNextPage = 0;
    private MineNewsAdapter mMyPublishAdapter;
    private final ArrayList<FreshNewItem> mMyPublishList = new ArrayList<>();
    private Unbinder mUnbinder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine_post, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mMinePublishPostList.setPullRefreshEnable(false);
        mMinePublishPostList.setPullLoadEnable(false);
        mMinePublishPostList.setXListViewListener(this);

        mRefreshLayout.setOnLoadMoreListener(this);
        mRefreshLayout.setOnRefreshListener(this);

        mMyPublishAdapter = new MineNewsAdapter(getActivity(), mMyPublishList);
        mMinePublishPostList.setAdapter(mMyPublishAdapter);
        mMinePublishPostList.setOnItemClickListener((parent, v, position, id) -> {
            if (id == -1) {
                return;
            }
            int postion = (int) id;
            FreshNewItem item = mMyPublishAdapter.getItem(postion);
            if (item.bizType == FreshNewItem.FRESH_ITEM_STUFF) {//宝贝
                Intent intent = new Intent(getActivity(), StuffDetailActivity.class);
                intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
                getActivity().startActivity(intent);
            } else if (item.bizType == FreshNewItem.FRESH_ITEM_DAILY) {
                Intent intent = new Intent(getActivity(), DailyDetailActivity.class);
                intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
                getActivity().startActivity(intent);
            } else if (item.bizType == 4) { // 长文和说说
                Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
                getActivity().startActivity(intent);

            }
            else if (item.bizType == FreshNewItem.FRESH_ITEM_POST) { // 长文和说说

                if(item.renderType == 1) {
                    Intent intent = new Intent(getActivity(), TwitterDetailActivity.class);
                    intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
                    intent.putExtra(Constants.INTENT_BIZE_TYPE, item.bizType);
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                    intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
                    intent.putExtra(Constants.INTENT_BIZE_TYPE, item.bizType);
                    getActivity().startActivity(intent);
                }

//                Intent intent = new Intent(getActivity(), PostDetailActivity.class);
//                intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
//                intent.putExtra(Constants.INTENT_BIZE_TYPE, item.bizType);
//                getActivity().startActivity(intent);

            }
            else if (item.bizType == FreshNewItem.FRESH_ITEM_NEWS
                    || item.bizType == FreshNewItem.FRESH_ITEM_NOTE
//                    || item.bizType == FreshNewItem.FRESH_ITEM_POST
                    || item.bizType == FreshNewItem.FRESH_ITEM_NEWS_COLLECTION) {
                Intent intent = new Intent(getActivity(), NoteDetailActivity.class);
                intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
                getActivity().startActivity(intent);
            } else if (item.bizType == FreshNewItem.FRESH_ITEM_FOOD
                    || item.bizType == FreshNewItem.FRESH_ITEM_LIVE
                    || item.bizType == FreshNewItem.FRESH_ITEM_COMMENT) {
                Intent intent = new Intent(getActivity(), FoodDetailActivity.class);
                intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
                getActivity().startActivity(intent);
            }
        });
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

        requestMyPublish();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    // 获取我的发布
    private void requestMyPublish() {

        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_GET_MY_PUBLISH_POST_URL);
        postRequest.setMethod(HttpMethods.Post);
        int PAGE_SIZE = 20;
        postRequest.setHttpBody(new RequestFreshNewsApiParameter(mPostNextPage + "", PAGE_SIZE + "").getRequestBody());
        // 设置缓存
        postRequest.setCacheMode(CacheMode.NetFirst);
        postRequest.setCacheExpire(-1, TimeUnit.SECONDS);
        postRequest.setCacheDir(CacheManager.getInstance().getSettleDirectory());
        postRequest.setCacheKey(null);

        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(getActivity()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                if (MinePostFragment.this.getActivity() == null || MinePostFragment.this.getActivity().isFinishing()) return;
                // 必须要加的
//                mMinePublishPostList.stopLoadMore();
//                mMinePublishPostList.stopRefresh();

                finishRefreshAndLoadMore();

                RequestFreshNewsResponse response1 = new RequestFreshNewsResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else if (response1.freshNewsListItem.list != null && response1.freshNewsListItem.list.length > 0) {
                    if (mPostNextPage == 0) mMyPublishList.clear();
                    Collections.addAll(mMyPublishList, response1.freshNewsListItem.list);
                    mMyPublishAdapter.notifyDataSetChanged();
                    mPostNextPage++;
                }
                updateNullView(mMyPublishList.size());
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                // 必须要加的
                if (MinePostFragment.this.getActivity() == null || MinePostFragment.this.getActivity().isFinishing()) return;
//                mMinePublishPostList.stopLoadMore();
//                mMinePublishPostList.stopRefresh();

                finishRefreshAndLoadMore();
                if (response.isCacheHit()) { // 中了缓存就不要在折腾了，直接加载数据
                    String s = response.getRequest().getDataParser().getData();
                    RequestFreshNewsResponse response1 = new RequestFreshNewsResponse(s);
                    if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                        CommonToast.getInstance(response1.mMsg).show();
                    } else if (response1.freshNewsListItem.list != null && response1.freshNewsListItem.list.length > 0) {
                        if (mPostNextPage == 0) mMyPublishList.clear();
                        Collections.addAll(mMyPublishList, response1.freshNewsListItem.list);
                        mMyPublishAdapter.notifyDataSetChanged();
                        mPostNextPage++;
                    }
                    updateNullView(mMyPublishList.size());
                }
                super.onFailure(e, response);
//
//                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_get_fresh_news_fail_alert),
//                        Toast.LENGTH_SHORT).show();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    private void updateNullView(int size) {
        if (size > 0) {
            mMinePublishPostList.setVisibility(View.VISIBLE);
            mRefreshLayout.setVisibility(View.VISIBLE);
            mMinePublishPostNullLp.setVisibility(View.GONE);
        } else {
            mMinePublishPostList.setVisibility(View.GONE);
            mRefreshLayout.setVisibility(View.GONE);
            mMinePublishPostNullLp.setVisibility(View.VISIBLE);
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
            for (int i = 0; i < mMyPublishList.size(); i++) {
                if (Utils.isStringEquals(uuid, mMyPublishList.get(i).postUuid)) {
                    mMyPublishList.get(i).isLike = isLike;
                    mMyPublishList.get(i).likeCount = likeCount;
                    mMyPublishAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mMyPublishList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mMyPublishList.get(i).postUuid)) {
                    mMyPublishList.get(i).commentCount++;
                    mMyPublishAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY) {
//            String postUuid = (String) eventMessage.mObject;
//            for (int i = 0; i < mMyPublishList.size(); i++) {
//                if (Utils.isStringEquals(postUuid, mMyPublishList.get(i).postUuid)) {
//                    mMyPublishList.get(i).commentCount--;
//                    mMyPublishAdapter.notifyDataSetChanged();
//                    return;
//                }
//            }

            DeleteCount deleteCount = (DeleteCount) eventMessage.mObject;
            for (int i = 0; i < mMyPublishList.size(); i++) {
                if (Utils.isStringEquals(deleteCount.postUUID, mMyPublishList.get(i).postUuid)) {
                    mMyPublishList.get(i).commentCount -= deleteCount.deleteCount;
                    mMyPublishAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mMyPublishList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mMyPublishList.get(i).postUuid)) {
                    mMyPublishList.remove(i);
                    mMyPublishAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mMyPublishList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mMyPublishList.get(i).postUuid)) {
                    mMyPublishList.get(i).browseCount++;
                    mMyPublishAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_ZONE_NEWS_LIST) {
            mPostNextPage = 0;
            requestMyPublish();
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mMyPublishList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mMyPublishList.get(i).postUuid)) {
                    mMyPublishList.get(i).isDislike = 1;
                    mMyPublishList.get(i).dislikeCount++;
                    mMyPublishAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mMyPublishList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mMyPublishList.get(i).postUuid)) {
                    mMyPublishList.get(i).isDislike = 0;
                    mMyPublishList.get(i).dislikeCount--;
                    mMyPublishAdapter.notifyDataSetChanged();
                    return;
                }
            }
        }
    }


    @Override
    public void onRefresh() {
        mPostNextPage = 0;
        requestMyPublish();
    }

    @Override
   public void onLoadMore() {
        requestMyPublish();
    }

    @Override
   public void onLoadMore(RefreshLayout refreshlayout) {

        requestMyPublish();

    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mPostNextPage = 0;
        requestMyPublish();
    }

    private void finishRefreshAndLoadMore() {
        mRefreshLayout.finishLoadMore();
        mRefreshLayout.finishRefresh();
    }
}
