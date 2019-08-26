package com.ciyuanplus.mobile.manager;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.ciyuanplus.mobile.activity.chat.FeedBackActivity;
import com.ciyuanplus.mobile.activity.mine.MineWelfareActivity;
import com.ciyuanplus.mobile.module.popup.share_invite_popup.ShareInvitePopupActivity;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.CustomDialog;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by Alen on 2017/11/20.
 */

class JsWebViewInterface {
    //原生 通知 JS页面  分享成功的方法。 非常不准确，十分不可靠，
    private static String JS_METHOD_INVITE_POP_UP = "shareCallBack";
    private static String JS_METHOD_SET_USER_ID_AND_GIFT_INFO = "setUserUuidAndUserGiftId";

    private final Activity mContent;
    private final WebView mWebview;

    public JsWebViewInterface(Activity mContent, WebView mWebview) {
        this.mContent = mContent;
        this.mWebview = mWebview;
    }

    //由于安全原因 targetSdkVersion>=17需要加 @JavascriptInterface
    //JS调用Android
    // 调用这个接口可以直接关闭当前的Activity
    @JavascriptInterface
    public void onBackImagePress() {

        mContent.runOnUiThread(() -> {
            CommonToast.getInstance("成功调用了 原生的接口，如果没有关闭页面是原生的bug").show();
            mContent.finish();
        });
    }


    // 测试方法
    @JavascriptInterface
    public void startFunction() {

        mContent.runOnUiThread(() -> CommonToast.getInstance("成功调用了 原生的接口").show());
    }

    // 弹出分享 邀请好友页面
    @JavascriptInterface
    public void popInviteShareActivity(final String urls, final String icon, final String title, final String desc) {

        mContent.runOnUiThread(() -> {
            Intent intent = new Intent(mContent, ShareInvitePopupActivity.class);
            intent.putExtra("url", urls);
            intent.putExtra("icon", icon);
            intent.putExtra("title", title);
            intent.putExtra("desc", desc);
            mContent.startActivityForResult(intent, Constants.REQUEST_CODE_POP_INVITE_SHARE_CODE);
        });
    }

    //联系客服的按钮
    @JavascriptInterface
    public void contactCustomerService() {
        mContent.runOnUiThread(() -> {
            Intent intent = new Intent(mContent, FeedBackActivity.class);
            mContent.startActivity(intent);
        });
    }

    //设置中间title的接口
    @JavascriptInterface
    public void setTopBarCenterText(final String text) {
        mContent.runOnUiThread(() -> {
            //mContent.commonTitleBar.setCenterText(text);
        });
    }

    //获取用户信息的接口
    // 使用Json 封装 用户名(nickname)  用户id(uuid)  头像(photo)  临时/正式用户(userState)
    @JavascriptInterface
    public void getCurrentUserInfo() {
        String nickname = UserInfoData.getInstance().getUserInfoItem().nickname;
        String uuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        String photo = UserInfoData.getInstance().getUserInfoItem().photo;
        String userState = LoginStateManager.isLogin() ? "1" : "2";
        String JS_METHOD_SET_CURRENT_USER_INFO = "setCurrentUserInfo";
        mWebview.loadUrl("javascript:" + JS_METHOD_SET_CURRENT_USER_INFO
                + "('" + nickname + "','" + uuid + "','" + photo + "','" + userState + "')");

    }

    // 获取用户uuid 和gift id
    @JavascriptInterface
    public void getUserUuidAndUserGiftId() {
        String uuid = UserInfoData.getInstance().getUserInfoItem().uuid;
//        mWebview.loadUrl("javascript:" + JS_METHOD_SET_USER_ID_AND_GIFT_INFO
//                + "('" + uuid + "','" + mParams + "')");
    }

    // 分享成功，检查是否要弹框
    @JavascriptInterface
    public void checkGift(String type, String alert) {
        CustomDialog.Builder builder = new CustomDialog.Builder(mContent);
        builder.setMessage(alert + "");
        if (Utils.isStringEquals(type, "1")) {
            builder.setPositiveButton("立刻查看", (dialog, which) -> {
                Intent intent = new Intent(mContent, MineWelfareActivity.class);
                mContent.startActivity(intent);
                dialog.dismiss();
            });
        }
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        CustomDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
