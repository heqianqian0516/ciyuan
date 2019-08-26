package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class GetAllWantListApiParameter extends ApiParameter {
    private final String postUuid;
    private final String placeId;

    public GetAllWantListApiParameter(String postUuid, String placeId) {
        this.postUuid = postUuid;
        this.placeId = placeId;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("postUuid", new ApiParamMap.ParamData(this.postUuid));
        map.put("placeId", new ApiParamMap.ParamData(this.placeId));
        return map;
    }
}
