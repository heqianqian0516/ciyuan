package com.ciyuanplus.mobile.module.settings.bind_phone;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Alen on 2017/5/18.
 * // 修改手机号
 */

public class BindPhoneActivity extends MyBaseActivity implements BindPhoneContract.View {
    @BindView(R.id.m_bind_phone_top_text)
    TextView mBindPhoneTopText;
    @BindView(R.id.m_bind_phone_close_btn)
    ImageView mBindPhoneCloseBtn;
    @BindView(R.id.m_bind_phone_account_view)
    EditText mBindPhoneAccountView;
    @BindView(R.id.m_bind_phone_verify_view)
    EditText mBindPhoneVerifyView;
    @BindView(R.id.m_bind_phone_send_verify_text)
    TextView mBindPhoneSendVerifyText;
    @BindView(R.id.m_bind_phone_confirm_text)
    TextView mBindPhoneConfirmText;
    @BindView(R.id.m_bind_phone_first_lp)
    LinearLayout mBindPhoneFirstLp;

    @Inject

    BindPhonePresenter mPresenter;
    private int mBindType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_bind_phone);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));

        if (getIntent() != null && getIntent().getExtras() != null) {
            mBindType = getIntent().getIntExtra(Constants.INTENT_BIND_MOBILE, 0);
        }

        this.initView();
    }

    private void initView() {
        ButterKnife.bind(this);
        DaggerBindPhonePresenterComponent.builder()
                .bindPhonePresenterModule(new BindPhonePresenterModule(this)).build().inject(this);
    }

    @Override
    public void getResetSendCode(boolean isOnclick, int timeCount) {
        if (isOnclick) {
            BindPhoneActivity.this.mBindPhoneSendVerifyText.setText(getResources().getString(R.string.string_forget_password_resend_verify_alert));
            BindPhoneActivity.this.mBindPhoneSendVerifyText.setClickable(true);
        } else {
            BindPhoneActivity.this.mBindPhoneSendVerifyText
                    .setText((timeCount) + getResources().getString(R.string.string_forget_password_resend_verify_hint));
            BindPhoneActivity.this.mBindPhoneSendVerifyText.setClickable(false);
        }
    }

    @Override
    public void showSuccessMsg() {

        if (mBindType == 1) {
            CommonToast.getInstance("绑定手机号成功").show();
        } else {
            CommonToast.getInstance(getResources().getString(R.string.string_my_profile_change_phone_success_alert), Toast.LENGTH_SHORT).show();

        }

    }

    @OnClick({R.id.m_bind_phone_close_btn, R.id.m_bind_phone_send_verify_text, R.id.m_bind_phone_confirm_text})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {
            case R.id.m_bind_phone_close_btn:
                finish();
                break;
            case R.id.m_bind_phone_send_verify_text:
                String mPhone = mBindPhoneAccountView.getText().toString();
                if (Utils.isStringEmpty(mPhone)) {
                    CommonToast.getInstance(getResources().getString(R.string.string_my_profile_change_phone_empty_alert)).show();
                    return;
                }
                if (Utils.isStringEquals(mPhone, UserInfoData.getInstance().getUserInfoItem().mobile)) {
                    CommonToast.getInstance("当前手机号已存在").show();
                    return;
                }
                mPresenter.sendCode(mPhone);
                break;
            case R.id.m_bind_phone_confirm_text:
                String phone = mBindPhoneAccountView.getText().toString();
                if (Utils.isStringEmpty(phone)) {
                    CommonToast.getInstance(getResources().getString(R.string.string_my_profile_change_phone_empty_alert)).show();
                    return;
                }
                String mCode = mBindPhoneVerifyView.getText().toString();
                if (Utils.isStringEmpty(mCode)) {
                    CommonToast.getInstance(getResources().getString(R.string.string_my_profile_change_verify_empty_alert)).show();
                    return;
                }
                mPresenter.changePhone(phone, mCode);
                break;
        }
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }
}
