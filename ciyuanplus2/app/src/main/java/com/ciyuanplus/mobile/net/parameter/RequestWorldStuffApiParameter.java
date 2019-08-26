package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class RequestWorldStuffApiParameter extends ApiParameter {
    private final String longitude;
    private final String latitude;
    private final String order;
    private final String pager;
    private final String pageSize;
    private String status;
    private String userUuid;
    private String communityUuid;

    public RequestWorldStuffApiParameter(String longitude, String latitude, String order, String status, String pager, String pageSize) {
        this.longitude = longitude;
        this.pager = pager;
        this.pageSize = pageSize;
        this.latitude = latitude;
        this.order = order;
        this.status = status;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
        this.communityUuid = UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("longitude", new ApiParamMap.ParamData(this.longitude));
        map.put("pager", new ApiParamMap.ParamData(this.pager));
        map.put("pageSize", new ApiParamMap.ParamData(this.pageSize));
        map.put("latitude", new ApiParamMap.ParamData(this.latitude));
        map.put("order", new ApiParamMap.ParamData(this.order));
        map.put("status", new ApiParamMap.ParamData(this.status));
        map.put("maxDistance", new ApiParamMap.ParamData("50000"));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("communityUuid", new ApiParamMap.ParamData(this.communityUuid));
        return map;
    }
}
