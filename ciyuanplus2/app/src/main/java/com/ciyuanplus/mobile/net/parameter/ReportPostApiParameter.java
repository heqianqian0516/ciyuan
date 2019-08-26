package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

import java.util.ArrayList;

/**
 * Created by Alen on 2017/2/11.
 */
public class ReportPostApiParameter extends ApiParameter {
    private final String bizType;
    private final String bizUuid;
    private String reportType = "";
    private final String contentText;
    private String imgs = "";
    private final String userUuid;

    public ReportPostApiParameter(String bizType, String bizUuid, ArrayList<String> reportType, String contentText, ArrayList<String> imgs) { //请求其他人
        this.bizUuid = bizUuid;
        this.bizType = bizType;
        this.contentText = contentText;
        userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        if (imgs != null && imgs.size() > 0) {
            for (int i = 0; i < imgs.size() - 1; i++) {
                this.imgs += "" + imgs.get(i) + ",";
            }
            this.imgs += "" + imgs.get(imgs.size() - 1) + "";
            this.imgs += "";
        }
        if (reportType != null && reportType.size() > 0) {
            for (int i = 0; i < reportType.size() - 1; i++) {
                this.reportType += "" + reportType.get(i) + ",";
            }
            this.reportType += "" + reportType.get(reportType.size() - 1) + "";
            this.reportType += "";
        }
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("bizType", new ApiParamMap.ParamData(this.bizType));
        map.put("bizUuid", new ApiParamMap.ParamData(this.bizUuid));
        map.put("reportType", new ApiParamMap.ParamData(this.reportType));
        map.put("contentText", new ApiParamMap.ParamData(this.contentText));
        map.put("imgs", new ApiParamMap.ParamData(this.imgs));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;
    }
}
