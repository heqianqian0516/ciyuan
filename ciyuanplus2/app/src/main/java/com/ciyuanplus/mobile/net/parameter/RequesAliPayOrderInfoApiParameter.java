package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class RequesAliPayOrderInfoApiParameter extends ApiParameter {
    private final String userUuid;
    private final String merId;
    private final String orderId;

    public RequesAliPayOrderInfoApiParameter(String merId, String orderId) { //请求其他人

        userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.merId = merId;
        this.orderId = orderId;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("merId", new ApiParamMap.ParamData(this.merId));
        map.put("orderId", new ApiParamMap.ParamData(this.orderId));
        return map;
    }
}
