package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class RequestOrderDetailApiParameter extends ApiParameter {
    private final String userUuid;
    private final String orderUUID;

    public RequestOrderDetailApiParameter(String orderUUID) { //请求其他人

        userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.orderUUID = orderUUID;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("orderUuid", new ApiParamMap.ParamData(this.orderUUID));
        return map;
    }
}
