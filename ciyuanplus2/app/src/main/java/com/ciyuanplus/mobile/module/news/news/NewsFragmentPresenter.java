package com.ciyuanplus.mobile.module.news.news;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.ciyuanplus.mobile.adapter.MainNewsAdapter;
import com.ciyuanplus.mobile.adapter.QuickEnterAdapter;
import com.ciyuanplus.mobile.manager.CacheManager;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.forum_detail.daily_detail.DailyDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.food_detail.FoodDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.note_detail.NoteDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.post_detail.PostDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.stuff_detail.StuffDetailActivity;
import com.ciyuanplus.mobile.module.found.market.MarketActivity;
import com.ciyuanplus.mobile.module.live_hood.LiveHoodActivity;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.module.news.select_show_type.SelectShowTypeActivity;
import com.ciyuanplus.mobile.module.webview.JsWebViewActivity;
import com.ciyuanplus.mobile.module.wiki.around_wiki.AroundWikiActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.BannerItem;
import com.ciyuanplus.mobile.net.bean.DeleteCount;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.net.bean.FriendsItem;
import com.ciyuanplus.mobile.net.bean.SquareTagItem;
import com.ciyuanplus.mobile.net.parameter.RequestBannerApiParameter;
import com.ciyuanplus.mobile.net.parameter.RequestCommunityPostsApiParameter;
import com.ciyuanplus.mobile.net.parameter.RequestSquareTagApiParameter;
import com.ciyuanplus.mobile.net.parameter.RequestWorldPostsApiParameter;
import com.ciyuanplus.mobile.net.response.RequestBannerListResponse;
import com.ciyuanplus.mobile.net.response.RequestFreshNewsResponse;
import com.ciyuanplus.mobile.net.response.RequestSquareTagResponse;
import com.ciyuanplus.mobile.statistics.StatisticsConstant;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.CacheMode;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.fragment.app.Fragment;

/**
 * Created by Alen on 2017/12/11.
 */

public class NewsFragmentPresenter implements NewsFragmentContract.Presenter {
    private final NewsFragmentContract.View mView;

    private String mZoneName;
    private final ArrayList<FreshNewItem> mNewsList = new ArrayList<>();
    private final MainNewsAdapter mNewsAdapter;

    private int mSelectedShowType = 0;


    private final int PAGE_SIZE = 20;
    private int mNextPage;
    private String mLastId = "";
    private boolean mIsFreshing = false;

    private final ArrayList<BannerItem> mTopList = new ArrayList<>();
    private final ArrayList<SquareTagItem> mSqureTagList = new ArrayList<>();
    private QuickEnterAdapter mQuickEnterAdapter;

    @Inject
    public NewsFragmentPresenter(NewsFragmentContract.View mView) {
        this.mView = mView;
        updateZoneName();
        mNewsAdapter = new MainNewsAdapter(((Fragment) mView).getActivity(), mNewsList, (v) -> {
            int position = mView.getListView().getChildAdapterPosition(v) - 2;
            clickItem(position);
        });
        mView.getListView().setIAdapter(mNewsAdapter);

        mQuickEnterAdapter = new QuickEnterAdapter((Activity) mView.getDefaultContext(), mSqureTagList, (v) -> {
            int position = mView.getQuickEnterListView().getChildAdapterPosition(v);
            SquareTagItem item = mQuickEnterAdapter.getItem(position);
            if (item == null) return;
            if (item.allowedUserState == 1 && !LoginStateManager.isLogin()) {
                Intent intent = new Intent(mView.getDefaultContext(), LoginActivity.class);
                mView.getDefaultContext().startActivity(intent);
                return;
            }
            if (!Utils.isStringEmpty(item.url)) {
                Intent intent = new Intent(mView.getDefaultContext(), JsWebViewActivity.class);
                intent.putExtra(Constants.INTENT_OPEN_URL, item.url);
                mView.getDefaultContext().startActivity(intent);
            } else {
                if (item.toOriginalPage == 1) {
                    Intent intent = new Intent(mView.getDefaultContext(), MarketActivity.class);
                    mView.getDefaultContext().startActivity(intent);
                } else if (item.toOriginalPage == 2) {
                    Intent intent = new Intent(mView.getDefaultContext(), AroundWikiActivity.class);
                    mView.getDefaultContext().startActivity(intent);
                } else if (item.toOriginalPage == 3) {
                    Intent intent = new Intent(mView.getDefaultContext(), LiveHoodActivity.class);
                    mView.getDefaultContext().startActivity(intent);
                }
            }
        });
        mView.getQuickEnterListView().setAdapter(mQuickEnterAdapter);
        doRequest(true);
        requestBanner();
        requestSquareTagList();
    }

    @Override
    public void detachView() {
    }

    @Override
    public void goSelectTypeActivity() {
        Intent intent1 = new Intent(mView.getDefaultContext(), SelectShowTypeActivity.class);
        intent1.putExtra(Constants.INTENT_COMMUNITY_NAME, mZoneName);
        intent1.putExtra(SelectShowTypeActivity.SELECTED, mSelectedShowType);
        ((Fragment) mView).startActivityForResult(intent1, Constants.REQUEST_SELECT_SHOW_TYPE);
    }

    @Override
    public void updateZoneName() {
        mZoneName = UserInfoData.getInstance().getUserInfoItem().currentCommunityName;
        updateSelectedName();
    }

    @Override
    public void updateShowType(int type) {
        if (mSelectedShowType != type) {
            mSelectedShowType = type;
            updateSelectedName();
            doRequest(true);
            mView.getListView().scrollToPosition(0);
        }
    }

    @Override
    public ArrayList<BannerItem> getTopList() {
        return mTopList;
    }

    private void updateSelectedName() {
        if (mSelectedShowType == 0) {
            mView.setSelectedName(mZoneName + "附近");
        } else if (mSelectedShowType == 1) {
            mView.setSelectedName(mZoneName);
        } else if (mSelectedShowType == 2) {
            mView.setSelectedName("邻里点评");
        } else if (mSelectedShowType == 3) {
            mView.setSelectedName("邻里买卖");
        }

        mView.changeTopBarVisible(mSelectedShowType == 0);
    }

    @Override
    public void handleEventCenterEvent(EventCenterManager.EventMessage eventMessage) {
        if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_COMMUNITY_LIST_FINISH) {
            // 刷新小区列表
            updateZoneName();
            if (mSelectedShowType == 1) doRequest(true);
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_DEFAULT_COMMUNITY_FINISH) {
            updateZoneName();
            if (mSelectedShowType == 1) doRequest(true);
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT) {
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
//            String postUuid = (String) eventMessage.mObject;
//            for (int i = 0; i < mNewsList.size(); i++) {
//                if (Utils.isStringEquals(postUuid, mNewsList.get(i).postUuid)) {
//                    mNewsList.get(i).commentCount--;
//                    mNewsAdapter.notifyDataSetChanged();
//                    return;
//                }
//            }

            DeleteCount deleteCount = (DeleteCount) eventMessage.mObject;
            for (int i = 0; i < mNewsList.size(); i++) {
                if (Utils.isStringEquals(deleteCount.postUUID, mNewsList.get(i).postUuid)) {
                    mNewsList.get(i).commentCount -= deleteCount.deleteCount;
                    mNewsAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_ALL_NEWS_LIST) {
            doRequest(true);
            mView.getListView().scrollToPosition(0);
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mNewsList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mNewsList.get(i).postUuid)) {
                    mNewsList.get(i).browseCount++;
                    mNewsAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_BEEN_MARKED) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mNewsList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mNewsList.get(i).postUuid)) {
                    mNewsList.get(i).isRated = 1;
                    mNewsAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_ITEM_CANCEL_MARKED) {
            String postUuid = (String) eventMessage.mObject;
            for (int i = 0; i < mNewsList.size(); i++) {
                if (Utils.isStringEquals(postUuid, mNewsList.get(i).postUuid)) {
                    mNewsList.get(i).isRated = 0;
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

    @Override
    public void clickItem(int postion) {
        FreshNewItem item = mNewsAdapter.getItem(postion);
        StatisticsManager.onEventInfo(StatisticsConstant.MODULE_NEWS, StatisticsConstant.OP_NEWS_ITEM_ENTER, item.postUuid);
        if (item.bizType == FreshNewItem.FRESH_ITEM_STUFF) {//宝贝
            Intent intent = new Intent(mView.getDefaultContext(), StuffDetailActivity.class);
            intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
            mView.getDefaultContext().startActivity(intent);
        } else if (item.bizType == FreshNewItem.FRESH_ITEM_DAILY) {
            Intent intent = new Intent(mView.getDefaultContext(), DailyDetailActivity.class);
            intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
            mView.getDefaultContext().startActivity(intent);
        } else if (item.bizType == FreshNewItem.FRESH_ITEM_POST) { // 长文和说说

            Intent intent = new Intent(mView.getDefaultContext(), PostDetailActivity.class);
            intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
            mView.getDefaultContext().startActivity(intent);

        } else if (item.bizType == FreshNewItem.FRESH_ITEM_NEWS
                || item.bizType == FreshNewItem.FRESH_ITEM_NOTE
                || item.bizType == FreshNewItem.FRESH_ITEM_NEWS_COLLECTION) {
            Intent intent = new Intent(mView.getDefaultContext(), NoteDetailActivity.class);
            intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
            mView.getDefaultContext().startActivity(intent);
        } else if (item.bizType == FreshNewItem.FRESH_ITEM_FOOD
                || item.bizType == FreshNewItem.FRESH_ITEM_LIVE
                || item.bizType == FreshNewItem.FRESH_ITEM_COMMENT) {
            Intent intent = new Intent(mView.getDefaultContext(), FoodDetailActivity.class);
            intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.postUuid);
            mView.getDefaultContext().startActivity(intent);
        }
    }

    @Override
    public void doRequest(boolean reset) {
        if (reset) {
            mNextPage = 0;
            mLastId = "";
        }
        if (mSelectedShowType == 0) {
            requestWorldPost("");
        } else if (mSelectedShowType == 1) {
            requestZonePost();
        } else if (mSelectedShowType == 2) {
            requestWorldPost("" + FreshNewItem.FRESH_ITEM_COMMENT);
        } else if (mSelectedShowType == 3) {
            requestWorldPost("" + FreshNewItem.FRESH_ITEM_STUFF);
        }
    }

    private void requestZonePost() {
        if (mIsFreshing) return;
        mIsFreshing = true;
        mView.showLoadingDialog();
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_COMMUNITY_POST_URL);
        postRequest.setHttpBody(new RequestCommunityPostsApiParameter(UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid,
                "", mNextPage + "", PAGE_SIZE + "", mLastId, "").getRequestBody());
        postRequest.setMethod(HttpMethods.Post);
        // 设置缓存
        postRequest.setCacheMode(CacheMode.NetFirst);
        postRequest.setCacheExpire(-1, TimeUnit.SECONDS);
        postRequest.setCacheDir(CacheManager.getInstance().getSettleDirectory());
        postRequest.setCacheKey(null);

        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                // 必须要加的
                mView.dismissLoadingDialog();
                mView.stopRereshAndLoad();

                RequestFreshNewsResponse response1 = new RequestFreshNewsResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else if (response1.freshNewsListItem.list != null) {
                    if (mNextPage == 0) mNewsList.clear();
                    mNextPage++;
                    Collections.addAll(mNewsList, response1.freshNewsListItem.list);
                    if (mNewsList.size() > 0)
                        mLastId = mNewsList.get(mNewsList.size() - 1).id;
                }
                mNewsAdapter.notifyDataSetChanged();
                mView.updateListView(mNewsList.size() > 0);
                mIsFreshing = false;
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                // 必须要加的
                mView.dismissLoadingDialog();
                mView.stopRereshAndLoad();

                if (response.isCacheHit()) { // 中了缓存就不要在折腾了，直接加载数据
                    String s = response.getRequest().getDataParser().getData();
                    RequestFreshNewsResponse response1 = new RequestFreshNewsResponse(s);
                    if (response1.freshNewsListItem.list != null && response1.freshNewsListItem.list.length > 0) {
                        if (mNextPage == 0) mNewsList.clear();
                        Collections.addAll(mNewsList, response1.freshNewsListItem.list);
                        mNextPage++;
                    }
                    mNewsAdapter.notifyDataSetChanged();
                    mView.updateListView(mNewsList.size() > 0);
                }
                super.onFailure(e, response);
                mIsFreshing = false;
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    private void requestWorldPost(String requsetType) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_WORLD_POST_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestWorldPostsApiParameter("", mNextPage + "", PAGE_SIZE + "", mLastId, "", requsetType).getRequestBody());
        // 设置缓存
        postRequest.setCacheMode(CacheMode.NetFirst);
        postRequest.setCacheExpire(-1, TimeUnit.SECONDS);
        postRequest.setCacheDir(CacheManager.getInstance().getSettleDirectory());
        postRequest.setCacheKey(null);

        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                // 必须要加的
                mView.stopRereshAndLoad();

                RequestFreshNewsResponse response1 = new RequestFreshNewsResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else if (response1.freshNewsListItem.list != null && response1.freshNewsListItem.list.length > 0) {
                    if (mNextPage == 0) mNewsList.clear();
                    Collections.addAll(mNewsList, response1.freshNewsListItem.list);
                    if (mNewsList.size() > 0)
                        mLastId = mNewsList.get(mNewsList.size() - 1).id;
                    mNextPage++;
                }
                mNewsAdapter.notifyDataSetChanged();
                mView.updateListView(mNewsList.size() > 0);
            }


            @Override
            public void onFailure(HttpException e, Response<String> response) {
                // 必须要加的
                mView.stopRereshAndLoad();

                if (response.isCacheHit()) { // 中了缓存就不要在折腾了，直接加载数据
                    String s = response.getRequest().getDataParser().getData();
                    RequestFreshNewsResponse response1 = new RequestFreshNewsResponse(s);
                    if (response1.freshNewsListItem.list != null && response1.freshNewsListItem.list.length > 0) {
                        if (mNextPage == 0) mNewsList.clear();
                        Collections.addAll(mNewsList, response1.freshNewsListItem.list);
                        mNextPage++;
                        mNewsAdapter.notifyDataSetChanged();
                        mView.updateListView(mNewsList.size() > 0);
                    }
                }
                super.onFailure(e, response);

            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    private void requestBanner() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_GET_BANNER_LIST_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestBannerApiParameter("2").getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                RequestBannerListResponse response1 = new RequestBannerListResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    // 存到本地里面
//                    SharedPreferencesManager.putString(Constants.SHARED_PREFERENCES_SET,
//                            Constants.SHARED_BANNER_LIST_CONTENT, response1.result);
                    mTopList.clear();
                    Collections.addAll(mTopList, response1.bannerListItem.list);

                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
                mView.updateTopView();
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                mView.updateTopView();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    //获取广场入口列表
    private void requestSquareTagList() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_SQUARE_TAG_LIST_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestSquareTagApiParameter("0", "4").getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                RequestSquareTagResponse response1 = new RequestSquareTagResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
//                    CommonToast.getInstance(response1.mMsg).show();
                    //民生一定是大于4个的  这里跟后台确认了
                    if (response1.squareTagListItem.list != null) {
                        mSqureTagList.clear();
                        Collections.addAll(mSqureTagList, response1.squareTagListItem.list);
                        mQuickEnterAdapter.notifyDataSetChanged();
                    }
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }
}
