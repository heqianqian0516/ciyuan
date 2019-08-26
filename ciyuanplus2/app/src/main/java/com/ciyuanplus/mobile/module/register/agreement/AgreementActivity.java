package com.ciyuanplus.mobile.module.register.agreement;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.widget.CommonTitleBar;
import com.tencent.smtt.sdk.WebView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Alen on 2017/5/11.
 * 用户协议页面
 */

public class AgreementActivity extends MyBaseActivity implements AgreementContract.View {
    @BindView(R.id.m_agreement_webview)
    WebView mAgreementWebview;
    @BindView(R.id.m_agreement_common_title)
    CommonTitleBar m_js_common_title;

    @Inject

    AgreementPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_agreenment);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();
        mPresenter.requestAgreementContract();
    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);

        DaggerAgreementPresenterComponent.builder()
                .agreementPresenterModule(new AgreementPresenterModule(this)).build().inject(this);

        m_js_common_title.setLeftClickListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {
                onBackPressed();
            }
        });
//        mAgreementWebview.setScrollbarFadingEnabled(false);

        mAgreementWebview.getSettings().setLoadWithOverviewMode(true);
        mAgreementWebview.getSettings().setUseWideViewPort(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destoryWebView();
    }


    @Override
    public WebView getWebView() {
        return mAgreementWebview;
    }

    private void destoryWebView() {
        if (mAgreementWebview != null) {
            mAgreementWebview.clearHistory();
            mAgreementWebview.clearCache(true);
            mAgreementWebview.loadUrl("about:blank"); // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now
            mAgreementWebview.freeMemory();
            mAgreementWebview.pauseTimers();
            mAgreementWebview = null; // Note that m_js_webview.destroy() and m_js_webview = null do the exact same thing
        }
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }
}
