package com.ciyuanplus.mobile.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.adapter.SystemNoticesAdapter;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.NoticeItem;
import com.ciyuanplus.mobile.net.parameter.DeleteSystemNoticeApiParameter;
import com.ciyuanplus.mobile.net.parameter.GetNoticeListApiParameter;
import com.ciyuanplus.mobile.net.response.NoticeListResponse;
import com.ciyuanplus.mobile.statistics.StatisticsConstant;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.SwipeXListView;
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

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/6/22.
 */

public class SystemMessageListActivity extends MyBaseActivity implements OnLoadMoreListener, OnRefreshListener {

    @BindView(R.id.m_system_message_list_list)
    SwipeXListView mSystemMessageListList;
    @BindView(R.id.m_system_message_null_lp)
    LinearLayout mSystemMessageNullLp;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.title_bar)
    TitleBarView mTitleBarView;

    private SystemNoticesAdapter mAdapter;
    private final ArrayList<NoticeItem> mlist = new ArrayList<>();

    private int mPage = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_system_message_list);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();
        this.requestList();
    }

    private void requestList() {
        int PAGE_SIZE = 20;
        if (!LoginStateManager.isLogin()) {
            StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_TEMP_USER_NOTICE_LIST_URL);
            postRequest.setMethod(HttpMethods.Post);
            postRequest.setHttpBody(new GetNoticeListApiParameter(mPage + "", PAGE_SIZE + "", GetNoticeListApiParameter.TYPE_SYSTEM).getRequestBody());
            postRequest.setHttpListener(new MyHttpListener<String>(this) {
                @Override
                public void onSuccess(String s, Response<String> response) {
                    super.onSuccess(s, response);
                    finishRefreshAndLoadMore();
                    NoticeListResponse response1 = new NoticeListResponse(s);
                    if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK) && response1.noticeListInfo.list != null) {
                        if (response1.noticeListInfo.list.length > 0) {
                            if (mPage == 0) mlist.clear();
                            Collections.addAll(mlist, response1.noticeListInfo.list);
                            mRefreshLayout.setEnableLoadMore(mlist.size()>=20);
                            mAdapter.notifyDataSetChanged();
                            mPage++;

                        }
                        updateView();
                    } else {
                        CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                        updateView();
                    }
                }

                @Override
                public void onFailure(HttpException e, Response<String> response) {
                    super.onFailure(e, response);
                    finishRefreshAndLoadMore();
                    finishRefreshAndLoadMore();
                    updateView();
                }
            });
            LiteHttpManager.getInstance().executeAsync(postRequest);
        } else {
            StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_USER_NOTICE_LIST_URL);
            postRequest.setMethod(HttpMethods.Post);
            postRequest.setHttpBody(new GetNoticeListApiParameter(mPage + "", PAGE_SIZE + "", GetNoticeListApiParameter.TYPE_SYSTEM).getRequestBody());
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
                            Collections.addAll(mlist, response1.noticeListInfo.list);
                            mAdapter.notifyDataSetChanged();
                            mPage++;

                        }
                        updateView();
                    } else {
                        CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(HttpException e, Response<String> response) {
                    super.onFailure(e, response);
                    finishRefreshAndLoadMore();
                    finishRefreshAndLoadMore();
                }
            });
            LiteHttpManager.getInstance().executeAsync(postRequest);
        }

    }

    private void updateView() {
        if (mlist.size() > 0) {
            mSystemMessageNullLp.setVisibility(View.GONE);
            this.mSystemMessageListList.setVisibility(View.VISIBLE);
        } else {
            mSystemMessageNullLp.setVisibility(View.VISIBLE);
            this.mSystemMessageListList.setVisibility(View.GONE);
        }
    }

    private void initView() {
        ButterKnife.bind(this);

        initTitleBar();
        mAdapter = new SystemNoticesAdapter(this, mlist);
        this.mSystemMessageListList.setAdapter(mAdapter);
        mSystemMessageListList.setOnItemClickListener((adapterView, view, i, l) -> {
            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_SYSTEM_MESSAGE, StatisticsConstant.OP_SYSTEM_MESSAGE_LIST_ITEM_CLICK);
            if (l == -1) {
                return;
            }
            int postion = (int) l;
            Intent intent = new Intent(SystemMessageListActivity.this, SystemMessageDetailActivity.class);
            intent.putExtra(Constants.INTENT_SYSTEM_MESSAGE_ID, mlist.get(postion).uuid);
            SystemMessageListActivity.this.startActivity(intent);

            mlist.get(postion).isRead = 1;
            mAdapter.notifyDataSetChanged();
        });
        mSystemMessageListList.setPullLoadEnable(false);
        mSystemMessageListList.setPullRefreshEnable(false);

//        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setOnLoadMoreListener(this);
        mRefreshLayout.setOnRefreshListener(this);
    }

    private void initTitleBar() {

        mTitleBarView.setTitle("系统消息");
        mTitleBarView.setOnBackListener(v -> onBackPressed());
    }

    public void deleteNotice(final NoticeItem item) {

        if (!LoginStateManager.isLogin()) {
            StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_TEMP_SYSTEM_NOTICE_DELETE_URL);
            postRequest.setMethod(HttpMethods.Post);
            postRequest.setHttpBody(new DeleteSystemNoticeApiParameter(item.uuid, GetNoticeListApiParameter.TYPE_SYSTEM).getRequestBody());
            postRequest.setHttpListener(new MyHttpListener<String>(this) {
                @Override
                public void onSuccess(String s, Response<String> response) {
                    super.onSuccess(s, response);
                    ResponseData response1 = new ResponseData(s);
                    if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                        mlist.remove(item);
                        CommonToast.getInstance("删除成功").show();
                        mAdapter = new SystemNoticesAdapter(SystemMessageListActivity.this, mlist);
                        mSystemMessageListList.setAdapter(mAdapter);
                        updateView();

                    } else {
                        CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(HttpException e, Response<String> response) {
                    super.onFailure(e, response);
                    updateView();
                }
            });
            LiteHttpManager.getInstance().executeAsync(postRequest);
        } else {
            StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_SYSTEM_NOTICE_DELETE_URL);
            postRequest.setMethod(HttpMethods.Post);
            postRequest.setHttpBody(new DeleteSystemNoticeApiParameter(item.uuid, GetNoticeListApiParameter.TYPE_SYSTEM).getRequestBody());
            String sessionKey = SharedPreferencesManager.getString(
                    Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
            if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
            postRequest.setHttpListener(new MyHttpListener<String>(this) {
                @Override
                public void onSuccess(String s, Response<String> response) {
                    super.onSuccess(s, response);
                    ResponseData response1 = new ResponseData(s);
                    if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                        mlist.remove(item);
                        CommonToast.getInstance("删除成功").show();
                        mAdapter = new SystemNoticesAdapter(SystemMessageListActivity.this, mlist);
                        mSystemMessageListList.setAdapter(mAdapter);
                        updateView();
                    } else {
                        CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(HttpException e, Response<String> response) {
                    super.onFailure(e, response);
                    updateView();
                }
            });
            LiteHttpManager.getInstance().executeAsync(postRequest);
        }

    }


    @Override
    public void onLoadMore(RefreshLayout refreshlayout) {
        requestList();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {

        mPage = 0;
        mlist.clear();
        requestList();

    }

    private void finishRefreshAndLoadMore() {
        mRefreshLayout.finishLoadMore();
        mRefreshLayout.finishRefresh();
    }
}
