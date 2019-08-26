package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class RequestOrderConfirmApiParameter extends ApiParameter {
    private final String userUuid;
    private final String orderUUID;
    private final String logisticsInfo;

    public RequestOrderConfirmApiParameter(String orderUUID, String logisticsInfo) { //请求其他人

        userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.orderUUID = orderUUID;
        this.logisticsInfo = logisticsInfo;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("orderUuid", new ApiParamMap.ParamData(this.orderUUID));
        map.put("logisticsInfo", new ApiParamMap.ParamData(this.logisticsInfo));
        return map;
    }
}
