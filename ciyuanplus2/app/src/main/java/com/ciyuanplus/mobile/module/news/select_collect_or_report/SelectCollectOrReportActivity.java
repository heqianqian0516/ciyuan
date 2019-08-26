package com.ciyuanplus.mobile.module.news.select_collect_or_report;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2018/1/10.
 */

public class SelectCollectOrReportActivity extends MyBaseActivity implements SelectCollectOrReportContract.View {
    @BindView(R.id.m_select_collect_or_report_collect_image)
    ImageView mSelectCollectOrReportCollectImage;
    @BindView(R.id.m_select_collect_or_report_collect)
    LinearLayout mSelectCollectOrReportCollect;
    @BindView(R.id.m_select_collect_or_report_report)
    LinearLayout mSelectCollectOrReportReport;
    @BindView(R.id.m_select_collect_or_report_cancel)
    TextView mSelectCollectOrReportCancel;

    @Inject

    SelectCollectOrReportPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_select_or_report);
        this.initView();
        mPresenter.initData(getIntent());
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
        DaggerSelectCollectOrReportPresenterComponent.builder()
                .selectCollectOrReportPresenterModule(new SelectCollectOrReportPresenterModule(this)).build().inject(this);
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }

    @Override
    public void updateCollectView(boolean isCollected) {
        mSelectCollectOrReportCollectImage.setImageResource(isCollected ? R.mipmap.btn_collectnews : R.mipmap.btn_un_collectnews);
    }


    @OnClick({R.id.m_select_collect_or_report_collect, R.id.m_select_collect_or_report_report, R.id.m_select_collect_or_report_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.m_select_collect_or_report_collect:
                mPresenter.doCollect();
                break;
            case R.id.m_select_collect_or_report_report:
                mPresenter.goReportActivity();
                break;
            case R.id.m_select_collect_or_report_cancel:
                finish();
                break;
        }
    }
}
