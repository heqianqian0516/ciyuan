package com.ciyuanplus.mobile.module.comment_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.news.SelectDeleteCommentActivity;
import com.ciyuanplus.mobile.adapter.CommentDetailReplyAdapter;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.forum_detail.daily_detail.DailyDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.food_detail.FoodDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.note_detail.NoteDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.post_detail.PostDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.stuff_detail.StuffDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.twitter_detail.TwitterDetailActivity;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.module.others.new_others.OthersActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.CommentItem;
import com.ciyuanplus.mobile.net.bean.DeleteCount;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.net.bean.ReplyItem;
import com.ciyuanplus.mobile.net.bean.UserInfoItem;
import com.ciyuanplus.mobile.net.parameter.AddCommentApiParameter;
import com.ciyuanplus.mobile.net.parameter.CancelCommentLikeOperaApiParameter;
import com.ciyuanplus.mobile.net.parameter.CommentLikeOperaApiParameter;
import com.ciyuanplus.mobile.net.parameter.DeleteCommentsApiParameter;
import com.ciyuanplus.mobile.net.parameter.RequestCommentDetailApiParameter;
import com.ciyuanplus.mobile.net.parameter.RequestPostReplyApiParameter;
import com.ciyuanplus.mobile.net.response.DeleteCommentOrReplyResponse;
import com.ciyuanplus.mobile.net.response.RequestCommentDetailResponse;
import com.ciyuanplus.mobile.net.response.RequestPostReplyResponse;
import com.ciyuanplus.mobile.pulltorefresh.XScrollView;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.ListViewForScrollView;
import com.ciyuanplus.mobile.widget.RoundImageView;
import com.ciyuanplus.mobile.widget.TitleBarView;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author Alen
 * @date 2017/6/21
 */

public class CommentDetailActivity extends MyBaseActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.m_comment_detail_comment_input)
    EditText mCommentDetailCommentInput;
    @BindView(R.id.m_comment_detail_comment_submit)
    TextView mCommentDetailCommentSubmit;
    @BindView(R.id.m_comment_detail_bottom_input_lp)
    RelativeLayout mCommentDetailBottomInputLp;
    @BindView(R.id.m_comment_detail_root)
    RelativeLayout mCommentDetailRoot;
    @BindView(R.id.m_comment_detail_comment_scroll_view)
    XScrollView mCommentDetailCommentScrollView;
    @BindView(R.id.title_bar)
    TitleBarView mTitleBar;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private TextView mCommentDetailCommentLikeNumber;
    private ImageView mCommentDetailCommentLikeImage;
    private ListViewForScrollView mCommentDetailCommentChild;
    private TextView mCommentDetailCommentOriginPost;
    private RoundImageView mCommentDetailCommentHeadImage;
    private ImageView mCommentDetailCommentHeadSex;
    private TextView mCommentDetailCommentNameText;
    private TextView mCommentDetailCommentContentText;


    private int mNexgtPage = 0;
    private final int PAGE_SIZE = 20;
    private String mPostUuid;
    private String mCommentUuid;
    private String mReplyCommentId;
    private String mTargetCommentId;
    private String mReplyToUserId;

    private CommentItem mItem;
    private CommentDetailReplyAdapter mAdapter;
    private final ArrayList<ReplyItem> mList = new ArrayList<>();

    private ReplyItem mTryDeleteReply;
    private String mFromType;
    private int mBizeType;
    private int mRenderType;
    private Unbinder mUnbinder;
    private TextView mTime;
    private final MyOnClickListener myOnClickListener = new MyOnClickListener() {
        @Override
        public void performRealClick(View v) {
            int id = v.getId();
            if (id == R.id.m_comment_detail_comment_head_image) {
                // 如果是匿名   或者 是自己发布的帖子，不允许进入他人页面
                if (mItem.isAnonymous == 1 || Utils.isStringEquals(mItem.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {
                    return;
                }
                Intent intent = new Intent(CommentDetailActivity.this, OthersActivity.class);
                intent.putExtra(Constants.INTENT_USER_ID, mItem.userUuid);
                startActivity(intent);
            } else if (id == R.id.m_comment_detail_comment_content_text) {
                if (Utils.isStringEquals(mItem.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {
                    tryDelete();
                } else {
                    replyCurrent();
                }
            } else if (id == R.id.m_comment_detail_comment_like_image) {
                if (!LoginStateManager.isLogin()) {
                    Intent intent = new Intent(CommentDetailActivity.this, LoginActivity.class);
                    CommentDetailActivity.this.startActivity(intent);
                    return;
                }
                if (mItem.isLike == 1) {
                    cancelRequestLikeComment(mItem.commentUuid);
                } else {
                    requestLikeComment(mItem.commentUuid);
                }
            } else if (id == R.id.m_comment_detail_comment_origin_post) {
                // 这里要区分是不是从帖子详情页进来的
                if (Utils.isStringEquals(mFromType, "UserMessageListActivity")) {

                    if (mBizeType == 0) {

                        Intent intent1 = new Intent(CommentDetailActivity.this, TwitterDetailActivity.class);
                        intent1.putExtra(Constants.INTENT_NEWS_ID_ITEM, mPostUuid);
                        intent1.putExtra(Constants.INTENT_BIZE_TYPE, mBizeType);
                        startActivity(intent1);
                    } else if (FreshNewItem.FRESH_ITEM_STUFF == mBizeType) {
                        Intent intent1 = new Intent(CommentDetailActivity.this, StuffDetailActivity.class);
                        intent1.putExtra(Constants.INTENT_NEWS_ID_ITEM, mPostUuid);
                        intent1.putExtra(Constants.INTENT_BIZE_TYPE, mBizeType);
                        CommentDetailActivity.this.startActivity(intent1);
                    } else if (mBizeType == FreshNewItem.FRESH_ITEM_DAILY) {
                        Intent intent1 = new Intent(CommentDetailActivity.this, DailyDetailActivity.class);
                        intent1.putExtra(Constants.INTENT_NEWS_ID_ITEM, mPostUuid);
                        intent1.putExtra(Constants.INTENT_BIZE_TYPE, mBizeType);
                        CommentDetailActivity.this.startActivity(intent1);
                    } else if (mBizeType == 4) {
                        // 长文和说说

                        Intent intent1 = new Intent(CommentDetailActivity.this, PostDetailActivity.class);
                        intent1.putExtra(Constants.INTENT_NEWS_ID_ITEM, mPostUuid);
                        intent1.putExtra(Constants.INTENT_BIZE_TYPE, mBizeType);
                        CommentDetailActivity.this.startActivity(intent1);

                    } else if (mBizeType == FreshNewItem.FRESH_ITEM_NEWS
                            || mBizeType == FreshNewItem.FRESH_ITEM_NOTE
                            || mBizeType == FreshNewItem.FRESH_ITEM_NEWS_COLLECTION) {
                        Intent intent1 = new Intent(CommentDetailActivity.this, NoteDetailActivity.class);
                        intent1.putExtra(Constants.INTENT_NEWS_ID_ITEM, mPostUuid);
                        intent1.putExtra(Constants.INTENT_BIZE_TYPE, mBizeType);
                        CommentDetailActivity.this.startActivity(intent1);
                    } else if (mBizeType == FreshNewItem.FRESH_ITEM_FOOD
                            || mBizeType == FreshNewItem.FRESH_ITEM_LIVE
                            || mBizeType == FreshNewItem.FRESH_ITEM_COMMENT) {
                        Intent intent1 = new Intent(CommentDetailActivity.this, FoodDetailActivity.class);
                        intent1.putExtra(Constants.INTENT_BIZE_TYPE, mBizeType);
                        intent1.putExtra(Constants.INTENT_NEWS_ID_ITEM, mPostUuid);
                        CommentDetailActivity.this.startActivity(intent1);
                    }
                    finish();
                } else {
                    onBackPressed();
                }
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_comment_detail);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        mPostUuid = getIntent().getStringExtra(Constants.INTENT_NEWS_ID_ITEM);
        mCommentUuid = getIntent().getStringExtra(Constants.INTENT_COMMENT_ID_ITEM);
        mFromType = getIntent().getStringExtra(Constants.INTENT_ACTIVITY_TYPE);
        mBizeType = getIntent().getIntExtra(Constants.INTENT_BIZE_TYPE, 0);
        mRenderType = getIntent().getIntExtra(Constants.INTENT_RENDER_TYPE, 0);
        this.initView();
        this.requestCommentDetail();
    }

    private void initView() {
        mUnbinder = ButterKnife.bind(this);

        initTitleBar();
        mCommentDetailCommentScrollView.setPullLoadEnable(false);
        mCommentDetailCommentScrollView.setPullRefreshEnable(false);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater != null ? inflater.inflate(R.layout.layout_comment_detail_top, null) : null;
        mCommentDetailCommentScrollView.setView(content);

        mCommentDetailCommentLikeNumber = content.findViewById(R.id.m_comment_detail_comment_like_number);
        mCommentDetailCommentLikeImage = content.findViewById(R.id.m_comment_detail_comment_like_image);
        mCommentDetailCommentChild = content.findViewById(R.id.m_comment_detail_comment_child);
        mCommentDetailCommentOriginPost = content.findViewById(R.id.m_comment_detail_comment_origin_post);
        mCommentDetailCommentHeadImage = content.findViewById(R.id.m_comment_detail_comment_head_image);
        mCommentDetailCommentHeadSex = content.findViewById(R.id.m_comment_detail_comment_head_sex);
        mCommentDetailCommentNameText = content.findViewById(R.id.m_comment_detail_comment_name_text);
        mCommentDetailCommentContentText = content.findViewById(R.id.m_comment_detail_comment_content_text);
        mTime = content.findViewById(R.id.tv_time);

        mCommentDetailCommentHeadImage.setOnClickListener(myOnClickListener);
        mCommentDetailCommentContentText.setOnClickListener(myOnClickListener);
        mCommentDetailCommentOriginPost.setOnClickListener(myOnClickListener);
        mCommentDetailCommentLikeImage.setOnClickListener(myOnClickListener);
//        mCommentDetailCommentChild.setmAutoFit(true);
        mAdapter = new CommentDetailReplyAdapter(CommentDetailActivity.this, mList);
        mCommentDetailCommentChild.setAdapter(mAdapter);
        mCommentDetailCommentInput.addTextChangedListener(new TextWatcher() {// 监听输入框的当前文字数量， 来变化发送按钮状态
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mCommentDetailCommentSubmit.setEnabled(true);
                } else {
                    mCommentDetailCommentSubmit.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        //监听global layout的大小变化  
        mCommentDetailRoot.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            //现在认为只要控件将Activity向上推的高度超过了50屏幕高，就认为软键盘弹起  
            if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom < 50)) {
                // 关闭
                mReplyCommentId = "";
                mReplyToUserId = "";
                mTargetCommentId = "";
                mCommentDetailBottomInputLp.setVisibility(View.GONE);

            }
        });
    }

    private void initTitleBar() {

        mTitleBar.setTitle("消息");
        mTitleBar.setOnBackListener(v -> onBackPressed());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_SELECT_DELETE_COMMENT && resultCode == RESULT_OK) {
            String opera = data.getStringExtra(SelectDeleteCommentActivity.SELECTED);
            if (Utils.isStringEquals(SelectDeleteCommentActivity.DELETE_COMENT, opera)) {
                deleteComment();
            } else if (Utils.isStringEquals(SelectDeleteCommentActivity.DELETE_REPLY, opera)) {
                deleteReply(mTryDeleteReply);
            }
        }
    }

    private void requestCommentDetail() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_GET_COMMENTS_DETAIL_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestCommentDetailApiParameter(mPostUuid, mCommentUuid).getRequestBody());
//        String sessionKey = SharedPreferencesManager.getString(
//                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
//        if(!Utils.isStringEmpty(sessionKey))postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(this) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                finishRefreshAndLoadMore();
                // 必须要加的

                RequestCommentDetailResponse response1 = new RequestCommentDetailResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    mItem = response1.commentItem;
                    updateView();
                }
            }

            //
            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                // 必须要加的
                finishRefreshAndLoadMore();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    private void finishRefreshAndLoadMore() {
        mRefreshLayout.finishLoadMore();
        mRefreshLayout.finishRefresh();
    }

    // 请求新鲜事评论列表
    private void requestPostComments(int pageSize) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_GET_POST_COMMENTS_LIST_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestPostReplyApiParameter(mPostUuid, mNexgtPage + "", pageSize + "", mItem.commentUuid).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(this) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                finishRefreshAndLoadMore();
                // 必须要加的

                RequestPostReplyResponse response1 = new RequestPostReplyResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    if (mNexgtPage == 0) mList.clear();
                    Collections.addAll(mList, response1.replyListItem.list);
                    if (response1.replyListItem.list.length >= pageSize) {
                        mRefreshLayout.setEnableLoadMore(true);
                        mNexgtPage++;
                    }

                    mAdapter.notifyDataSetChanged();
                }
            }

            //
            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                // 必须要加的
                finishRefreshAndLoadMore();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    private void deleteReply(final ReplyItem mComment) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_DELETE_COMMENT_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new DeleteCommentsApiParameter(mPostUuid, mComment.commentUuid).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(this) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);

                DeleteCommentOrReplyResponse response1 = new DeleteCommentOrReplyResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    // 刷新评论
                    mList.remove(mComment);
                    CommonToast.getInstance("删除成功").show();
                    mAdapter.notifyDataSetChanged();

                    DeleteCount deleteCount = new DeleteCount(mPostUuid, response1.deleteCommentOrReplyItem.delCount);

                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY, deleteCount));

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

    private void updateView() {
        if (mItem.isAnonymous == 1) {
            mCommentDetailCommentHeadSex.setVisibility(View.GONE);
        } else {
            mCommentDetailCommentHeadSex.setVisibility(View.VISIBLE);
        }
        mCommentDetailCommentNameText.setText(mItem.nickname);
        Glide.with(this).load(Constants.IMAGE_LOAD_HEADER + mItem.photo).apply(new RequestOptions().placeholder(R.drawable.ic_default_image_007).error(R.mipmap.imgfail)
                .dontAnimate().centerCrop()).into(mCommentDetailCommentHeadImage);
//        ImageLoader.getInstance().displayImage(Constants.IMAGE_LOAD_HEADER + mItem.photo, mCommentDetailCommentHeadImage, new ImageSize(150,150));
        mCommentDetailCommentContentText.setText(mItem.contentText);
        mTime.setText(Utils.getFormattedTimeString(mItem.createTime));
        mCommentDetailCommentHeadSex.setImageResource(UserInfoItem.getSexImageResource(mItem.sex));
        if (Utils.isStringEquals(mItem.userUuid, UserInfoData.getInstance().getUserInfoItem().uuid)) {
            mCommentDetailBottomInputLp.setVisibility(View.GONE);
        } else mCommentDetailBottomInputLp.setVisibility(View.VISIBLE);
        requestPostComments(PAGE_SIZE);
        mReplyCommentId = mItem.commentUuid;
        mReplyToUserId = mItem.userUuid;
        mTargetCommentId = mItem.commentUuid;
        mCommentDetailCommentLikeNumber.setText(mItem.likeCount + "");
        mCommentDetailCommentLikeImage.setImageResource(mItem.isLike == 1 ? R.drawable.icon_like : R.drawable.icon_unlike);
//        mCommentDetailCommentLikeImage.setImageResource(mItem.isLike == 1 ? R.drawable.icon_list_like : R.drawable.icon_list_unlike);

    }

    //删除评论
    private void deleteComment() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_DELETE_COMMENT_URL);
        postRequest.setHttpBody(new DeleteCommentsApiParameter(mPostUuid, mItem.commentUuid).getRequestBody());
        postRequest.setMethod(HttpMethods.Post);
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(this) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);

                DeleteCommentOrReplyResponse response1 = new DeleteCommentOrReplyResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    CommonToast.getInstance("删除成功").show();

                    DeleteCount deleteCount = new DeleteCount(mPostUuid, response1.deleteCommentOrReplyItem.delCount);

                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_DELETE_COMMENT_OR_REPLY, deleteCount));
                    finish();
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


    private void tryDelete() {

        Intent intent = new Intent(this, SelectDeleteCommentActivity.class);
        intent.putExtra(Constants.INTENT_ACTIVITY_TYPE, SelectDeleteCommentActivity.DELETE_COMENT);
        this.startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_DELETE_COMMENT);
    }

    public void tryDelete(ReplyItem item) {

        mTryDeleteReply = item;// 先保存一下
        Intent intent = new Intent(this, SelectDeleteCommentActivity.class);
        intent.putExtra(Constants.INTENT_ACTIVITY_TYPE, SelectDeleteCommentActivity.DELETE_REPLY);
        this.startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_DELETE_COMMENT);
    }

    private void replyCurrent() {
        if (!LoginStateManager.isLogin()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        mCommentDetailCommentInput.setHint("");
        mCommentDetailCommentInput.setFocusable(true);
        mCommentDetailCommentInput.requestFocus();
        mCommentDetailBottomInputLp.setVisibility(View.VISIBLE);
        showKeyboard(mCommentDetailCommentInput);
        mReplyCommentId = mItem.commentUuid;
        mReplyToUserId = mItem.userUuid;
        mTargetCommentId = mItem.commentUuid;
    }

    // 当回复被点击时候的回调
    public void replyReply(ReplyItem item) {
        if (!LoginStateManager.isLogin()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        mCommentDetailCommentInput.setHint("回复 " + item.sendNickname);
        mCommentDetailCommentInput.setFocusable(true);
        mCommentDetailCommentInput.requestFocus();
        mCommentDetailBottomInputLp.setVisibility(View.VISIBLE);
//        if (KeyboardUtils.isSoftInputVisible(this)){
//            hideKeyboard();
//        }else {
//            showKeyboard(mCommentDetailCommentInput);
//        }
        if (KeyboardUtils.isSoftInputVisible(this)) {
            showKeyboard(mCommentDetailCommentInput);
        }

        mReplyCommentId = item.parentCommentUuid;
        mTargetCommentId = item.commentUuid;
        mReplyToUserId = item.sendUserUuid;
    }

    //显示虚拟键盘
    private void showKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);

    }

    //隐藏虚拟键盘
    private void HideKeyboard(View v) {
        //mCommentDetailBottomInputLp.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

        }
    }

    // 提交帖子的回复内容
    private void submitReply(String comment, String commentId, String targetCommentId, String toUserUuid) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_POST_COMMENTS_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new AddCommentApiParameter(mPostUuid, commentId, targetCommentId, comment, "2", toUserUuid).getRequestBody());
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
                    CommonToast.getInstance("评论成功").show();
                    mCommentDetailCommentInput.setText("");
                    mNexgtPage = 0;
                    requestPostComments(PAGE_SIZE);// 成功之后需要刷新评论列表
                    EventCenterManager.synSendEvent(new EventCenterManager.EventMessage(Constants.EVENT_MESSAGE_ADD_COMMENT_OR_REPLY, mPostUuid));
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

    @OnClick({R.id.m_comment_detail_comment_submit})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {

            case R.id.m_comment_detail_comment_submit:
                HideKeyboard(mCommentDetailCommentInput);
                if (!LoginStateManager.isLogin()) {
                    Intent intent = new Intent(CommentDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                String comment = mCommentDetailCommentInput.getText().toString();
                if (Utils.isStringEmpty(comment)) {
                    return;
                } else {
                    submitReply(comment, mReplyCommentId, mTargetCommentId, mReplyToUserId);
                }
                break;
        }
    }

    //点赞评论
    private void requestLikeComment(String commentUuid) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_LIKE_POST_COMMENT_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new CommentLikeOperaApiParameter(mPostUuid, commentUuid, mBizeType + "", mRenderType + "").getRequestBody());
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
                    mItem.likeCount++;
                    mItem.isLike = 1;
                    mCommentDetailCommentLikeNumber.setText(mItem.likeCount + "");
                    mCommentDetailCommentLikeImage.setImageResource(mItem.isLike == 1 ? R.drawable.icon_like : R.drawable.icon_unlike);

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
    private void cancelRequestLikeComment(String commentUuid) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_CANCEL_LIKE_POST_COMMENT_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new CancelCommentLikeOperaApiParameter(commentUuid).getRequestBody());
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
                    mItem.isLike = 0;
                    mItem.likeCount--;
                    mCommentDetailCommentLikeNumber.setText(mItem.likeCount + "");
                    mCommentDetailCommentLikeImage.setImageResource(mItem.isLike == 1 ? R.drawable.icon_like : R.drawable.icon_unlike);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mNexgtPage = 0;
        requestPostComments(PAGE_SIZE);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        requestPostComments(PAGE_SIZE);
    }
}
