package com.ciyuanplus.mobile.module.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.activity.news.ShareNewsPopupActivity;
import com.ciyuanplus.mobile.adapter.RankedFirstAdapter;
import com.ciyuanplus.mobile.adapter.RankingListAdapter;
import com.ciyuanplus.mobile.inter.LoadMoreStatusInterface;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.bean.FreshNewItem;
import com.ciyuanplus.mobile.net.bean.HomeADBean;
import com.ciyuanplus.mobile.net.bean.RankListItem;
import com.ciyuanplus.mobile.net.bean.RankedFirstItem;
import com.ciyuanplus.mobile.net.parameter.RequestRankedFirstApiParameter;
import com.ciyuanplus.mobile.net.parameter.RequestRankngListApiParameter;
import com.ciyuanplus.mobile.net.response.RequestRankedFirstResponse;
import com.ciyuanplus.mobile.net.response.RequestRankingListResponse;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import crossoverone.statuslib.StatusUtil;

/*排行榜页面
 * */
public class RankingListActivity extends MyBaseActivity implements EventCenterManager.OnHandleEventListener {


    @BindView(R.id.ranking_list_recy)
    RecyclerView rankingListRecy;
    @BindView(R.id.rank_max_first)
    RecyclerView rankMaxFirst;
    @BindView(R.id.iv_activity)
    ImageView ivActivity;
    @BindView(R.id.iv_activitydetial)
    ImageView ivActivityDetial;
    @BindView(R.id.m_step_ranking_test)
    SmartRefreshLayout mStepRankingTest;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.share)
    ImageView share;
    @BindView(R.id.relative_layout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.image_bg)
    ImageView imageBg;

    private LinearLayoutManager linearLayoutManager;
    private int mStuffNextPage;
    private LoadMoreStatusInterface mLoadMoreStatusInterface;
    private final ArrayList<RankListItem> rankListItems = new ArrayList<>();
    private final ArrayList<RankedFirstItem> rankedFirstItems = new ArrayList<>();

    private boolean isLoadMoreEnable;
    private RankingListAdapter rankingListAdapter;
    // private String activityUuid = "189aa81ff4064cbb87ddbfef923a1c4b";
    private RankedFirstAdapter rankedFirstAdapter;
    private LinearLayoutManager linearLayoutManager1;
    private ArrayList<HomeADBean.DataBean> dataBeans = new ArrayList<>();
    private String mUuid;

    // private String activityUuid;;

    public boolean isLoadMoreEnable() {
        return isLoadMoreEnable;
    }

    public void setLoadMoreEnable(boolean loadMoreEnable) {
        isLoadMoreEnable = loadMoreEnable;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_list);
        ButterKnife.bind(this);
        StatusUtil.setUseStatusBarColor(this, Color.WHITE, Color.parseColor("#ffffff"));

        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色。
        StatusUtil.setSystemStatus(this, false, true);

        mStepRankingTest.setEnableRefresh(true);
        mStepRankingTest.setEnableLoadMore(true);
        mStepRankingTest.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mStuffNextPage = 1;
                requestRankingList();
            }
        });
        mStepRankingTest.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mStuffNextPage++;
                requestRankingList();
            }
        });
        Intent intent = getIntent();
        mUuid = intent.getStringExtra("mUuid");

        //activityUuid= getIntent().getStringExtra("uuid");
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rankingListRecy.setLayoutManager(linearLayoutManager);
        rankingListRecy.setNestedScrollingEnabled(true);//解决ScrollView嵌套RecyclerView导致滑动不流畅的问题
        rankingListRecy.getRecycledViewPool().setMaxRecycledViews(0, 10);
        rankingListAdapter = new RankingListAdapter(this, rankListItems);
        rankingListRecy.setAdapter(rankingListAdapter);
        linearLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        rankMaxFirst.setLayoutManager(linearLayoutManager1);
        rankMaxFirst.getRecycledViewPool().setMaxRecycledViews(0, 10);
        if (rankedFirstItems != null && rankedFirstItems != null) {
            rankedFirstAdapter = new RankedFirstAdapter(this, rankedFirstItems, rankListItems);
            rankMaxFirst.setAdapter(rankedFirstAdapter);
        } else {

        }
        requestRankedFirst();
        ivActivityDetial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUuid.length() > 0) {
                    Intent intent = new Intent(RankingListActivity.this, ParticipationDetailsActivity.class);
                    intent.putExtra("activityUuid", mUuid);
                    startActivity(intent);
                } else {
                    CommonToast.getInstance("暂无活动").show();

                }
            }
        });

    }

    //排名第一
    private void requestRankedFirst() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_GET_POST_LIKE_MAX);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new RequestRankedFirstApiParameter(mUuid).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(this) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);


                RequestRankedFirstResponse response1 = new RequestRankedFirstResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    //   CommonToast.getInstance(response1.mMsg).show();
                } else if (response1.rankedFirstBean != null) {
                    // Collections.addAll(rankedFirstItems, response1.rankedFirstBean.data);
                    // rankedFirstAdapter.notifyDataSetChanged();
                    ArrayList<RankedFirstItem> rankedFirstItems = new ArrayList<>();
                    rankedFirstItems.add(response1.rankedFirstBean.data);
                    rankedFirstAdapter.setFirsList(rankedFirstItems);
                    if (rankedFirstItems.size() != 0) {
                        imageBg.setVisibility(View.GONE);
                    } else {

                        imageBg.setVisibility(View.VISIBLE);
                    }
                    /*rankMaxFirst.loadMoreComplete();
                    rankMaxFirst.refreshComplete();*/
                    requestRankingList();
                }
            }
//
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                super.onFailure(e, response);
//                CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_my_profile_get_help_fail_alert)).show();
//            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }


    @Override
    public void onHandleEvent(EventCenterManager.EventMessage eventMessage) {
        if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE) {
            String activityUuid = (String) eventMessage.mObject;
            for (int i = 0; i < rankListItems.size(); i++) {
                if (Utils.isStringEquals(activityUuid, rankListItems.get(i).activityUuid)) {
                    rankListItems.remove(i);
                    rankingListAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_STUFF_ITEM) {
            RankListItem item = (RankListItem) eventMessage.mObject;
            for (int i = 0; i < rankListItems.size(); i++) {
                if (Utils.isStringEquals(item.activityUuid, rankListItems.get(i).activityUuid)) {
                    rankListItems.set(i, item);
                    rankingListAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (eventMessage.mEvent == Constants.EVENT_MESSAGE_UPDATE_BROWSE_COUNT) {
            String activityUuid = (String) eventMessage.mObject;
            for (int i = 0; i < rankListItems.size(); i++) {
                if (Utils.isStringEquals(activityUuid, rankListItems.get(i).activityUuid)) {
                    rankListItems.get(i).browseCount++;
                    rankingListAdapter.notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void requestRankingList() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD + ApiContant.REQUEST_GET_POST_LIKE_COUNT);
        postRequest.setMethod(HttpMethods.Post);
        int PAGE_SIZE = 20;
      /* postRequest.setHttpBody(new RequestRankngListApiParameter(UserInfoData.getInstance().getUserInfoItem().uuid, mStuffNextPage + ""
                , PAGE_SIZE + "").getRequestBody());*/
        postRequest.setHttpBody(new RequestRankngListApiParameter(mUuid, mStuffNextPage + ""
                , PAGE_SIZE + "").getRequestBody());

        postRequest.setHttpListener(new MyHttpListener<String>(this) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                RequestRankingListResponse response1 = new RequestRankingListResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    //  CommonToast.getInstance(response1.mMsg).show();
                    if (mStuffNextPage == 0) rankListItems.clear();
                    if (response1.rankingListBean.list != null && response1.rankingListBean.list.length > 0) {
                        //  Collections.addAll(rankListItems, response1.rankingListBean.list);

                        mStuffNextPage++;
                        // rankingListAdapter.notifyDataSetChanged();
                        rankedFirstAdapter.setLists(response1.rankingListBean.list);
                        if (rankedFirstItems.size() == 0) {
                            imageBg.setVisibility(View.GONE);

                        } else {
                            imageBg.setVisibility(View.VISIBLE);
                        }
                        setLoadMoreEnable(response1.rankingListBean.list.length >= PAGE_SIZE);
                        /*rankMaxFirst.loadMoreComplete();
                        rankMaxFirst.refreshComplete();*/
                    }

                } else {
                    //setLoadMoreEnable(isLoadMoreEnable);
                    CommonToast.getInstance("这已经是最新数据了", Toast.LENGTH_SHORT).show();
                }

                if (mLoadMoreStatusInterface != null) {

                    mLoadMoreStatusInterface.onFinishLoadMore(isLoadMoreEnable);
                }
                //关闭下拉刷新
                mStepRankingTest.finishRefresh();
                //关闭上拉加载
                mStepRankingTest.finishLoadMore();
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);

                setLoadMoreEnable(true);
                if (mLoadMoreStatusInterface != null) {

                    mLoadMoreStatusInterface.onFinishLoadMore(isLoadMoreEnable);
                }

            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    public void setLoadMoreStatusInterface(LoadMoreStatusInterface loadMoreStatusInterface) {
        mLoadMoreStatusInterface = loadMoreStatusInterface;
    }

    @OnClick({R.id.back, R.id.share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.share:
                FreshNewItem freshNewItem = new FreshNewItem();
                freshNewItem.bizType = 2;
                freshNewItem.title = "次元PLUS";
                freshNewItem.imgs = "a.img,b.img";
                Intent intent = new Intent(RankingListActivity.this, ShareNewsPopupActivity.class);
                intent.putExtra(Constants.INTENT_NEWS_ITEM, freshNewItem);
                startActivity(intent);


                break;
        }
    }

}
