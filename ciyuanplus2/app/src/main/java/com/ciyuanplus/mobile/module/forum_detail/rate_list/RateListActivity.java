package com.ciyuanplus.mobile.module.forum_detail.rate_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ciyuanplus.base.irecyclerview.IRecyclerView;
import com.ciyuanplus.base.irecyclerview.LoadMoreFooterView;
import com.ciyuanplus.base.irecyclerview.OnLoadMoreListener;
import com.ciyuanplus.base.irecyclerview.OnRefreshListener;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.module.wiki.wiki_position.WikiPositionActivity;
import com.ciyuanplus.mobile.net.bean.WikiItem;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonTitleBar;
import com.ciyuanplus.mobile.widget.MarkView;
import com.ciyuanplus.mobile.widget.RoundImageView;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Alen on 2018/1/9.
 */

public class RateListActivity extends MyBaseActivity implements RateListContract.View,OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.m_rate_list_common_title)
    CommonTitleBar m_js_common_title;
    @BindView(R.id.m_rate_list_view)
    IRecyclerView mRateListView;

    private ImageView mRateListInfoImage;
    private TextView mRateListInfoScore;
    private TextView mRateListPlaceName;
    private TextView mRateListAlert;
    private MarkView mRateListMarkView;
    private TextView mRateListNumbers;
    private RoundImageView mRateListMineHead;
    private TextView mRateListMineName;
    private MarkView mRateListMineMarkView;
    private TextView mRateListMineContent;
    private TextView mRateListMineTime;
    private RelativeLayout mRateListMineLayout;


    private LoadMoreFooterView loadMoreFooterView;

    @Inject
 RateListPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_rate_list);
        this.initView();
        mPresenter.initData(getIntent());
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);

        DaggerRateListPresenterComponent.builder()
                .rateListPresenterModule(new RateListPresenterModule(this)).build().inject(this);

        m_js_common_title.setLeftClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {
                onBackPressed();
            }
        });
        m_js_common_title.setCenterText("点评");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRateListView.setLayoutManager(linearLayoutManager);
        loadMoreFooterView = (LoadMoreFooterView) mRateListView.getLoadMoreFooterView();
        mRateListView.setOnRefreshListener(this);
        mRateListView.setOnLoadMoreListener(this);

        // 绑定上面的View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View headView = inflater != null ? inflater.inflate(R.layout.activity_rate_list_top_layout, null) : null;
        mRateListView.addHeaderView(headView);

        mRateListInfoImage = headView.findViewById( R.id.m_rate_list_info_image);
        mRateListInfoScore = headView.findViewById( R.id.m_rate_list_info_score);
        mRateListPlaceName = headView.findViewById( R.id.m_rate_list_place_name);
        mRateListAlert = headView.findViewById( R.id.m_rate_list_alert);
        mRateListMarkView = headView.findViewById( R.id.m_rate_list_mark_view);
        mRateListNumbers = headView.findViewById( R.id.m_rate_list_numbers);
        mRateListMineHead = headView.findViewById( R.id.m_rate_list_mine_head);
        mRateListMineName = headView.findViewById( R.id.m_rate_list_mine_name);
        mRateListMineMarkView = headView.findViewById( R.id.m_rate_list_mine_mark_view);
        mRateListMineContent = headView.findViewById( R.id.m_rate_list_mine_content);
        mRateListMineTime = headView.findViewById( R.id.m_rate_list_mine_time);
        mRateListMineLayout = headView.findViewById( R.id.m_rate_list_mine_layout);
        mRateListPlaceName.setOnClickListener(myOnClickListener);
    }

    private final MyOnClickListener myOnClickListener = new MyOnClickListener() {
        @Override
        public void performRealClick(View view) {
            int id = view.getId();
            if(id == R.id.m_rate_list_place_name) {
                // 需要传WikiItem 到那个页面。 必传  四个参数， name、address、longitude、latitude
                WikiItem wikiItem = new WikiItem();
                wikiItem.name = mPresenter.mPlaceDetailItem.name;
                wikiItem.address = mPresenter.mPlaceDetailItem.address;
                wikiItem.longitude = mPresenter.mPlaceDetailItem.longitude;
                wikiItem.latitude = mPresenter.mPlaceDetailItem.latitude;
                Intent intent = new Intent(RateListActivity.this, WikiPositionActivity.class);
                intent.putExtra(Constants.INTENT_POSITION_ITEM, wikiItem);
                startActivity(intent);
            }
        }
    };
    @Override
    public void updateView(){
        Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + mPresenter.mPlaceDetailItem.indexImg)
                .into(mRateListInfoImage);
        mRateListInfoScore.setText(mPresenter.mPlaceDetailItem.score + "");
        mRateListPlaceName.setText(mPresenter.mPlaceDetailItem.name);
        mRateListMarkView.setValue(mPresenter.mPlaceDetailItem.score);
        mRateListNumbers.setText(mPresenter.mPlaceDetailItem.rateCount + "人评过");
    }


    @Override
    public void stopRereshAndLoad() {
        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        mRateListView.setRefreshing(false);
    }

    @Override
    public void updateMineCommentView() {
        if(mPresenter.mMineComment == null){
            mRateListMineLayout.setVisibility(View.GONE);
        }
        else {
            mRateListMineLayout.setVisibility(View.VISIBLE);
            Glide.with(App.mContext).load(Constants.IMAGE_LOAD_HEADER + mPresenter.mMineComment.photo)
                    .into(mRateListMineHead);
            mRateListMineName.setText(mPresenter.mMineComment.nickname);
            mRateListMineMarkView.setValue(mPresenter.mMineComment.score);
            mRateListMineContent.setText(mPresenter.mMineComment.contentText);
            mRateListMineTime.setText(Utils.getFormattedTimeString(mPresenter.mMineComment.createTime));
        }
    }

    @Override
    public void setTitleString(String title) {
        m_js_common_title.setCenterText(title);
    }

    @Override
    public void onRefresh() {
        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        mPresenter.getRateList(true);
    }

    @Override
   public void onLoadMore() {
        if (loadMoreFooterView.canLoadMore()) {
            loadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
            mPresenter.getRateList(false);
        }
    }

    @Override
    public IRecyclerView getGridView() {
        return mRateListView;
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }
}
