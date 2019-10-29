package com.ciyuanplus.mobile.module.video;


import com.blankj.utilcode.util.StringUtils;
import com.ciyuanplus.mobile.App;
import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiContant;
import com.ciyuanplus.mobile.net.LiteHttpManager;
import com.ciyuanplus.mobile.net.MyHttpListener;
import com.ciyuanplus.mobile.net.parameter.GetVideoListApiParameter;
import com.ciyuanplus.mobile.net.parameter.RequestOthersInfoApiParameter;
import com.litesuits.http.exception.HttpException;
import com.litesuits.http.request.StringRequest;
import com.litesuits.http.request.param.HttpMethods;
import com.litesuits.http.response.Response;

import java.util.HashMap;

public class VideoModel implements IVideoContract.IVideoModel {
    @Override
    public void getRecommendVideo(HashMap<String, String> params, IVideoCallback callback) {
        String userUuid = params.get("userUuid");
        String pager = params.get("pager");
        String pageSize = params.get("pageSize");
        StringRequest postRequest = new StringRequest(ApiContant.URL_HEAD
                + ApiContant.REQUEST_VIDEO_LIST_URL);
        postRequest.setMethod(HttpMethods.Post);
        postRequest.setHttpBody(new GetVideoListApiParameter(userUuid,  Integer.parseInt(pager),Integer.parseInt(pageSize)).getRequestBody());
        postRequest.setHttpListener(new MyHttpListener<String>(App.mContext) {
            @Override

            public void onSuccess(String s, Response<String> response) {
                super.onSuccess(s, response);
                if (callback != null) {
                    callback.success(s);
                }
            }

            @Override
            public void onFailure(HttpException e, Response<String> response) {
                super.onFailure(e, response);
                if (callback != null) {
                    callback.error(e.getMessage());
                }
            }
        });
        LiteHttpManager.getInstance().executeAsync(postRequest);
    }
}
