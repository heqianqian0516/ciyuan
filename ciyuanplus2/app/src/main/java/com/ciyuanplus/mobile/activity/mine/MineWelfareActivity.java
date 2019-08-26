package com.ciyuanplus.mobile.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.adapter.WelfareAdapter;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.module.webview.JsWebViewActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.WelfareItem;
import com.ciyuanplus.mobile.net.parameter.GetMineWelfareListApiParameter;
import com.ciyuanplus.mobile.net.response.GetWelfareListResponse;
import com.ciyuanplus.mobile.pulltorefresh.XListView;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonTitleBar;
import com.ciyuanplus.mobile.widget.CommonToast;
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
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/11/20.
 */

public class MineWelfareActivity extends MyBaseActivity implements XListView.IXListViewListener, OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.m_mine_welfare_list)
    XListView mMineWelfareList;
    @BindView(R.id.m_mine_welfare_null_lp)
    LinearLayout mMineWelfareNullLp;
    @BindView(R.id.m_mine_welfare_common_title)
    CommonTitleBar m_js_common_title;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private int mNextPage = 0;

    private final ArrayList<WelfareItem> mWelfareList = new ArrayList<>();
    private WelfareAdapter mWelfareAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_mine_welfare);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();
        requestMyWelfare();
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
        m_js_common_title.setLeftClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {
                onBackPressed();
            }
        });
        m_js_common_title.setCenterText("我的活动");

        mWelfareAdapter = new WelfareAdapter(this, mWelfareList);
        mMineWelfareList.setAdapter(mWelfareAdapter);
        mMineWelfareList.setXListViewListener(this);

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);

        mMineWelfareList.setOnItemClickListener((parent, view, position, id) -> {
            if (id == -1) {
                return;
            }
            int postion = (int) id;
            Intent intent = new Intent(MineWelfareActivity.this, JsWebViewActivity.class);
            intent.putExtra(Constants.INTENT_OPEN_URL, mWelfareList.get(postion).giftUrl);
            intent.putExtra(Constants.INTENT_JS_WEB_VIEW_PARAM, mWelfareList.get(postion).id + "");
            startActivity(intent);
        });
    }


    private void requestMyWelfare() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_MY_GIFT_LIST_URL);
        postRequest.setMethod(HttpMethods.Post);
        int PAGE_SIZE = 20;
        postRequest.setHttpBody(new GetMineWelfareListApiParameter(mNextPage + "", PAGE_SIZE + "").getRequestBody());
        String sessionKey = SharedPreferencesManager.getString(
                Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
        if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
        postRequest.setHttpListener(new MyHttpListener<String>(this) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
//                mMineWelfareList.stopLoadMore();
//                mMineWelfareList.stopRefresh();
                finishRefreshAndLoadMore();
                GetWelfareListResponse response1 = new GetWelfareListResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    if (mNextPage == 0) mWelfareList.clear();
                    mNextPage++;
                    Collections.addAll(mWelfareList, response1.welfareListInfo.list);
                    updateList();
                } else {
                    CommonToast.getInstance(response1.mMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
//                mMineWelfareList.stopLoadMore();
//                mMineWelfareList.stopRefresh();

                finishRefreshAndLoadMore();
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    private void updateList() {
        mWelfareAdapter.notifyDataSetChanged();
        if (mWelfareList.size() > 0) {
            mMineWelfareNullLp.setVisibility(View.GONE);
            mRefreshLayout.setVisibility(View.VISIBLE);

        } else {
            mRefreshLayout.setVisibility(View.GONE);
            mMineWelfareNullLp.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefresh() {
        mNextPage = 0;
        requestMyWelfare();
    }

    @Override
    public void onLoadMore() {
        requestMyWelfare();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mNextPage = 0;
        requestMyWelfare();
    }

    @Override
    public void onLoadMore(RefreshLayout refreshlayout) {
        requestMyWelfare();
    }

    private void finishRefreshAndLoadMore() {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
    }
}
