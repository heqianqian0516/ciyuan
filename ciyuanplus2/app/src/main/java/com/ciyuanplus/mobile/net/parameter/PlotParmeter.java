package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by 小狼 on 2018/4/11.
 */

public class PlotParmeter extends ApiParameter {

    private final String communityUuid;
    private final String userUuid;
    private final String userState;
    private final String pager;
    private final String pageSize;
    private final String lastId;
    private final String bizType;

    public PlotParmeter(String communityUuid, String userUuid, String userState, String pager, String pageSize, String lastId, String bizType) {

        this.communityUuid = communityUuid;
        this.userUuid = userUuid;
        this.userState = userState;
        this.pager = pager;
        this.pageSize = pageSize;
        this.lastId = lastId;
        this.bizType = bizType;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("communityUuid", new ApiParamMap.ParamData(this.communityUuid));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("userState", new ApiParamMap.ParamData(this.userState));
        map.put("pager", new ApiParamMap.ParamData(this.pager));
        map.put("pageSize", new ApiParamMap.ParamData(this.pageSize));
        map.put("lastId", new ApiParamMap.ParamData(this.lastId));
        map.put("bizType", new ApiParamMap.ParamData(this.bizType));
        return map;
    }
}
