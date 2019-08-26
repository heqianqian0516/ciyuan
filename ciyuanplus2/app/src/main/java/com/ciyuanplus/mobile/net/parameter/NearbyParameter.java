package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by 小狼 on 2018/3/13.
 */

public class NearbyParameter extends ApiParameter {

    private final String communityUuid;
    private final String userUuid;
    private final int bizType;
    private final int pager;
    private final int pageSize;
    private final int lastId;
    private final String searchValue;

    public NearbyParameter(String communityUuid, String userUuid, int bizType, int pager, int pageSize, int lastId, String searchValue) {
        this.communityUuid = communityUuid;
        this.userUuid = userUuid;
        this.bizType = bizType;
        this.pager = pager;
        this.pageSize = pageSize;
        this.lastId = lastId;
        this.searchValue = searchValue;

    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("communityUuid", new ApiParamMap.ParamData(this.communityUuid));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("bizType", new ApiParamMap.ParamData(this.bizType + ""));
        map.put("pager", new ApiParamMap.ParamData(this.pager + ""));
        map.put("pageSize", new ApiParamMap.ParamData(this.pageSize + ""));
        map.put("lastId", new ApiParamMap.ParamData(this.lastId + ""));
        map.put("searchValue", new ApiParamMap.ParamData(this.searchValue));
        return map;
    }
}
