package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class SignedSupplementApiParameter extends ApiParameter {
    private final String userUuid;
     private final String singedDate;
    public SignedSupplementApiParameter(String singedDate) {
        this.singedDate=singedDate;
        userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("singedDate",new ApiParamMap.ParamData(this.singedDate));
        return map;
    }

}
