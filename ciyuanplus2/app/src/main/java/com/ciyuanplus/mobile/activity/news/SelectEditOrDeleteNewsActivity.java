package com.ciyuanplus.mobile.activity.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.utils.Constants;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/6/21.
 */

public class SelectEditOrDeleteNewsActivity extends MyBaseActivity {
    public static final String SELECTED = "selected";
    public static final String DELETE = "delete";
    public static final String EDIT = "edit";
    public static final String COLLECT = "collect";
    public static final String REPORT = "report";
    @BindView(R.id.m_select_edit_delete_news_edit)
    LinearLayout mSelectEditDeleteNewsEdit;
    @BindView(R.id.m_select_edit_delete_news_delete)
    LinearLayout mSelectEditDeleteNewsDelete;
    @BindView(R.id.m_select_edit_delete_news_mine_lp)
    LinearLayout mSelectEditDeleteNewsMineLp;
    @BindView(R.id.m_select_edit_delete_news_collect_image)
    ImageView mSelectEditDeleteNewsCollectImage;
    @BindView(R.id.m_select_edit_delete_news_collect)
    LinearLayout mSelectEditDeleteNewsCollect;
    @BindView(R.id.m_select_edit_delete_news_report)
    LinearLayout mSelectEditDeleteNewsReport;
    @BindView(R.id.m_select_edit_delete_news_others_lp)
    LinearLayout mSelectEditDeleteNewsOthersLp;
    @BindView(R.id.m_select_edit_delete_news_cancel)
    TextView mSelectEditDeleteNewsCancel;
    private int mType;
    private int mHasCollected;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_select_edit_delete_news);
        mType = getIntent().getIntExtra(Constants.INTENT_ACTIVITY_TYPE, 0);
        mHasCollected = getIntent().getIntExtra(Constants.INTENT_POST_HAS_COLLECTED, 0);
        this.initView();
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);

        mSelectEditDeleteNewsCollectImage.setImageResource(mHasCollected == 1 ? R.mipmap.btn_collectnews : R.mipmap.btn_un_collectnews);
        if (mType == 0) { // 根据不同的分类  区分显示不同的layout
            mSelectEditDeleteNewsMineLp.setVisibility(View.VISIBLE);
            mSelectEditDeleteNewsOthersLp.setVisibility(View.GONE);
        } else {
            mSelectEditDeleteNewsMineLp.setVisibility(View.GONE);
            mSelectEditDeleteNewsOthersLp.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.m_select_edit_delete_news_edit, R.id.m_select_edit_delete_news_delete, R.id.m_select_edit_delete_news_collect, R.id.m_select_edit_delete_news_report, R.id.m_select_edit_delete_news_cancel})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        Intent data = new Intent();
        switch (view.getId()) {
            case R.id.m_select_edit_delete_news_edit:
                data.putExtra(SELECTED, EDIT);
                SelectEditOrDeleteNewsActivity.this.setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.m_select_edit_delete_news_delete:
                data.putExtra(SELECTED, DELETE);
                SelectEditOrDeleteNewsActivity.this.setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.m_select_edit_delete_news_collect:
                data.putExtra(SELECTED, COLLECT);
                SelectEditOrDeleteNewsActivity.this.setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.m_select_edit_delete_news_report:
                data.putExtra(SELECTED, REPORT);
                SelectEditOrDeleteNewsActivity.this.setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.m_select_edit_delete_news_cancel:
                finish();
                break;
        }
    }
}
