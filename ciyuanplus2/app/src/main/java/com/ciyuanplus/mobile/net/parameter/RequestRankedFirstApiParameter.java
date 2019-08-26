package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2019/7/04.
 */
public class RequestRankedFirstApiParameter extends ApiParameter {
    private final String activityUuid;
    private final String userUuid;



    public RequestRankedFirstApiParameter(String activityUuid) {



        this.activityUuid=activityUuid;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("activityUuid",new ApiParamMap.ParamData(this.activityUuid));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));

        return map;
    }

}
