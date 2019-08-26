package com.ciyuanplus.mobile.module.mine.friends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.manager.EventCenterManager;
import com.ciyuanplus.mobile.module.mine.search_friends.SearchFriendsActivity;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.widget.TitleBarView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by Alen on 2017/5/20.
 * <p>
 * 我的粉丝和我的关注页面
 */

public class MyFriendsActivity extends MyBaseActivity implements MyFriendsContract.View,
        EventCenterManager.OnHandleEventListener, OnRefreshListener, OnLoadMoreListener {
    @Inject
    public MyFriendsPresenter mPresenter;
    @BindView(R.id.m_my_friends_back_image)
    ImageView mMyFriendsBackImage;
    @BindView(R.id.m_my_friends_search_edit)
    LinearLayout mMyFriendsSearchEdit;
    @BindView(R.id.m_my_friends_list)
    RecyclerView mMyFriendsList;
    @BindView(R.id.m_my_friends_null_lp)
    LinearLayout mMyFriendsNullLp;
    @BindView(R.id.title_bar)
    TitleBarView mTitleBar;
    @BindView(R.id.refresh)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.tv_notice)
    TextView tvNotice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_my_friends);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();

        mPresenter.initData(getIntent());
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);

        DaggerMyFriendsPresenterComponent.builder()
                .myFriendsPresenterModule(new MyFriendsPresenterModule(this)).build().inject(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getDefaultContext());//
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mMyFriendsList.setLayoutManager(linearLayoutManager);

        EventCenterManager.addEventListener(Constants.EVENT_MESSAGE_UPDATE_PEOPLE_STATE, this);

        String mType = getIntent().getStringExtra(Constants.INTENT_MY_FRIENDS_TYPE);
        mTitleBar.setTitle(MyFriendsPresenter.FOLLOW_TYPE.equals(mType) ? "我的关注" : "我的粉丝");
        mTitleBar.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public RecyclerView getListView() {
        return mMyFriendsList;
    }

    @Override
    public void stopRefreshAndLoadMore() {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();

    }

    @Override
    public void updateView(int size) {
        if (size > 0) {
            mMyFriendsNullLp.setVisibility(View.GONE);
            this.mMyFriendsList.setVisibility(View.VISIBLE);
            String mType = getIntent().getStringExtra(Constants.INTENT_MY_FRIENDS_TYPE);
            tvNotice.setText(MyFriendsPresenter.FOLLOW_TYPE.equals(mType) ? "暂无关注" : "暂无新粉丝");
        } else {
            mMyFriendsNullLp.setVisibility(View.VISIBLE);
            this.mMyFriendsList.setVisibility(View.GONE);
            String mType = getIntent().getStringExtra(Constants.INTENT_MY_FRIENDS_TYPE);
            tvNotice.setText(MyFriendsPresenter.FOLLOW_TYPE.equals(mType) ? "暂无关注" : "暂无新粉丝");

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

    @OnClick({R.id.m_my_friends_back_image, R.id.m_my_friends_search_edit})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {
            case R.id.m_my_friends_back_image:
                onBackPressed();
                break;
            case R.id.m_my_friends_search_edit:
                Intent intent = new Intent(MyFriendsActivity.this, SearchFriendsActivity.class);
                intent.putExtra(Constants.INTENT_MY_FRIENDS_TYPE, mPresenter.mPageType);
                startActivity(intent);
                break;
        }
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.requestList(true);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mPresenter.requestList(false);
    }


}
