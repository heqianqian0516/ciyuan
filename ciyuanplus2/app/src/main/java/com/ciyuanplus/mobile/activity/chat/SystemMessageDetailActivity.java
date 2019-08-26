package com.ciyuanplus.mobile.activity.chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.TextView;

import com.ciyuanplus.mobile.MyBaseActivity;
import com.ciyuanplus.mobile.R;
import com.ciyuanplus.mobile.image_select.utils.StatusBarCompat;
import com.ciyuanplus.mobile.inter.MyOnClickListener;
import com.ciyuanplus.mobile.manager.LoginStateManager;
import com.ciyuanplus.mobile.manager.SharedPreferencesManager;
import com.ciyuanplus.mobile.module.mine.my_order_detail.MyOrderDetailActivity;
import com.ciyuanplus.mobile.module.webview.JsWebViewActivity;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.parameter.DeleteSystemNoticeApiParameter;
import com.ciyuanplus.mobile.net.parameter.GetNoticeListApiParameter;
import com.ciyuanplus.mobile.net.parameter.GetSystemMessageDetailApiParameter;
import com.ciyuanplus.mobile.net.response.GetSystemMessageDetailResponse;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.TitleBarView;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.ciyuanplus.mobile.module.mine.my_order_detail.MyOrderDetailActivity.ORDER_UUID;

/**
 * Created by Alen on 2017/6/22.
 */

public class SystemMessageDetailActivity extends MyBaseActivity {
    @BindView(R.id.m_system_message_detail_title)
    TextView mSystemMessageDetailTitle;
    @BindView(R.id.m_system_message_detail_time)
    TextView mSystemMessageDetailTime;
    @BindView(R.id.m_system_message_detail_webview)
    WebView mSystemMessageDetailWebview;
    @BindView(R.id.title_bar)
    TitleBarView m_js_common_title;

    private String mUuid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_system_message_detail);
        this.mUuid = getIntent().getStringExtra(Constants.INTENT_SYSTEM_MESSAGE_ID);

        StatusBarCompat.compat(this, getResources().getColor(R.color.title));
        this.initView();
        this.requestSystemMessage();
        setSystemMessageRead();

    }

    private void initView() {
        Unbinder unbinder = ButterKnife.bind(this);

        m_js_common_title.setOnBackListener(new MyOnClickListener() {
            @Override
            public void performRealClick(View v) {
                onBackPressed();
            }
        });
        m_js_common_title.setTitle("系统消息");
        mSystemMessageDetailWebview.getSettings().setLoadWithOverviewMode(true);
        mSystemMessageDetailWebview.getSettings().setUseWideViewPort(true);
        mSystemMessageDetailWebview.getSettings().setJavaScriptEnabled(true);
        mSystemMessageDetailWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mSystemMessageDetailWebview.getSettings().setSupportMultipleWindows(true);
        mSystemMessageDetailWebview.getSettings().setTextSize(WebSettings.TextSize.NORMAL);

        WebView.setWebContentsDebuggingEnabled(true);
        mSystemMessageDetailWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http://") || url.startsWith("https://")) {

                } else {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                        return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                    }
                }
                //处理http和https开头的url
                view.loadUrl(url);
                return true;
            }
        });
        mSystemMessageDetailWebview.addJavascriptInterface(SystemMessageDetailActivity.this, "Android");

        mSystemMessageDetailWebview.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        mSystemMessageDetailWebview.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        mSystemMessageDetailWebview.getSettings().setAppCacheEnabled(true);//是否使用缓存
        mSystemMessageDetailWebview.getSettings().setDomStorageEnabled(true);//DOM Storage
        mSystemMessageDetailWebview.getSettings().setDatabaseEnabled(true);
        //contentWebView.getSettings().setDatabasePath(CacheManager.getInstance().getCacheDirectory());
        mSystemMessageDetailWebview.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }

    // 获取用户协议
    private void requestSystemMessage() {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_SYSTEM_MESSAGE_DETAIL_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new GetSystemMessageDetailApiParameter(mUuid).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(this) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                GetSystemMessageDetailResponse response1 = new GetSystemMessageDetailResponse(s);
                if (!Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK)) {
                    CommonToast.getInstance(response1.mMsg).show();
                } else {
                    mSystemMessageDetailTitle.setText(response1.systemMessageDetailItem.title);
                    mSystemMessageDetailTime.setText(Utils.getFormattedTimeString(response1.systemMessageDetailItem.createTime));
                    mSystemMessageDetailWebview.loadDataWithBaseURL(null, ApiContant.WEB_HEAD_STSRT + response1.systemMessageDetailItem.contentText + ApiContant.WEB_HEAD_END, "text/html", "utf-8", null);
                }
            }

        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    // 获取用户协议
    private void setSystemMessageRead() {
        if (!LoginStateManager.isLogin()) {
            StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                    + ApiContant.REQUEST_TEMP_SYSTEM_NOTICE_READ_URL);
            postRequest.setMethod(HttpMethods.Post);
            postRequest.setHttpBody(new DeleteSystemNoticeApiParameter(mUuid, GetNoticeListApiParameter.TYPE_SYSTEM).getRequestBody());
            LiteHttpManager.getInstance().executeAsync(postRequest);
        } else {
            StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                    + ApiContant.REQUEST_SYSTEM_NOTICE_READ_URL);
            postRequest.setMethod(HttpMethods.Post);
            postRequest.setHttpBody(new DeleteSystemNoticeApiParameter(mUuid, GetNoticeListApiParameter.TYPE_SYSTEM).getRequestBody());
            String sessionKey = SharedPreferencesManager.getString(
                    Constants.SHARED_PREFERENCES_SET, Constants.SHARED_PREFERENCES_LOGIN_USER_SESSION_KEY, "");
            if (!Utils.isStringEmpty(sessionKey)) postRequest.addHeader("authToken", sessionKey);
            LiteHttpManager.getInstance().executeAsync(postRequest);

        }
    }

    //联系客服的按钮
    @JavascriptInterface
    public void openGiftDetail(final int giftId, final String url) {
        runOnUiThread(() -> {
            Intent intent = new Intent(SystemMessageDetailActivity.this, JsWebViewActivity.class);
            intent.putExtra(Constants.INTENT_OPEN_URL, url);
            intent.putExtra(Constants.INTENT_JS_WEB_VIEW_PARAM, giftId + "");
            startActivity(intent);
        });
    }

    @JavascriptInterface
    public void gotoOrderDetail(String mUuid) {
        runOnUiThread(() -> {
            Intent intent = new Intent(SystemMessageDetailActivity.this, MyOrderDetailActivity.class);
            intent.putExtra(ORDER_UUID, mUuid);
            startActivity(intent);
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destoryWebView();
    }

    private void destoryWebView() {
        if (mSystemMessageDetailWebview != null) {
            mSystemMessageDetailWebview.clearHistory();
            mSystemMessageDetailWebview.clearCache(true);
            mSystemMessageDetailWebview.loadUrl("about:blank"); // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now
            mSystemMessageDetailWebview.freeMemory();
            mSystemMessageDetailWebview.pauseTimers();
            mSystemMessageDetailWebview = null; // Note that m_js_webview.destroy() and m_js_webview = null do the exact same thing
        }
    }
}
