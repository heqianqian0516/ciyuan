package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2019/7/04.
 */
public class RequestRankngListApiParameter extends ApiParameter {
    private final String activityUuid;
    private final String userUuid;
    private final String pager;
    private final String pageSize;


    public RequestRankngListApiParameter(String activityUuid, String pager, String pageSize) {


        this.pager = pager;
        this.pageSize = pageSize;
        this.activityUuid=activityUuid;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("activityUuid",new ApiParamMap.ParamData(this.activityUuid));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("pager", new ApiParamMap.ParamData(this.pager));
        map.put("pageSize", new ApiParamMap.ParamData(this.pageSize));
        return map;
    }

}
