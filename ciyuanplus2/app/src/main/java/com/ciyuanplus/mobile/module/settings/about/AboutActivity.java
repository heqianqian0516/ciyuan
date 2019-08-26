package com.ciyuanplus.mobile.module.settings.about;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.TitleBarView;
import com.tencent.smtt.sdk.WebView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alen on 2017/5/18.
 * 关于我们 页面
 */

public class AboutActivity extends MyBaseActivity implements AboutContract.View {
    @BindView(R.id.m_about_logo)
    ImageView mAboutLogo;
    @BindView(R.id.m_about_version_text)
    TextView mAboutVersionText;
    @BindView(R.id.m_about_text)
    WebView mAboutText;
    @BindView(R.id.m_about_common_title)
    TitleBarView m_js_common_title;

    @Inject
    AboutPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_about);
        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();
        mPresenter.requestAbout();
    }

    private void initView() {
        ButterKnife.bind(this);
        DaggerAboutPresenterComponent.builder().aboutPresenterModule(new AboutPresenterModule(this))
                .build().inject(this);
        m_js_common_title.setOnBackListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {
                onBackPressed();
            }
        });
        m_js_common_title.setTitle("关于我们");

        mAboutVersionText.setText(getResources().getString(R.string.string_about_version_alert) + Utils.getVersion());
//        mAboutText.setScrollbarFadingEnabled(false);
        mAboutText.getSettings().setLoadWithOverviewMode(true);
        mAboutText.getSettings().setUseWideViewPort(true);
        mAboutText.setVerticalScrollBarEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destoryWebView();
    }

    private void destoryWebView() {
        if (mAboutText != null) {
            mAboutText.clearHistory();
            mAboutText.clearCache(true);
            mAboutText.loadUrl("about:blank"); // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now
            mAboutText.freeMemory();
            mAboutText.pauseTimers();
            mAboutText = null; // Note that m_js_webview.destroy() and m_js_webview = null do the exact same thing
        }
    }

    @Override
    public Context getDefaultContext() {
        return this;
    }

    @Override
    public WebView getAboutWebView() {
        return mAboutText;
    }
}
