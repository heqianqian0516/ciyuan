package com.ciyuanplus.mobile.activity.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.utils.Constants;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/8/14.
 */

public class NewsSearchPopUpActivity extends MyBaseActivity {
    public static final String SELECTED = "selected";
    @BindView(R.id.m_news_search_all_lp)
    TextView mNewsSearchAllLp;
    @BindView(R.id.m_news_search_zone_lp)
    TextView mNewsSearchZoneLp;
    @BindView(R.id.m_news_search_user_lp)
    TextView mNewsSearchUserLp;
    @BindView(R.id.m_news_search_root)
    LinearLayout mNewsSearchRoot;
    private int mSearchType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.actvity_news_search_pop_up);
        mSearchType = getIntent().getIntExtra(Constants.INTENT_SEARCH_NEWS_TYPE, 0);
        SharedPreferencesManager.putBoolean(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_HAS_OPEM_SEARCH_VIEW, true);

        this.initView();
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
        if (mSearchType == 0) mNewsSearchAllLp.setTextColor(0xff0091ea);
        if (mSearchType == 1) mNewsSearchZoneLp.setTextColor(0xff0091ea);
        if (mSearchType == 2) mNewsSearchUserLp.setTextColor(0xff0091ea);

    }

    @OnClick({R.id.m_news_search_all_lp, R.id.m_news_search_zone_lp, R.id.m_news_search_user_lp, R.id.m_news_search_root})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.m_news_search_all_lp:
                intent.putExtra(SELECTED, 0);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.m_news_search_zone_lp:
                intent.putExtra(SELECTED, 1);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.m_news_search_user_lp:
                intent.putExtra(SELECTED, 2);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.m_news_search_root:
                finish();
                break;
        }
    }
}
