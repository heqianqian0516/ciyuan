package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class DeleteDetailAddressApiParameter extends ApiParameter {
    private final String userCommunityAddressUuid;
    private final String userUuid;

    public DeleteDetailAddressApiParameter(String communityId) {
        this.userCommunityAddressUuid = communityId;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("userCommunityAddressUuid", new ApiParamMap.ParamData(this.userCommunityAddressUuid));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;
    }

}
