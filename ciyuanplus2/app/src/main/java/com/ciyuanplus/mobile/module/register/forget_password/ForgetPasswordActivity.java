package com.ciyuanplus.mobile.module.register.forget_password;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
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

public class ForgetPasswordActivity extends MyBaseActivity implements ForgetPasswordContract.View {
    @Inject
 ForgetPasswordPresenter mPresenter;
    @BindView(R.id.m_forget_password_account_view)
    EditText mForgetPasswordAccountView;
    @BindView(R.id.m_forget_password_verify_view)
    EditText mForgetPasswordVerifyView;
    @BindView(R.id.m_forget_password_password_view)
    NoEmojiEditText mForgetPasswordPasswordView;
    @BindView(R.id.m_forget_password_send_verify_text)
    TextView mForgetPasswordSendVerifyText;
    @BindView(R.id.m_forget_password_confirm_text)
    TextView mForgetPasswordConfirmText;
    @BindView(R.id.m_forget_password_back_image)
    ImageView mBackImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_forget_password);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);
        //setTopAndBottom();
        mBackImage.setOnClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {
                onBackPressed();
            }
        });
        DaggerForgetPasswordPresenterComponent.builder()
                .forgetPasswordPresenterModule(new ForgetPasswordPresenterModule(this)).build().inject(this);

    }

//    private void setTopAndBottom() {
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.MATCH_PARENT, Utils.getSelfHeight(750, 459));
//        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        mForgetPasswordBottomImage.setLayoutParams(layoutParams);
//    }

    @OnClick({R.id.m_forget_password_send_verify_text, R.id.m_forget_password_confirm_text})
    public void onViewClicked(View view) {
        super.onViewClicked(view);
        switch (view.getId()) {
            case R.id.m_forget_password_send_verify_text:
                String phone = mForgetPasswordAccountView.getText().toString();
                if (Utils.isStringEmpty(phone)) {
                    CommonToast.getInstance(getResources().getString(R.string.string_forget_password_phone_empty_alert)).show();
                    return;
                }
                mPresenter.sendCode(phone);
                break;
            case R.id.m_forget_password_confirm_text:

                String phone1 = mForgetPasswordAccountView.getText().toString();
                String verify = mForgetPasswordVerifyView.getText().toString();
                String password = mForgetPasswordPasswordView.getText().toString();
                if (Utils.isStringEmpty(phone1)) {
                    CommonToast.getInstance(getResources().getString(R.string.string_forget_password_phone_empty_alert)).show();
                    return;
                }
                if (!Utils.isMobileNO(phone1)) {
                    CommonToast.getInstance(getResources().getString(R.string.string_forget_password_phone_unformat_alert)).show();
                    return;
                }
                if (Utils.isStringEmpty(verify)) {
                    CommonToast.getInstance(getResources().getString(R.string.string_forget_password_verify_empty_alert)).show();
                    return;
                }
                if (Utils.isStringEmpty(password)) {
                    CommonToast.getInstance(getResources().getString(R.string.string_forget_password_password_empty_alert)).show();
                    return;
                }
                if (!Utils.isValidPassword(password)) {
                    CommonToast.getInstance(getResources().getString(R.string.string_register_password_unformatted_hint)).show();
                    return;
                }
                mPresenter.forgetPassword(phone1, password, verify);

                break;
        }
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }

    @Override
    public void setVerifyTextState(String tt, boolean clickable) {
        mForgetPasswordSendVerifyText.setText(tt);
        mForgetPasswordSendVerifyText.setClickable(clickable);

    }

    @Override
    public void closeCurrentActivity() {
        finish();
    }
}
