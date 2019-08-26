package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class UnFollowOtherApiParameter extends ApiParameter {
    private final String userUuid;
    private final String toUserUuid;

    public UnFollowOtherApiParameter(String toUserUuid) { //请求其他人
        this.toUserUuid = toUserUuid;
        userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("toUserUuid", new ApiParamMap.ParamData(this.toUserUuid));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;
    }
}
