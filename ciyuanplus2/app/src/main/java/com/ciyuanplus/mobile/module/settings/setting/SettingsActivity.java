package com.ciyuanplus.mobile.module.settings.setting;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.widget.CustomDialog;
import com.ciyuanplus.mobile.widget.LoadingDialog;
import com.ciyuanplus.mobile.widget.TitleBarView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/17.
 * 设置页面
 */

public class SettingsActivity extends MyBaseActivity implements SettingsContract.View {

    @Inject
    SettingsPresenter mPresenter;
    @BindView(R.id.m_my_setting_list)
    ListView mMySettingList;
    @BindView(R.id.m_my_setting_logout_button)
    TextView mMySettingLogoutButton;
    @BindView(R.id.m_my_setting_common_title)
    TitleBarView m_js_common_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_my_setting);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();
        initData();
    }

    private void initView() {
        ButterKnife.bind(this);
        DaggerSettingsPresenterComponent.builder().settingsPresenterModule(new SettingsPresenterModule(this))
                .build().inject(this);
        m_js_common_title.setOnBackListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {
                onBackPressed();
            }
        });
        m_js_common_title.setTitle("设置");

        LoadingDialog.Builder builder = new LoadingDialog.Builder(this);
        builder.setMessage("加载中....");
        Dialog loadingDialog = builder.create();
        loadingDialog.setCanceledOnTouchOutside(false);

        this.mMySettingLogoutButton.setOnClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View view) {
                if (LoginStateManager.isLogin()) {
                    CustomDialog.Builder builder = new CustomDialog.Builder(SettingsActivity.this);
                    builder.setMessage("是否退出账号？");
                    builder.setPositiveButton("退出", (dialog, which) -> {

                        dialog.dismiss();
                        LoginStateManager.logout();

                    });
                    builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
                    CustomDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }
            }
        });
    }

    //更新界面信息
    private void initData() {
        mPresenter.initData(mMySettingList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public Context getDefaultContext() {
        return this;
    }
}
