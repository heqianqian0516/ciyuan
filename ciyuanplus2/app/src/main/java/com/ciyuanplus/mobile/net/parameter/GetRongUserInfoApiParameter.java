package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class GetRongUserInfoApiParameter extends ApiParameter {
    private final String targetUserUuid;
    private final String userUuid;

    public GetRongUserInfoApiParameter(String userUuid) {
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.targetUserUuid = userUuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("targetUserUuid", new ApiParamMap.ParamData(this.targetUserUuid));
        return map;
    }

}
