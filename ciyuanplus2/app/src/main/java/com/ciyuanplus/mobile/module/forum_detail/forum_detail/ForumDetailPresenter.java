package com.ciyuanplus.mobile.module.forum_detail.forum_detail;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.adapter.NewsCommentAdapter;
import com.ciyuanplus.mobile.manager.AMapLocationManager;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.forum_detail.stuff_detail.StuffDetailActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.CommentItem;
import com.ciyuanplus.mobile.net.bean.DeleteCount;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.net.bean.FriendsItem;
import com.ciyuanplus.mobile.net.bean.ReplyItem;
import com.ciyuanplus.mobile.net.parameter.AddCommentApiParameter;
import com.ciyuanplus.mobile.net.parameter.CancelCommentLikeOperaApiParameter;
import com.ciyuanplus.mobile.net.parameter.CommentLikeOperaApiParameter;
import com.ciyuanplus.mobile.net.parameter.DeleteCommentsApiParameter;
import com.ciyuanplus.mobile.net.parameter.DeleteMyNewsApiParameter;
import com.ciyuanplus.mobile.net.parameter.FollowOtherApiParameter;
import com.ciyuanplus.mobile.net.parameter.ItemOperaApiParameter;
import com.ciyuanplus.mobile.net.parameter.LikeOperaApiParameter;
import com.ciyuanplus.mobile.net.parameter.RequestPostCommentApiParameter;
import com.ciyuanplus.mobile.net.parameter.RequestPostDetailApiParameter;
import com.ciyuanplus.mobile.net.parameter.RequestStuffDetailApiParameter;
import com.ciyuanplus.mobile.net.response.DeleteCommentOrReplyResponse;
import com.ciyuanplus.mobile.net.response.RequestOtherInfoResponse;
import com.ciyuanplus.mobile.net.response.RequestPostCommentResponse;
import com.ciyuanplus.mobile.net.response.RequestPostDetailResponse;
import com.ciyuanplus.mobile.net.response.RequestStuffDetailResponse;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

/**
 * @author Alen
 * @date 2017/12/11
 */

public class ForumDetailPresenter implements ForumDetailContract.Presenter {
    private final ForumDetailContract.View mView;

    // 帖子ID
    public String mUuid;
    public FreshNewItem mItem;
    // 是否是自己发的帖子
    public boolean isMine;
    public boolean mHideTag = false;
    // 是否是banner详情
    public boolean isBanner;

    private NewsCommentAdapter mCommentAdapter;
    private final ArrayList<CommentItem> mCommentList = new ArrayList<>();
    private int mNextPage = 0;
    private final int PAGE_SIZE = 20;
    public String mReplyCommentId;
    public String mTargetCommentId;
    public String mReplyToUserId;
    public CommentItem mTryDeleteComment;
    public ReplyItem mTryDeleteReply;
    // 防止连续请求  数据重复
    private boolean mIsRequestComment = false;
    private int mBizType;

    @Inject
    public ForumDetailPresenter(ForumDetailContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void detachView() {
    }

    @Override
    public void initData(Intent intent) {
        isMine = intent.getBooleanExtra(Constants.INTENT_NEWS_IS_MINE, false);
        mUuid = intent.getStringExtra(Constants.INTENT_NEWS_ID_ITEM);
        isBanner = intent.getBooleanExtra(Constants.INTENT_IS_BANNER, false);
        mHideTag = intent.getBooleanExtra(Constants.INTENT_HIDE_TAG, false);
        mBizType = intent.getIntExtra(Constants.INTENT_BIZE_TYPE, -1);

        mCommentAdapter = new NewsCommentAdapter((ForumDetailActivity) mView, mCommentList, mBizType);
        mView.getDetailCommentListView().setAdapter(mCommentAdapter);
        requestForumDetail();
//        mCommentAdapter = new NewsCommentAdapter((ForumDetailActivity) mView, mCommentList, mBizType);
        mNextPage = 0;

    }

    @Override
    public void resetCommentData() {
        mReplyCommentId = "";
        mReplyToUserId = "";
        mTargetCommentId = "";
    }

    @Override
    public void requestForumDetail() { // 区分是宝贝还是其他的
        if (mView instanceof StuffDetailActivity) {
            requestStuffDetail();
        } else {
            requestPostDetail();
        }
    }

    // 收藏新鲜事
    public void cancelCollectPost() {
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

                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_ITEM_CANCEL_COLLECTED, mItem.postUuid));
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
    public void collectPost() {
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

                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_ITEM_BEEN_COLLECTED, mItem.postUuid));

                }
            }
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance("操作失败").show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    private void requestPostDetail() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_POST_DETAIL_URL);
        postRequest.setMethod(HttpMethods.Post);

        postRequest.setHttpBody(new RequestPostDetailApiParameter(mUuid).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                RequestPostDetailResponse response1 = new RequestPostDetailResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    mView.getDetailCommentListView().setVisibility(View.VISIBLE);
                    mItem = response1.freshNewsItem;

                    if (mCommentAdapter == null) {
                        mCommentAdapter = new NewsCommentAdapter((ForumDetailActivity) mView, mCommentList, mBizType);
                        mView.getDetailCommentListView().setAdapter(mCommentAdapter);
                    } else {
                        mCommentAdapter.notifyDataSetChanged();
                    }


                    isMine = Utils.isStringEquals(mItem.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid);
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, mUuid));
                    mView.updateView();
                    mView.updateBottomView();

                    requestPostComments(PAGE_SIZE);

                } else if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_NULL_DATA)) {
                    mView.getDetailCommentListView().setVisibility(View.GONE);
                    CommonToast.getInstance(response1.mMsg).show();
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

    private void requestStuffDetail() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_STUFF_DETAIL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestStuffDetailApiParameter(AMapLocationManager.getInstance().getLongitude(),
                AMapLocationManager.getInstance().getLatitude(), mUuid).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                RequestStuffDetailResponse response1 = new RequestStuffDetailResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    mView.getDetailCommentListView().setVisibility(View.VISIBLE);
                    mItem = response1.FreshNewItem;
                    isMine = Utils.isStringEquals(mItem.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid);
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT, mUuid));

                    mView.updateView();
                    mView.updateBottomView();

                    requestPostComments(PAGE_SIZE);

                } else if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_NULL_DATA)) {
                    mView.getDetailCommentListView().setVisibility(View.GONE);
                    CommonToast.getInstance(response1.mMsg).show();
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

    @Override
    public void requestPostComments(boolean b) {
        if (b) {
            mNextPage = 0;
        }
        requestPostComments(PAGE_SIZE);
    }

    private void requestPostComments(int pageSize) {
        if (mIsRequestComment) {
            return;
        }

        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_GET_POST_COMMENTS_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestPostCommentApiParameter(mNextPage + "", pageSize + "", mUuid).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                mIsRequestComment = false;

                // 必须要加的
                mView.stopRefreshAndLoadMore();


                RequestPostCommentResponse response1 = new RequestPostCommentResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    if (mNextPage == 0) {
                        mCommentList.clear();
                    }
                    mNextPage++;
                    mView.setLoadMoreEnable(response1.commentListItem.list.length >= pageSize);
                    Collections.addAll(mCommentList, response1.commentListItem.list);

                    mCommentAdapter.notifyDataSetChanged();
                    mView.updateCommentView(mCommentList.size());
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                // 必须要加的
                mIsRequestComment = false;

                mView.stopRefreshAndLoadMore();
                super.onFailure(e, response);
            }
        });
        mIsRequestComment = true;
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    /**
     * 回复楼猪帖子
     */
    @Override
    public void submitCommentOrRely(String comment) {
        if (Utils.isStringEmpty(mReplyCommentId)) {
            submitComment(comment);
        } else {
            submitReply(comment, mReplyCommentId, mTargetCommentId, mReplyToUserId);
        }
    }

    // 提交帖子的评论内容
    private void submitComment(String comments) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_POST_COMMENTS_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new AddCommentApiParameter(mUuid, "", "", comments, "1", mItem.userUuid).getRequestBody());
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
                    CommonToast.getInstance("评论成功").show();
                    mView.setCommentInput("");
                    mItem.commentCount++;
                    mView.setCommentText(Utils.formateNumber(mItem.commentCount));
                    mNextPage = 0;
                    requestPostComments(PAGE_SIZE);// 成功之后需要刷新评论列表
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY, mUuid));
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

    // 提交帖子的回复内容
    private void submitReply(String comment, String commentId, String targetCommentId, String toUserUuid) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_POST_COMMENTS_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new AddCommentApiParameter(mUuid, commentId, targetCommentId, comment, "2", toUserUuid).getRequestBody());
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
                    CommonToast.getInstance("评论成功").show();
                    mView.setCommentInput("");
                    mItem.commentCount++;
                    mView.setCommentText(Utils.formateNumber(mItem.commentCount));
                    mNextPage = 0;
                    requestPostComments(PAGE_SIZE);// 成功之后需要刷新评论列表
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY, mUuid));
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

    // 赞新鲜事
    @Override
    public void likePost() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_LIKE_URL);

        Log.d("赞新鲜事", "likePost: mItem.bizType  =  " + mItem.bizType + " , mUUID = " + mUuid);
        postRequest.setHttpBody(new LikeOperaApiParameter(mItem.bizType + "", mUuid).getRequestBody());
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
                    mItem.isLike = 1;
                    mItem.likeCount++;
//                    mView.updateBottomView();
                    mView.updateView();
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, mItem));

                }
            }
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance("mmmmmm 操作失败").show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    // 取消赞新鲜事
    @Override
    public void cancelLikePost() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_CANCEL_LIKE_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new ItemOperaApiParameter(mUuid).getRequestBody());
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
                    mItem.isLike = 0;
                    if (mItem.likeCount > 0) mItem.likeCount--;
//                    mView.updateBottomView();
                    mView.updateView();
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_NEWS_LIKE_COUNT, mItem));

                }
            }
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance("操作失败").show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    // 删除我发布的某个帖子
    public void deleteFreshNews(FreshNewItem item) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_DELETE_MY_PUBLISH_POST_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new DeleteMyNewsApiParameter(item.postUuid, item.communityUuid).getRequestBody());
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
                    CommonToast.getInstance("删除成功").show();
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, mItem.postUuid));
                    ((Activity) mView).finish();
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

    //删除评论
    public void deleteComment(final CommentItem item) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_DELETE_COMMENT_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new DeleteCommentsApiParameter(mItem.postUuid, item.commentUuid).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                DeleteCommentOrReplyResponse response1 = new DeleteCommentOrReplyResponse(s);

                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    mCommentList.remove(item);
                    mItem.commentCount -= response1.deleteCommentOrReplyItem.delCount;
                    if (mItem.commentCount < 0) {
                        mItem.commentCount = 0;
                    }

                    CommonToast.getInstance("删除成功").show();
                    if (mCommentAdapter == null) {
                        mCommentAdapter = new NewsCommentAdapter((ForumDetailActivity) mView, mCommentList, mBizType);
                    } else {
                        mCommentAdapter.notifyDataSetChanged();
                    }
                    requestBrowseCount(); //
                    if (item.score > 0) {
                        EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_ITEM_CANCEL_MARKED, mUuid));
                    }

                    DeleteCount deleteCount = new DeleteCount(mUuid, response1.deleteCommentOrReplyItem.delCount);
                    Log.e("删除数量", "onSuccess: " + deleteCount.deleteCount);
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY, deleteCount));
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

    public void deleteReply(final ReplyItem mComment) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_DELETE_COMMENT_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new DeleteCommentsApiParameter(mItem.postUuid, mComment.commentUuid).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);

                DeleteCommentOrReplyResponse response1 = new DeleteCommentOrReplyResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    // 刷新评论
                    mNextPage = 0;
                    CommonToast.getInstance("删除成功").show();
                    requestPostComments(PAGE_SIZE);
                    mItem.commentCount -= response1.deleteCommentOrReplyItem.delCount;
                    if (mItem.commentCount < 0) {
                        mItem.commentCount = 0;
                    }
                    mView.setCommentText(Utils.formateNumber(mItem.commentCount));
                    DeleteCount deleteCount = new DeleteCount(mUuid, response1.deleteCommentOrReplyItem.delCount);
                    Log.e("删除数量", "onSuccess: " + deleteCount.deleteCount);
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY, deleteCount));
//                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY, mUuid));
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

    public void requestFollowUser() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_FOLLOW_USER_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new FollowOtherApiParameter(mItem.userUuid).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new HttpListener<String>() {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                RequestOtherInfoResponse response1 = new RequestOtherInfoResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    mItem.isFollow = 1;
                    CommonToast.getInstance("关注成功").show();
                    mView.updateView();

                    // 更新其他页面
                    FriendsItem friendsItem = new FriendsItem();
                    friendsItem.uuid = mItem.userUuid;
                    friendsItem.followType = 1;
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, friendsItem));

                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_get_fresh_news_fail_alert),
                        Toast.LENGTH_SHORT).show();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    // 更新评论数量
    private void requestBrowseCount() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_GET_BROWSE_COUNT_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestPostDetailApiParameter(mUuid).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                RequestPostDetailResponse response1 = new RequestPostDetailResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    mItem.commentCount = response1.freshNewsItem.commentCount;
                    mView.setCommentText(Utils.formateNumber(mItem.commentCount));
                    mView.updateCommentView(mCommentList.size());
                } else if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_NULL_DATA)) {

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

    //点赞评论
    public void requestLikeComment(String commentuuid) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_LIKE_POST_COMMENT_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new CommentLikeOperaApiParameter(mItem.postUuid, commentuuid, mItem.bizType + "", mItem.renderType + "").getRequestBody());
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
                    for (int i = 0; i < mCommentList.size(); i++) {
                        if ((mCommentList.get(i).commentUuid.equals(commentuuid))) {
                            mCommentList.get(i).isLike = 1;
                            mCommentList.get(i).likeCount++;
                        }
                    }
                    if (mCommentAdapter == null) {
                        mCommentAdapter = new NewsCommentAdapter((ForumDetailActivity) mView, mCommentList, mBizType);
                    } else {
                        mCommentAdapter.notifyDataSetChanged();
                    }
                    //CommonToast.getInstance("点赞成功").show();
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

    //取消点赞评论
    public void cancelRequestLikeComment(String commentUuid) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_CANCEL_LIKE_POST_COMMENT_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new CancelCommentLikeOperaApiParameter(commentUuid).getRequestBody());
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
                    for (int i = 0; i < mCommentList.size(); i++) {
                        if (mCommentList.get(i).commentUuid == commentUuid) {
                            mCommentList.get(i).isLike = 0;
                            mCommentList.get(i).likeCount--;
                        }
                    }
                    if (mCommentAdapter == null) {
                        mCommentAdapter = new NewsCommentAdapter((ForumDetailActivity) mView, mCommentList, mBizType);
                    } else {
                        mCommentAdapter.notifyDataSetChanged();
                    }
                    //CommonToast.getInstance("已取消点赞").show();
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

    //采纳回答
    public void acceptAnswer(String commentUuid) {

//        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
//                + ApiContant.REQUEST_accept);
//        postRequest.setMethod(HttpMethods.Post);
//        postRequest.setHttpBody(new AcceptAnswerParameter(mItem.postUuid, commentUuid, mItem.userUuid).getRequestBody());
//        String sessionKey = SharedPreferencesManager.getString(
//                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
//        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
//
//        postRequest.setHttpListener(new MyHttpListener<String>(mView.getDefaultContext()) {
//            @Override
//            public void onSuccess(String s, Response<String> response) {
//                super.onSuccess(s, response);
//                ResponseData response1 = new ResponseData(s);
//                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
//                    CommonToast.getInstance(response1.mMsg).show();
//                } else {
//                    for (int i = 0; i < mCommentList.size(); i++) {
//                        if (mCommentList.get(i).commentUuid.equals(commentUuid)) {
//
//                            //已采纳
//                            mCommentList.get(i).isResolved = 1;
//                            mItem.isResolved = 1;
//                            mCommentList.set(i, mCommentList.set(0, mCommentList.get(i)));
//                            //把item放在集合首位
//
//                        }
//                    }
//                    if (mCommentAdapter == null) {
//                        mCommentAdapter = new NewsCommentAdapter((ForumDetailActivity) mView, mCommentList, mBizType);
//                    } else {
//                        mCommentAdapter.notifyDataSetChanged();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                Log.e("acceptAnswer", "onFailure: e =" + e.toString() + ", response = " + response.getResult());
//            }
//        });
//        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    public FreshNewItem getDetailItem() {

        return mItem;
    }
}
