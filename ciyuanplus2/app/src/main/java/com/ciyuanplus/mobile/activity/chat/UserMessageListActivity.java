package com.ciyuanplus.mobile.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.adapter.UserNoticesAdapter;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.comment_detail.CommentDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.daily_detail.DailyDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.food_detail.FoodDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.note_detail.NoteDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.stuff_detail.StuffDetailActivity;
import com.ciyuanplus.mobile.module.forum_detail.twitter_detail.TwitterDetailActivity;
import com.ciyuanplus.mobile.module.others.new_others.OthersActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.net.bean.NoticeItem;
import com.ciyuanplus.mobile.net.parameter.GetNoticeListApiParameter;
import com.ciyuanplus.mobile.net.response.NoticeListResponse;
import com.ciyuanplus.mobile.pulltorefresh.XListView;
import com.ciyuanplus.mobile.statistics.StatisticsConstant;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
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
import java.util.Arrays;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/6/22.
 * <p>
 * 新粉丝列表页面   评论和赞  收藏
 */

public class UserMessageListActivity extends MyBaseActivity implements OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.m_user_message_list)
    XListView mUserMessageList;
    @BindView(R.id.m_user_message_null_lp)
    LinearLayout mUserMessageNullLp;
    @BindView(R.id.m_user_message_list_common_title)
    TitleBarView m_js_common_title;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.tv_notice)
    TextView noticeText;

    private String mType;
    private UserNoticesAdapter mAdapter;
    private final ArrayList<NoticeItem> mlist = new ArrayList<>();

    private int mPage = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_user_message_list);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        mType = getIntent().getStringExtra(Constants.INTENT_ACTIVITY_TYPE);

        if (Utils.isStringEquals(mType, GetNoticeListApiParameter.TYPE_NEWS)) {
            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_COMMENTS, StatisticsConstant.OP_COMMENTS_PAGE_LOAD);
        } else {
            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_FANS, StatisticsConstant.OP_FANS_PAGE_LOAD);
        }

        this.initView();
        this.requestList();
    }

    private void requestList() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_USER_NOTICE_LIST_URL);
        postRequest.setMethod(HttpMethods.Post);
        int PAGE_SIZE = 20;
        postRequest.setHttpBody(new GetNoticeListApiParameter(mPage + "", PAGE_SIZE + "", mType).getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(this) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);

                finishRefreshAndLoadMore();
                NoticeListResponse response1 = new NoticeListResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK) && response1.noticeListInfo.list != null) {
                    if (response1.noticeListInfo.list.length > 0) {
                        if (mPage == 0) mlist.clear();
                        mlist.addAll(Arrays.asList(response1.noticeListInfo.list));
                        mAdapter.notifyDataSetChanged();
                        mPage++;
                        updateView();
                    }
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
//                mUserMessageList.stopRefresh();
//                mUserMessageList.stopLoadMore();
                finishRefreshAndLoadMore();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);

        m_js_common_title.setOnBackListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {
                onBackPressed();
            }
        });
        m_js_common_title.setTitle(Utils.isStringEquals(mType, GetNoticeListApiParameter.TYPE_NEWS) ? "评论和赞" : "新粉丝");
        noticeText.setText(Utils.isStringEquals(mType, GetNoticeListApiParameter.TYPE_NEWS) ? "暂无消息" : "暂无新粉丝");

        mAdapter = new UserNoticesAdapter(this, mlist);
        this.mUserMessageList.setAdapter(mAdapter);
        mUserMessageList.setPullLoadEnable(false);
        mUserMessageList.setPullRefreshEnable(false);
//        mUserMessageList.setXListViewListener(this);

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);

        mUserMessageList.setOnItemClickListener((parent, view, position, id) -> {
            if (Utils.isStringEquals(mType, GetNoticeListApiParameter.TYPE_NEWS)) {
                StatisticsManager.onEventInfo(StatisticsConstant.MODULE_COMMENTS, StatisticsConstant.OP_COMMENTS_LIST_ITEM_CLICK);
            } else {
                StatisticsManager.onEventInfo(StatisticsConstant.MODULE_FANS, StatisticsConstant.OP_FANS_LIST_ITEM_CLICK);
            }
            if (id == -1) {
                return;
            }
            int index = (int) id;
            Log.e("UserMessageListActivity", "position = " + position);
            Log.e("UserMessageListActivity", "id = " + id);

            NoticeItem item = mAdapter.getItem(index);
            Log.e("UserMessageListActivity", "item = " + item.toString());

            if (Utils.isStringEquals(mType, GetNoticeListApiParameter.TYPE_FOLLOW)) { // 如果是被关注了
                // 如果是匿名   或者 是自己发布的帖子，不允许进入他人页面
                if (Utils.isStringEquals(item.fromUserUuid, UserInfoData.getInstance().getUserInfoItem().uuid))
                    return;
                Intent intent = new Intent(UserMessageListActivity.this, OthersActivity.class);
                intent.putExtra(Constants.INTENT_USER_ID, item.fromUserUuid);
                intent.putExtra(Constants.INTENT_BIZE_TYPE, item.bizType);
                UserMessageListActivity.this.startActivity(intent);
            } else if (Utils.isStringEquals(mType, GetNoticeListApiParameter.TYPE_NEWS)) { // 如果是评论和赞
                if (item.noticeType == 1) { // 评论
                    Intent intent = new Intent(UserMessageListActivity.this, CommentDetailActivity.class);
                    intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.bizUuid);
                    intent.putExtra(Constants.INTENT_COMMENT_ID_ITEM, item.currentBizUuid);
                    intent.putExtra(Constants.INTENT_ACTIVITY_TYPE, "UserMessageListActivity");
                    intent.putExtra(Constants.INTENT_RENDER_TYPE, item.renderType);
                    intent.putExtra(Constants.INTENT_BIZE_TYPE, item.bizType);
                    UserMessageListActivity.this.startActivity(intent);
                } else if (item.noticeType == 2 || item.noticeType == 9) {// 赞
                    if (item.bizType == FreshNewItem.FRESH_ITEM_STUFF) {//宝贝
                        Intent intent = new Intent(UserMessageListActivity.this, StuffDetailActivity.class);
                        intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.bizUuid);
                        intent.putExtra(Constants.INTENT_BIZE_TYPE, item.bizType);
                        UserMessageListActivity.this.startActivity(intent);
                    } else if (item.bizType == FreshNewItem.FRESH_ITEM_DAILY) {
                        Intent intent = new Intent(UserMessageListActivity.this, DailyDetailActivity.class);
                        intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.bizUuid);
                        intent.putExtra(Constants.INTENT_BIZE_TYPE, item.bizType);
                        UserMessageListActivity.this.startActivity(intent);
                    } else if (item.bizType == FreshNewItem.FRESH_ITEM_POST || item.bizType == FreshNewItem.FRESH_ITEM_ANSWER) { // 长文和说说

                        Intent intent = new Intent(UserMessageListActivity.this, TwitterDetailActivity.class);
                        intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.bizUuid);
                        intent.putExtra(Constants.INTENT_BIZE_TYPE, item.bizType);
                        UserMessageListActivity.this.startActivity(intent);

                    } else if (item.bizType == FreshNewItem.FRESH_ITEM_NEWS
                            || item.bizType == FreshNewItem.FRESH_ITEM_NOTE
                            || item.bizType == FreshNewItem.FRESH_ITEM_NEWS_COLLECTION) {
                        Intent intent = new Intent(UserMessageListActivity.this, NoteDetailActivity.class);
                        intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.bizUuid);
                        intent.putExtra(Constants.INTENT_BIZE_TYPE, item.bizType);
                        UserMessageListActivity.this.startActivity(intent);
                    } else if (item.bizType == FreshNewItem.FRESH_ITEM_FOOD
                            || item.bizType == FreshNewItem.FRESH_ITEM_LIVE
                            || item.bizType == FreshNewItem.FRESH_ITEM_COMMENT) {
                        Intent intent = new Intent(UserMessageListActivity.this, FoodDetailActivity.class);
                        intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.bizUuid);
                        intent.putExtra(Constants.INTENT_BIZE_TYPE, item.bizType);
                        UserMessageListActivity.this.startActivity(intent);
                    }
                } else if (item.noticeType == 5) {// 回复
                    Intent intent = new Intent(UserMessageListActivity.this, CommentDetailActivity.class);
                    intent.putExtra(Constants.INTENT_NEWS_ID_ITEM, item.bizUuid);
                    intent.putExtra(Constants.INTENT_COMMENT_ID_ITEM, item.currentBizUuid);
                    intent.putExtra(Constants.INTENT_ACTIVITY_TYPE, "UserMessageListActivity");
                    intent.putExtra(Constants.INTENT_RENDER_TYPE, item.renderType);
                    intent.putExtra(Constants.INTENT_BIZE_TYPE, item.bizType);

                    Log.e("UserMessageListActivity", "INTENT_NEWS_ID_ITEM = " + item.bizUuid);
                    Log.e("UserMessageListActivity", "INTENT_COMMENT_ID_ITEM = " + item.currentBizUuid);
                    Log.e("UserMessageListActivity", "NTENT_RENDER_TYPE = " + item.renderType);
                    Log.e("UserMessageListActivity", "INTENT_BIZE_TYPE = " + item.bizType);

                    UserMessageListActivity.this.startActivity(intent);
                }
            }
        });
    }


    private void updateView() {
        if (mlist.size() > 0) {
            mUserMessageNullLp.setVisibility(View.GONE);
            this.mUserMessageList.setVisibility(View.VISIBLE);
        } else {
            mUserMessageNullLp.setVisibility(View.VISIBLE);
            this.mUserMessageList.setVisibility(View.GONE);
        }
    }


    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mPage = 0;
        requestList();
    }

    @Override
    public void onLoadMore(RefreshLayout refreshlayout) {
        requestList();
    }

    private void finishRefreshAndLoadMore() {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
    }
}
