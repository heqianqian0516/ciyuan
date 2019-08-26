package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class RequestMineStuffListApiParameter extends ApiParameter {
    private String communityUuid;
    private final String userUuid;
    private final String pager;
    private final String pageSize;
    private final String longitude;
    private final String latitude;
    private String meUserUuid;

    public RequestMineStuffListApiParameter(String userUuid, String longitude, String latitude, String pager, String pageSize, String meUserUuid) {
        this.userUuid = userUuid;
        this.pager = pager;
        this.pageSize = pageSize;
        this.longitude = longitude;
        this.latitude = latitude;
        this.communityUuid = UserInfoData.getInstance().getUserInfoItem().currentCommunityUuid;
        this.meUserUuid = meUserUuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("pager", new ApiParamMap.ParamData(this.pager));
        map.put("pageSize", new ApiParamMap.ParamData(this.pageSize));
        map.put("longitude", new ApiParamMap.ParamData(this.longitude));
        map.put("latitude", new ApiParamMap.ParamData(this.latitude));
        map.put("communityUuid", new ApiParamMap.ParamData(this.communityUuid));
        map.put("meUserUuid", new ApiParamMap.ParamData(this.meUserUuid));
        return map;
    }

}
