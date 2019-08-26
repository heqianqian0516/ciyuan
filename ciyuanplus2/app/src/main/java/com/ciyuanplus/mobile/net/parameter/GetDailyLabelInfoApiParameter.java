package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class GetDailyLabelInfoApiParameter extends ApiParameter {

    private String communityUuid;

    public GetDailyLabelInfoApiParameter() {
        this.communityUuid = UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("communityUuid", new ApiParamMap.ParamData(this.communityUuid));
        return map;
    }
}
