package com.ciyuanplus.mobile.module.settings.help;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.widget.CommonTitleBar;

import javax.inject.Inject;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/5/18.
 */

public class HelpActivity extends MyBaseActivity implements HelpContract.View {
    @BindView(R.id.m_help_question_list)
    RecyclerView mHelpQuestionList;
    @BindView(R.id.m_help_common_title)
    CommonTitleBar m_js_common_title;

    @Inject

    HelpPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_help);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();
        mPresenter.initData();
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
        DaggerHelpPresenterComponent.builder().helpPresenterModule(new HelpPresenterModule(this)).build().inject(this);
        m_js_common_title.setLeftClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {
                onBackPressed();
            }
        });
        m_js_common_title.setCenterText("帮助中心");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getDefaultContext());//   LinearLayoutManager不能共用 真是坑爹
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mHelpQuestionList.setLayoutManager(linearLayoutManager);
    }

    @Override
    public RecyclerView getListView() {
        return mHelpQuestionList;
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }
}
