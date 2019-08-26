package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class BoundDetailAddressApiParameter extends ApiParameter {
    private final String userUuid;
    private final String communityUuid;
    private final String address;

    public BoundDetailAddressApiParameter(String address, String communityUuid) {
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.communityUuid = communityUuid;
        this.address = address;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("communityUuid", new ApiParamMap.ParamData(this.communityUuid));
        map.put("address", new ApiParamMap.ParamData(this.address));
        return map;
    }
}
