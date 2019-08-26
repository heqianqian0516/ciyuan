package com.ciyuanplus.mobile.statistics;

import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.utils.SystemUtil;
import com.ciyuanplus.mobile.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Alen on 2017/7/4.
 */

class LogHandlerManager {

    public static void onEvent(String module, String option) {
        onEvent(module, option, null);
    }

    public static void onError(String errorMessage, String etc) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("errorMessage", errorMessage);
            jsonObject.put("etc", etc);
            jsonObject.put("time", new Date().getTime());
            jsonObject.put("version", Utils.getVersion());
            jsonObject.put("os", "android");
            jsonObject.put("phoneType", SystemUtil.getSystemModel());
            jsonObject.put("imei", SystemUtil.getIMEI(App.mContext));
            jsonObject.put("systemVersion", SystemUtil.getSystemVersion());
            jsonObject.put("screenWidth", Utils.getScreenWidth() + "");
            jsonObject.put("screenHeight", Utils.getScreenHeight() + "");
            jsonObject.put("dpi", Utils.getDispalyDensity() + "");
            if (UserInfoData.getInstance().getUserInfoItem() != null)
                jsonObject.put("userUuid", UserInfoData.getInstance().getUserInfoItem().uuid);
            else jsonObject.put("userUuid", "");
            requestError(jsonObject.toString());// 直接上传， 如果不成功再暂存

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 实时打点的接口
     *
     * @param module 定义的vaule值,必须传
     * @param option 定义的vaule值,必须传
     * @param etcs   定义的vaule值,必须传
     */

    public static void onEvent(String module, String option,
                               String etcs) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("module", module);
            jsonObject.put("op", option);
            jsonObject.put("etc", etcs);
            jsonObject.put("time", new Date().getTime());
            jsonObject.put("version", Utils.getVersion());
            jsonObject.put("os", "android");
            jsonObject.put("phoneType", SystemUtil.getSystemModel());
            jsonObject.put("imei", SystemUtil.getIMEI(App.mContext));
            jsonObject.put("systemVersion", SystemUtil.getSystemVersion());
            jsonObject.put("screenWidth", Utils.getScreenWidth() + "");
            jsonObject.put("screenHeight", Utils.getScreenHeight() + "");
            jsonObject.put("dpi", Utils.getDispalyDensity() + "");

            if (UserInfoData.getInstance().getUserInfoItem() != null)
                jsonObject.put("userUuid", UserInfoData.getInstance().getUserInfoItem().uuid);
            else jsonObject.put("userUuid", "");
            requestLog(jsonObject.toString(), 1);// 直接上传， 如果不成功再暂存
            MobclickAgent.onEvent(App.mContext, module, option);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    // 上传Log的接口   index 是当前重试的次数
    public static void requestLog(final String log, final int index) {
//        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
//                + ApiContant.REQUEST_UPLOAD_DOT_INFO_URL);
//        postRequest.setMethod(HttpMethods.Post);
//        postRequest.setHttpBody(new UploadLogInfoApiParameter(log).getRequestBody());
//        postRequest.setHttpListener(new MyHttpListener<String>(App.mContext) {
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                if (index >= 3) {
//                    new Thread(() -> {
//                        YrLogger.b("", log);// 存放在本地
//                        YrLogger.flush(null);
//                    }).start();
//                } else {
//                    int next = index + 1;
//                    requestLog(log, next);
//                }
//            }
//
//            @Override
//            public void onSuccess(String s, Response<String> response) {
//
//            }
//        });
//        LiteHttpManager.getInstance().executeAsync(postRequest);
    }


    private static void requestError(final String log) {
//        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
//                + ApiContant.REQUEST_UPLOAD_ERROR_INFO_URL);
//        postRequest.setMethod(HttpMethods.Post);
//        postRequest.setHttpBody(new UploadErrorInfoApiParameter(log).getRequestBody());
//        postRequest.setHttpListener(new MyHttpListener<String>(App.mContext) {
//            @Override
//            public void onFailure(HttpException e, Response<String> response) {
//                if (1 >= 3) {
//                    YrLogger.e("", log);// 存放在本地
//                    YrLogger.flush(null);
//                } else {
//                    int next = 1 + 1;
//                    requestLog(log, next);
//                }
//            }
//
//            @Override
//            public void onSuccess(String s, Response<String> response) {
//
//            }
//        });
//        LiteHttpManager.getInstance().executeAsync(postRequest);
    }
}
