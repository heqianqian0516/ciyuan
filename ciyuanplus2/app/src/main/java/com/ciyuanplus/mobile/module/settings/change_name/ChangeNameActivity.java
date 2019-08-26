package com.ciyuanplus.mobile.module.settings.change_name;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.module.register.register.RegisterActivity;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.ClearEditText;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.LengthFilter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/5/18.
 */

public class ChangeNameActivity extends MyBaseActivity implements ChangeNameContract.View {
    @Inject
     ChangeNamePresenter mPresenter;
    @BindView(R.id.topText)
    TextView mChangeNameTopText;
    @BindView(R.id.closeButton)
    ImageView mChangeNameCloseBtn;
    @BindView(R.id.signText)
    ClearEditText mChangeNameAccountView;
    @BindView(R.id.submitButton)
    TextView mChangeNameConfirmText;
    @BindView(R.id.m_change_name_first_lp)
    LinearLayout mChangeNameFirstLp;
    private String mFormType;
    private String oldName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_change_name);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        oldName = getIntent().getStringExtra(Constants.INTENT_USER_NAME);
        mFormType = getIntent().getStringExtra(Constants.INTENT_ACTIVITY_TYPE);
        this.initView();
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
        DaggerChangeNamePresenterComponent.builder().
                changeNamePresenterModule(new ChangeNamePresenterModule(this)).build().inject(this);
        mChangeNameAccountView.setFilters(new InputFilter[]{new LengthFilter(12)});
        if (Utils.isStringEmpty(oldName))
            oldName = UserInfoData.getInstance().getUserInfoItem().nickname;
        mChangeNameAccountView.setText(oldName);
        mChangeNameAccountView.setSelection(oldName.length());
    }


    @OnClick({R.id.closeButton, R.id.submitButton})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {
            case R.id.closeButton:
                finish();

                break;
            case R.id.submitButton:
                String name = mChangeNameAccountView.getText().toString();
                if (Utils.isStringEmpty(name)) {
                    CommonToast.getInstance(getResources().getString(R.string.string_my_profile_change_name_empty_alert)).show();
                    return;
                }
                if (Utils.isStringEquals(name, oldName)) {
                    CommonToast.getInstance("请先修改昵称").show();
                    return;
                }
                if (Utils.isStringEquals(RegisterActivity.class.getSimpleName(), mFormType)) {// 如果是注册页面打开的  需要传回去名字，不能直接修改。
                    mPresenter.checkUserNickName(name);
                    return;
                }
                mPresenter.changeName(name);
                break;
        }
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }
}
