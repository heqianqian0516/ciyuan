package com.ciyuanplus.mobile.module.login;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.module.register.forget_password.ForgetPasswordActivity;
import com.ciyuanplus.mobile.module.webview.JsWebViewActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.CountDownButton;
import com.ciyuanplus.mobile.widget.LoadingDialog;
import com.umeng.socialize.UMShareAPI;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Alen on 2017/5/9.
 * 新版登录页面
 */
public class LoginActivity extends MyBaseActivity implements com.ciyuanplus.mobile.module.login.LoginContract.View {

    @BindView(R.id.m_login_account_view)
    EditText mLoginAccountView;
    @BindView(R.id.btn_get_sms_code)
    CountDownButton mSmsCodeButton;
    @BindView(R.id.et_code)
    EditText mSmsCode;
    @BindView(R.id.tv_login_protocol_cy)
    TextView loginProtocol;

    @Inject
    com.ciyuanplus.mobile.module.login.LoginPresenter mLoginPresenter;
    private Dialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        LoadingDialog.Builder builder = new LoadingDialog.Builder(this);
        builder.setMessage("加载中....");
        mLoadingDialog = builder.create();
        mLoadingDialog.setCanceledOnTouchOutside(false);


        DaggerLoginPresenterComponent.builder()
                .loginPresenterModule(new com.ciyuanplus.mobile.module.login.LoginPresenterModule(this)).build().inject(this);

        loginProtocol.setText(getClickableSpan());
        //设置该句使文本的超连接起作用
        loginProtocol.setAutoLinkMask(0);
        loginProtocol.setHighlightColor(Color.TRANSPARENT);
        loginProtocol.setMovementMethod(LinkMovementMethod.getInstance());
    }

    //设置超链接文字
    private SpannableString getClickableSpan() {
        SpannableString spanStr = new SpannableString("登录即代表同意次元PLUS用户协议");
        //设置下划线文字
        spanStr.setSpan(new UnderlineSpan(), 13, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置文字的单击事件
        spanStr.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View widget) {

                Intent intent = new Intent(mActivity, JsWebViewActivity.class);
                String url = ApiContant.WEB_DETAIL_VIEW_URL + "cyplus-share/userAgreement.html";
                intent.putExtra(Constants.INTENT_OPEN_URL, url);
                startActivity(intent);
            }
        }, 13, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        //设置文字的前景色
        spanStr.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_lavender)), 13, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        //设置下划线文字
//        spanStr.setSpan(new UnderlineSpan(), 21, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        //设置文字的单击事件
//        spanStr.setSpan(new ClickableSpan() {
//
//            @Override
//            public void onClick(View widget) {
//
//                startActivity(new Intent(mActivity, JsWebViewActivity.class));
//            }
//        }, 21, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        //设置文字的前景色
//        spanStr.setSpan(new ForegroundColorSpan(Color.BLUE), 21, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    @Override
    @OnClick({R.id.iv_login_back_image, R.id.tv_login, R.id.m_login_forget_password_text,
            R.id.iv_login_weichat, R.id.iv_login_qq, R.id.iv_login_weibo, R.id.btn_get_sms_code})
    public void onViewClicked(View view) {
        super.onViewClicked(view);

        String mobile = mLoginAccountView.getText().toString();
        String smsCode = mSmsCode.getText().toString();

        switch (view.getId()) {

            case R.id.iv_login_back_image:
                finish();
                break;
            case R.id.tv_login:


//                mLoginPresenter.requestLogin("18610646754", "111111");
                if (Utils.isStringEmpty(mobile)) {
                    CommonToast.getInstance(getResources().getString(R.string.string_login_account_empty_alert)).show();
                    return;
                }
                if (Utils.isStringEmpty(smsCode)) {
                    CommonToast.getInstance(getResources().getString(R.string.string_forget_password_verify_empty_alert)).show();
                    return;
                }
                mLoginPresenter.requestLogin(mobile, smsCode);
                break;

            case R.id.m_login_forget_password_text:
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                LoginActivity.this.startActivity(intent);
                break;
            case R.id.iv_login_weichat:
                mLoginPresenter.requestWeiChatLogin();
                break;
            case R.id.iv_login_qq:
                mLoginPresenter.requestQQLogin();
                break;
            case R.id.iv_login_weibo:
                mLoginPresenter.requestWeiBoLogin();
                break;
            case R.id.btn_get_sms_code:

//                mLoginPresenter.requestLogin("18610646754", "111111");
                if (Utils.isStringEmpty(mobile)) {
                    CommonToast.getInstance(getResources().getString(R.string.string_login_account_empty_alert)).show();
                    return;
                }
                mLoginPresenter.sendCode(mobile);

                break;
        }
    }

    @Override
    public void dismissDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();

    }

    @Override
    public void showLoadingDialog() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) mLoadingDialog.show();
    }

    @Override
    public void startCount() {

        mSmsCodeButton.startCount();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }
}
