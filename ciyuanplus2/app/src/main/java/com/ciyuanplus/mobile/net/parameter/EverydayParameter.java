package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by 小狼 on 2018/3/13.
 */

public class EverydayParameter extends ApiParameter {

    private final String userUuid;
    private final String userState;
    private final String pager;
    private final String pageSize;
    private final String lastId;
    private final String bizType;
    private final String searchValue;

    public EverydayParameter(String userUuid, String userState, String pager, String pageSize, String lastId, String bizType, String searchValue) {
        this.userUuid = userUuid;
        this.userState = userState;
        this.pager = pager;
        this.pageSize = pageSize;
        this.lastId = lastId;
        this.bizType = bizType;
        this.searchValue = searchValue;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        map.put("userState", new ApiParamMap.ParamData(this.userState));
        map.put("pager", new ApiParamMap.ParamData(this.pager));
        map.put("pageSize", new ApiParamMap.ParamData(this.pageSize));
        map.put("lastId", new ApiParamMap.ParamData(this.lastId));
        map.put("bizType", new ApiParamMap.ParamData(this.bizType));
        map.put("searchValue", new ApiParamMap.ParamData(this.searchValue));
        return map;
    }
}
