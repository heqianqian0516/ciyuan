package com.ciyuanplus.mobile.manager;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

/**
 * Created by kk on 2018/4/28.
 */

class JsChangeWeatherInterface {

    private final Activity mContext;
    private final WebView m_js_webview;

    public JsChangeWeatherInterface(Activity context, WebView webView) {

        this.mContext = context;
        this.m_js_webview = webView;
    }

    //调用js
    @JavascriptInterface
    private void changeFullMode() {

        mContext.runOnUiThread(() -> m_js_webview.loadUrl("javascript:halfMode()"));
    }

    @JavascriptInterface
    private void changeHalfMode() {
        mContext.runOnUiThread(() -> m_js_webview.loadUrl("javascript:fullMode()"));
    }
}
