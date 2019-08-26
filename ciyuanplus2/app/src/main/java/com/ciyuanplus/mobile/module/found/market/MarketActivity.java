package com.ciyuanplus.mobile.module.found.market;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciyuanplus.base.irecyclerview.IRecyclerView;
import com.ciyuanplus.base.irecyclerview.LoadMoreFooterView;
import com.ciyuanplus.base.irecyclerview.OnLoadMoreListener;
import com.ciyuanplus.base.irecyclerview.OnRefreshListener;
import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.adapter.PopupMarkteSortAdapter;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.manager.CommunityManager;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.found.market_search.MarketSearchActivity;
import com.ciyuanplus.mobile.module.login.LoginActivity;
import com.ciyuanplus.mobile.module.settings.address.AddressManagerActivity;
import com.ciyuanplus.mobile.module.start_forum.start_stuff.StartStuffActivity;
import com.ciyuanplus.mobile.net.bean.MarketSortItem;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CustomDialog;

import java.util.ArrayList;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * B
 * Created by Alen on 2017/8/19.
 * 闲市 页面
 */

public class MarketActivity extends MyBaseActivity implements MarketContract.View,
        EventCenterManager.OnHandleEventListener, OnLoadMoreListener, OnRefreshListener {

    @BindView(R.id.m_market_back_image)
    ImageView mMarketBackImage;
    @BindView(R.id.m_market_add_image)
    ImageView mMarketAddImage;
    @BindView(R.id.m_market_search_edit)
    LinearLayout mMarketSearchEdit;
    @BindView(R.id.m_market_top_lp)
    RelativeLayout mMarketTopLp;
    @BindView(R.id.m_market_sort_1)
    TextView mMarketSort1;
    @BindView(R.id.m_market_sort_2)
    TextView mMarketSort2;
    @BindView(R.id.m_market_sort_3)
    TextView mMarketSort3;
    @BindView(R.id.m_market_sort_lp)
    RelativeLayout mMarketSortLp;
    @BindView(R.id.m_market_null_lp)
    LinearLayout mMarketNullLp;
    @BindView(R.id.m_market_grid_view)
    IRecyclerView mMarketGridView;
    @BindView(R.id.m_market_shadow_lp)
    View mMarketShadowLp;
    @Inject

    MarketPresenter mPresenter;
    private PopupWindow popupWindow;
    private ListView mSortListView;
    private LoadMoreFooterView loadMoreFooterView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        this.initView();
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        StatisticsManager.onEventInfo("discover", "discover_index_page_load");
        mPresenter.initData();
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
        DaggerMarketPresenterComponent.builder()
                .marketPresenterModule(new MarketPresenterModule(this)).build().inject(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getDefaultContext(), 2);
        mMarketGridView.setLayoutManager(gridLayoutManager);
        mMarketGridView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = Utils.dip2px(10);
                outRect.right = Utils.dip2px(10);
            }
        });
        loadMoreFooterView = (LoadMoreFooterView) mMarketGridView.getLoadMoreFooterView();
        mMarketGridView.setOnLoadMoreListener(this);
        mMarketGridView.setOnRefreshListener(this);

        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_STUFF_LIST, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_STUFF_ITEM, this);
        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this);

    }

    @Override
    public void stopLoadMoreAndRefresh() {
        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        mMarketGridView.setRefreshing(false);
    }

    @Override
    public void updateView(int size) {
        if (size > 0) {
            mMarketGridView.setVisibility(View.VISIBLE);
            mMarketNullLp.setVisibility(View.GONE);
        } else {
            mMarketGridView.setVisibility(View.GONE);
            mMarketNullLp.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefresh() {
        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        mPresenter.requestStuffList(true);
    }

    @Override
    public void onLoadMore() {
        if (loadMoreFooterView.canLoadMore()) {
            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
            mPresenter.requestStuffList(false);
        }
    }

    @Override
    public IRecyclerView getRecyclerView() {
        return mMarketGridView;
    }

    //选择筛选列表弹出框
    private void showOrderPopupDialog(int option) {
        mPresenter.mOption = option;
        if (popupWindow == null) {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view1 = inflater != null ? inflater.inflate(R.layout.layout_market_sort_popup, null) : null;
            popupWindow = new PopupWindow(view1, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mSortListView = view1.findViewById(R.id.m_market_sort_popup_list);
            mSortListView.setOnItemClickListener((adapterView, view, i, l) -> {
                if (l == -1) {
                    return;
                }
                int postion = (int) l;
                MarketSortItem item = mPresenter.mSortAdapter.getItem(postion);
                if (mPresenter.mOption == 1) {
                    mMarketSort1.setText(item.sortName);
                    mMarketSort2.setText("价格");
                    mPresenter.mOrder = item.sortId;
                } else if (mPresenter.mOption == 2) {
                    mMarketSort2.setText(item.sortName);
                    mMarketSort1.setText("排序");
                    mPresenter.mOrder = item.sortId;
                } else if (mPresenter.mOption == 3) {
                    mMarketSort3.setText(item.sortName);
                    mPresenter.mStatus = item.sortId;
                }
                popupWindow.dismiss();
                mMarketGridView.smoothScrollToPosition(0);
                onRefresh();
            });
            popupWindow.setOnDismissListener(() -> mMarketShadowLp.setVisibility(View.GONE));
        }
        ArrayList<MarketSortItem> list = new ArrayList<>();
        switch (option) {
            case 1:
                list.add(new MarketSortItem("1", "最新发布", Utils.isStringEquals(mPresenter.mOrder, "1")));
                list.add(new MarketSortItem("2", "离我最近", Utils.isStringEquals(mPresenter.mOrder, "2")));
                list.add(new MarketSortItem("3", "最受关注", Utils.isStringEquals(mPresenter.mOrder, "3")));
                mPresenter.mSortAdapter = new PopupMarkteSortAdapter(this, list);
                mSortListView.setAdapter(mPresenter.mSortAdapter);
                popupWindow.showAsDropDown(mMarketSort1, 0, 0);
                break;
            case 2:
                list.add(new MarketSortItem("4", "由低到高", Utils.isStringEquals(mPresenter.mOrder, "4")));
                list.add(new MarketSortItem("5", "由高到低", Utils.isStringEquals(mPresenter.mOrder, "5")));
                mPresenter.mSortAdapter = new PopupMarkteSortAdapter(this, list);
                mSortListView.setAdapter(mPresenter.mSortAdapter);
                popupWindow.showAsDropDown(mMarketSort2, 0, 0);
                break;
            case 3:
                list.add(new MarketSortItem("9", "全部状态", Utils.isStringEquals(mPresenter.mStatus, "9")));
                list.add(new MarketSortItem("6", "在售中", Utils.isStringEquals(mPresenter.mStatus, "6")));
                list.add(new MarketSortItem("7", "已预订", Utils.isStringEquals(mPresenter.mStatus, "7")));
                list.add(new MarketSortItem("8", "已售出", Utils.isStringEquals(mPresenter.mStatus, "8")));
                mPresenter.mSortAdapter = new PopupMarkteSortAdapter(this, list);
                mSortListView.setAdapter(mPresenter.mSortAdapter);
                popupWindow.showAsDropDown(mMarketSort3, 0, 0);
                break;
        }
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        mMarketShadowLp.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_STUFF_LIST, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_STUFF_ITEM, this);
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_NEWS_DELETE, this);
    }

    @Override
    public void onHandleEvent(EventCenterManager.EventMessage eventMessage) {
        mPresenter.handleEvent(eventMessage);
    }

    @OnClick({R.id.m_market_back_image, R.id.m_market_add_image, R.id.m_market_top_lp, R.id.m_market_search_edit,
            R.id.m_market_sort_1, R.id.m_market_sort_2, R.id.m_market_sort_3})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {
            case R.id.m_market_back_image:
                onBackPressed();
                break;
            case R.id.m_market_add_image:
                if (!LoginStateManager.isLogin()) {
                    Intent intent = new Intent(MarketActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                if (!CommunityManager.getInstance().checkHasDetailAddress(UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid)) {// meiyou
                    CustomDialog.Builder builder = new CustomDialog.Builder(MarketActivity.this);
                    builder.setMessage("主人，发布宝贝前请提交您的有效地址哟~");
                    builder.setPositiveButton("去添加", (dialog, which) -> {
                        dialog.dismiss();
                        Intent intent = new Intent(MarketActivity.this, AddressManagerActivity.class);
                        startActivity(intent);
                    });
                    builder.setNegativeButton("取消", (dialog, i) -> dialog.dismiss());
                    CustomDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                } else {
                    Intent intent = new Intent(MarketActivity.this, StartStuffActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.m_market_top_lp:
                break;
            case R.id.m_market_sort_1:
                showOrderPopupDialog(1);
                break;
            case R.id.m_market_sort_2:
                showOrderPopupDialog(2);
                break;
            case R.id.m_market_sort_3:
                showOrderPopupDialog(3);
                break;
            case R.id.m_market_search_edit:
                Intent intent = new Intent(MarketActivity.this, MarketSearchActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }
}
