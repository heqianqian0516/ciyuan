package com.ciyuanplus.mobile.manager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.ResponseData;
import com.ciyuanplus.mobile.net.parameter.CheckVersionApiParameter;
import com.ciyuanplus.mobile.net.response.CheckVersionResponse;
import com.ciyuanplus.mobile.utils.Constants;
import com.ciyuanplus.mobile.utils.Utils;
import com.ciyuanplus.mobile.widget.CommonToast;
import com.ciyuanplus.mobile.widget.CustomDialog;
import com.ciyuanplus.mobile.widget.LoadingDialog;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.listener.HttpListener;
import com.litesuits.http.request.AbstractRequest;
import com.litesuits.http.request.FileRequest;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by Alen on 2017/6/22.
 */

public class AppVersionManager {
    private static final DecimalFormat df = new DecimalFormat(".##");

    // 检查版本更新
    //
    public static void requestAppVersion(final Activity content) {
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_APP_VERSION_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new CheckVersionApiParameter().getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(App.mContext) {
            @Override
            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                CheckVersionResponse response1 = new CheckVersionResponse(s);
                if (Utils.isStringEquals(response1.mCode, ResponseData.CODE_OK) && response1.appVersion != null) {
                    if (response1.appVersion.lastForceVersionCode > Utils.getVersionCode()) { //强制更新
                        showForceUpdateDialog(response1.appVersion.url, content);
                    } else if (response1.appVersion.versionCode > Utils.getVersionCode()) { //有更新
                        int lastIgnoreVersion = SharedPreferencesManager.getInt(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_UPDATE_IGNORE_VERSION, 0);
                        if (response1.appVersion.versionCode > lastIgnoreVersion)
                            showUpdateDialog(response1.appVersion.versionCode, response1.appVersion.url, content);
                    }  //CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_get_app_version_newest_alert)).show();

                }  //                    CommonToast.getInstance(App.mContext.getResources().getString(R.string.string_get_app_version_error_alert)).show();

            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }

    // 版本更新dialog 强制更新
    private static void showForceUpdateDialog(final String url, final Activity content) {
        CustomDialog.Builder builder = new CustomDialog.Builder(content);
        builder.setMessage("发现新版本，需要更新后才能继续使用");
        builder.setPositiveButton("确定更新", (dialog, which) -> {
            dialog.dismiss();
            downLoadApk(content, url);
        });
        builder.setNegativeButton("退出应用", (dialog, which) -> {
            dialog.dismiss();
            MobclickAgent.onKillProcess(App.mContext); // Umeng 统计， 及时保存信息
            System.exit(0);
        });
        CustomDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    // 版本更新dialog 非强制更新
    public static void showUpdateDialog(final int versionCode, final String url, final Activity content) {
        CustomDialog.Builder builder = new CustomDialog.Builder(content);
        builder.setMessage("发现新版本，请选择更新");
        builder.setPositiveButton("确定更新", (dialog, which) -> {
            dialog.dismiss();
            downLoadApk(content, url);
        });
        builder.setNegativeButton("不，谢谢", (dialog, which) -> {
            dialog.dismiss();
            SharedPreferencesManager.putInt(Constants.SHARED_PREFERENCES_SET, Constants.SHARED_UPDATE_IGNORE_VERSION, versionCode);
        });
        CustomDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    // 下载apk
    private static void downLoadApk(Activity content, String url) {
        LoadingDialog.Builder builder = new LoadingDialog.Builder(content);
        builder.setMessage("下载中....");
        final LoadingDialog mLoadingDialog = builder.create();
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.show();


        FileRequest request = new FileRequest(Constants.IMAGE_LOAD_HEADER + url, CacheManager.getInstance().getCacheDirectory() + "new_apk.apk");
        request.setHttpListener(new HttpListener<File>(true, true, false) {
            @Override
            public void onSuccess(File file, Response<File> response) {
                super.onSuccess(file, response);
                CommonToast.getInstance("下载成功，正在进行安装").show();
                if (mLoadingDialog.isShowing()) mLoadingDialog.dismiss();
                installApk(file);
            }

            @Override
            public void onFailure(HttpException e, Response<File> response) {
                super.onFailure(e, response);
                CommonToast.getInstance("下载新版本失败").show();
                if (mLoadingDialog.isShowing()) mLoadingDialog.dismiss();
            }

            @Override
            public void onLoading(AbstractRequest<File> request, long total, long len) {
                super.onLoading(request, total, len);
                mLoadingDialog.mMessageText.setText("当前进度:" + df.format((((float) len / (float) total) * 100.0f)) + "%");
            }
        });
        LiteHttpManager.getInstance().executeAsync(request);
    }

    /**
     * 安装apk
     */
    private static void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        App.mContext.startActivity(intent);
    }

}
