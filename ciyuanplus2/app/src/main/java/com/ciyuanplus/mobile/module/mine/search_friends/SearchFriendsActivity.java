package com.ciyuanplus.mobile.module.mine.search_friends;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciyuanplus.base.irecyclerview.IRecyclerView;
import com.ciyuanplus.base.irecyclerview.LoadMoreFooterView;
import com.ciyuanplus.base.irecyclerview.OnLoadMoreListener;
import com.ciyuanplus.base.irecyclerview.OnRefreshListener;
import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.module.mine.friends.MyFriendsPresenter;
import com.ciyuanplus.mobile.statistics.StatisticsConstant;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.ClearEditText;
import com.ciyuanplus.mobile.widget.CommonToast;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/7/6.
 */

public class SearchFriendsActivity extends MyBaseActivity implements SearchFriendsContract.View,
        EventCenterManager.OnHandleEventListener, OnLoadMoreListener, OnRefreshListener {
    @Inject
    public SearchFriendsPresenter mPresenter;
    @BindView(R.id.m_search_friends_cancel)
    TextView mSearchFriendsCancel;
    @BindView(R.id.m_search_friends_search_edit)
    ClearEditText mSearchFriendsSearchEdit;
    @BindView(R.id.m_search_friends_top_lp)
    RelativeLayout mSearchFriendsTopLp;
    @BindView(R.id.m_search_friends_list)
    IRecyclerView mSearchFriendsList;
    @BindView(R.id.m_imageView)
    ImageView mImageView;
    @BindView(R.id.m_search_friends_null_lp)
    LinearLayout mSearchFriendsNullLp;
    private LoadMoreFooterView loadMoreFooterView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_search_friends);
        this.initView();
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        mPresenter.initData(getIntent());
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
        DaggerSearchFriendsPresenterComponent.builder()
                .searchFriendsPresenterModule(new SearchFriendsPresenterModule(this)).build().inject(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getDefaultContext());//
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mSearchFriendsList.setLayoutManager(linearLayoutManager);
        loadMoreFooterView = (LoadMoreFooterView) mSearchFriendsList.getLoadMoreFooterView();
        mSearchFriendsList.setOnLoadMoreListener(this);
        mSearchFriendsList.setOnRefreshListener(this);


        mSearchFriendsSearchEdit.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                if (Utils.isStringEquals(mPresenter.mPageType, MyFriendsPresenter.FOLLOW_TYPE)) {
                    StatisticsManager.onEventInfo(StatisticsConstant.MODULE_MINE_FOLLOW_SEARCH, StatisticsConstant.OP_MINE_FOLLOW_SEARCH_FINISH_CLICK);
                } else {
                    StatisticsManager.onEventInfo(StatisticsConstant.MODULE_MINE_FANS_SEARCH, StatisticsConstant.OP_MINE_FANS_SEARCH_FINISH_CLICK);
                }
                HideKeyboard(mSearchFriendsSearchEdit);
                String name = mSearchFriendsSearchEdit.getText().toString();
                mPresenter.doSearch(name);
                return true;
            }
            return false;
        });

        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, this);

    }

    @Override
    public IRecyclerView getListView() {
        return mSearchFriendsList;
    }

    @Override
    public void updateView(int size) {
        if (size > 0) {
            mSearchFriendsNullLp.setVisibility(View.GONE);
            this.mSearchFriendsList.setVisibility(View.VISIBLE);
        } else {
            mSearchFriendsNullLp.setVisibility(View.VISIBLE);
            this.mSearchFriendsList.setVisibility(View.GONE);
            CommonToast.getInstance("未搜索到相关用户").show();
        }
    }

    public void onRefresh() {
        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);

        mPresenter.requestList(true);
    }

    @Override
    public void onLoadMore() {
        if (loadMoreFooterView.canLoadMore()) {
            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
            mPresenter.requestList(false);
        }
    }

    //隐藏虚拟键盘
    private void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventCenterManager.deleteEventListener(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, this);
    }

    @Override
    public void onHandleEvent(EventCenterManager.EventMessage eventMessage) {
        mPresenter.handleEvent(eventMessage);
    }

    @OnClick(R.id.m_search_friends_cancel)
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        int id = view.getId();
        if (id == R.id.m_search_friends_cancel) {
            if (Utils.isStringEquals(mPresenter.mPageType, MyFriendsPresenter.FOLLOW_TYPE)) {
                StatisticsManager.onEventInfo(StatisticsConstant.MODULE_MINE_FOLLOW_SEARCH, StatisticsConstant.OP_MINE_FOLLOW_SEARCH_BACK_CLICK);
            } else {
                StatisticsManager.onEventInfo(StatisticsConstant.MODULE_MINE_FANS_SEARCH, StatisticsConstant.OP_MINE_FANS_SEARCH_BACK_CLICK);
            }
            onBackPressed();
        }
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }
}
