package com.ciyuanplus.mobile.module.register.search_community;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.adapter.SearchCommunityAdapter;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.pulltorefresh.XListView;
import com.ciyuanplus.mobile.statistics.StatisticsConstant;
import com.ciyuanplus.mobile.statistics.StatisticsManager;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.widget.ClearEditText;
import com.ciyuanplus.mobile.widget.CommonToast;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/6/20.
 */

public class SearchCommunityActivity extends MyBaseActivity implements SearchCommunityContract.View, XListView.IXListViewListener {
    @Inject
 SearchCommunityPresenter mPresenter;
    @BindView(R.id.m_search_community_cancel)
    TextView mSearchCommunityCancel;
    @BindView(R.id.m_search_community_search_edit)
    ClearEditText mSearchCommunitySearchEdit;
    @BindView(R.id.m_search_community_top_lp)
    RelativeLayout mSearchCommunityTopLp;
    @BindView(R.id.m_search_community_list)
    XListView mSearchCommunityList;
    @BindView(R.id.m_imageView)
    ImageView mImageView;
    @BindView(R.id.m_search_community_null_lp)
    LinearLayout mSearchCommunityNullLp;

    //显示虚拟键盘
    private static void ShowKeyboard(View v) {
        v.requestFocus();
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_search_community);
//        keyWord = getIntent().getStringExtra(Constants.INTENT_SEARCH_CONTENT);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();
        ShowKeyboard(mSearchCommunitySearchEdit);
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);

        DaggerSearchCommunityPresenterComponent.builder()
                .searchCommunityPresenterModule(new SearchCommunityPresenterModule(this)).build().inject(this);
        mSearchCommunityList.setPullLoadEnable(true);
        mSearchCommunityList.setPullRefreshEnable(false);
        mSearchCommunityList.setXListViewListener(this);

        mSearchCommunitySearchEdit.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                StatisticsManager.onEventInfo(StatisticsConstant.MODULE_SEARCH_COMMUNITY,
                        StatisticsConstant.OP_SEARCH_COMMUNITY_FINISH_CLICK);
                HideKeyboard(mSearchCommunitySearchEdit);
                mPresenter.doSearchQuery(mSearchCommunitySearchEdit.getText().toString());
                return true;
            }
            return false;
        });

        mSearchCommunityList.setOnItemClickListener((adapterView, view, i, l) -> {
            StatisticsManager.onEventInfo(StatisticsConstant.MODULE_SEARCH_COMMUNITY,
                    StatisticsConstant.OP_SEARCH_COMMUNITY_SELECT_ITEM);
            if (l == -1) {
                return;
            }
            int postion = (int) l;
            mPresenter.returnResult(postion);
        });

        mPresenter.initData(getIntent().getStringExtra(Constants.INTENT_SEARCH_CITY));
    }


    //隐藏虚拟键盘
    private void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    // 如果没有找到小区  显示未找到页面
    @Override
    public void updateView(int listSize) {
        if (listSize > 0) {
            mSearchCommunityNullLp.setVisibility(View.GONE);
            mSearchCommunityList.setVisibility(View.VISIBLE);
        } else {
            CommonToast.getInstance("未搜索到小区").show();
            mSearchCommunityNullLp.setVisibility(View.VISIBLE);
            mSearchCommunityList.setVisibility(View.GONE);
        }
    }

    @Override
    public void listStopAnim() {
        mSearchCommunityList.stopLoadMore();
        mSearchCommunityList.stopRefresh();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        mPresenter.doSearchQuery();
    }

    @OnClick(R.id.m_search_community_cancel)
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        int id = view.getId();
        if (id == R.id.m_search_community_cancel) {
            onBackPressed();
        }
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }

    @Override
    public void setSearchText(String keyWord) {
        mSearchCommunitySearchEdit.setText(keyWord);
        mSearchCommunitySearchEdit.setSelection(keyWord.length());
    }

    @Override
    public void setCommunityListAdapter(SearchCommunityAdapter mAdapter) {
        mSearchCommunityList.setAdapter(mAdapter);
    }
}
