package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;
import com.ciyuanplus.mobile.statistics.StatisticsManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alen on 2017/2/11.
 */
public class UploadErrorInfoApiParameter extends ApiParameter {
    private String errorMessage;
    private String etc;
    private String userUuid;
    private String time;
    private String version;
    private String os;
    private String phoneType;
    private String imei;
    private String systemVersion;
    private String screenWidth;
    private String screenHeight;
    private String dpi;

    public UploadErrorInfoApiParameter(String dot) {
        try {
            JSONObject jsonObject = new JSONObject(dot);
            this.errorMessage = jsonObject.optString("errorMessage");
            this.etc = jsonObject.optString("etc");
            this.userUuid = jsonObject.optString("userUuid");
            this.time = jsonObject.optString("time");
            this.version = jsonObject.optString("version");
            this.os = jsonObject.optString("os");
            this.phoneType = jsonObject.optString("phoneType");
            this.imei = jsonObject.optString("imei");
            this.systemVersion = jsonObject.optString("systemVersion");
            this.screenWidth = jsonObject.optString("screenWidth");
            this.screenHeight = jsonObject.optString("screenHeight");
            this.dpi = jsonObject.optString("dpi");
        } catch (JSONException e) {
            StatisticsManager.onErrorInfo(StatisticsManager.getStackMsg(e), "");

            e.printStackTrace();
        }

    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("errorMessage", new ApiParamMap.ParamData(this.errorMessage));
        map.put("etc", new ApiParamMap.ParamData(this.etc));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("time", new ApiParamMap.ParamData(this.time));
        map.put("version", new ApiParamMap.ParamData(this.version));
        map.put("os", new ApiParamMap.ParamData(this.os));
        map.put("phoneType", new ApiParamMap.ParamData(this.phoneType));
        map.put("imei", new ApiParamMap.ParamData(this.imei));
        map.put("systemVersion", new ApiParamMap.ParamData(this.systemVersion));
        map.put("screenWidth", new ApiParamMap.ParamData(this.screenWidth));
        map.put("screenHeight", new ApiParamMap.ParamData(this.screenHeight));
        map.put("dpi", new ApiParamMap.ParamData(this.dpi));
        return map;
    }
}
