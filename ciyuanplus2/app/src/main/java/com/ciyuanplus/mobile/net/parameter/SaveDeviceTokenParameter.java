package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class SaveDeviceTokenParameter extends ApiParameter {
    private final String deviceToken;
    private final String deviceType;
    private final String userUuid;

    public SaveDeviceTokenParameter(String deviceToken) {
        this.deviceToken = deviceToken;
        this.deviceType = "20";
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("deviceToken", new ApiParamMap.ParamData(this.deviceToken));
        map.put("deviceType", new ApiParamMap.ParamData(this.deviceType));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;
    }
}
