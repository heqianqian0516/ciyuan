package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2019/7/04.
 */
public class RequestParticipationDetailApiParameter extends ApiParameter {
    private final String uuid;
    private final String userUuid;



    public RequestParticipationDetailApiParameter(String uuid) {
        this.uuid=uuid;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("uuid",new ApiParamMap.ParamData(this.uuid));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));

        return map;
    }

}
