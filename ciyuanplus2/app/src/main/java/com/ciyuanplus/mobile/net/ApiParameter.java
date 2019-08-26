package com.ciyuanplus.mobile.net;


import androidx.annotation.Nullable;

import com.ciyuanplus.mobile.utils.Utils;
import com.litesuits.http.request.content.multi.MultipartBody;
import com.litesuits.http.request.content.multi.StringPart;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alen
 * @date 2017/2/11
 * <p>
 * 请求接口的父类
 */
public class ApiParameter {

    private String result = "";

    @Nullable
    protected ApiParamMap buildExtraParameter() {
        return new ApiParamMap();
    }

    public MultipartBody getRequestBody() {
        ApiParamMap apiParamMap = buildExtraParameter();
        apiParamMap.put("os", new ApiParamMap.ParamData("android"));
        apiParamMap.put("appVersion", new ApiParamMap.ParamData(Utils.getVersion()));
        apiParamMap.put("versionCode", new ApiParamMap.ParamData(Utils.getVersionCode() + ""));
        apiParamMap.put("channel", new ApiParamMap.ParamData(Utils.getChannelName()));
        apiParamMap.put("osVersion", new ApiParamMap.ParamData(android.os.Build.VERSION.SDK_INT + ""));


        MultipartBody body = new MultipartBody();
        List<String> lists = new ArrayList<>(apiParamMap.keySet());
        for (String key : lists) {

            if (null != key && apiParamMap.containsKey(key) && null != apiParamMap.get(key)) {

                if (null == apiParamMap.get(key).value) {
                    //防止外面过来空对象  导致崩溃
                    apiParamMap.get(key).value = "";
                }
                body.addPart(new StringPart(key, apiParamMap.get(key).value));
                result = new StringBuilder().append(result).append(key).append(" = ").append(apiParamMap.get(key).value).append(" & ").toString();
            }
        }

        Logger.d(result);
        return body;
    }
}
