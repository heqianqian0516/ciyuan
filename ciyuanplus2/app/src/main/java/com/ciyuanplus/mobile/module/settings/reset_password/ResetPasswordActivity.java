package com.ciyuanplus.mobile.module.settings.reset_password;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.NoEmojiEditText;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/5/9.
 */

public class ResetPasswordActivity extends MyBaseActivity implements ResetPasswordContract.View {
    @BindView(R.id.m_reset_password_top_text)
    TextView mResetPasswordTopText;
    @BindView(R.id.m_reset_password_close_btn)
    ImageView mResetPasswordCloseBtn;
    @BindView(R.id.m_reset_password_old_view)
    NoEmojiEditText mResetPasswordOldView;
    @BindView(R.id.m_reset_password_new_view)
    NoEmojiEditText mResetPasswordNewView;
    @BindView(R.id.m_reset_password_confirm_text)
    TextView mResetPasswordConfirmText;
    @BindView(R.id.m_reset_password_first_lp)
    LinearLayout mResetPasswordFirstLp;

    @Inject

    ResetPasswordPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_reset_password);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
        DaggerResetPasswordPresenterComponent.builder()
                .resetPasswordPresenterModule(new ResetPasswordPresenterModule(this)).build().inject(this);
    }

    @OnClick({R.id.m_reset_password_close_btn, R.id.m_reset_password_confirm_text})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {
            case R.id.m_reset_password_close_btn:
                finish();
                break;
            case R.id.m_reset_password_confirm_text:
                String mOldPassword = mResetPasswordOldView.getText().toString();
                String mNewPassword = mResetPasswordNewView.getText().toString();
                if (Utils.isStringEmpty(mOldPassword)) {
                    CommonToast.getInstance(getResources().getString(R.string.string_my_profile_change_password_old_empty_alert)).show();
                    return;
                }
                if (Utils.isStringEmpty(mNewPassword)) {
                    CommonToast.getInstance(getResources().getString(R.string.string_my_profile_change_password_new_empty_alert)).show();
                    return;
                }
                if (!Utils.isValidPassword(mNewPassword)) {
                    CommonToast.getInstance(getResources().getString(R.string.string_register_password_unformatted_hint)).show();
                    return;
                }
                mPresenter.changePassword(mOldPassword, mNewPassword);
                break;
        }
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }
}
