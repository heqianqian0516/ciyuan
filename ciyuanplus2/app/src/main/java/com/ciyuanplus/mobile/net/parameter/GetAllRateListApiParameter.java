package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class GetAllRateListApiParameter extends ApiParameter {
    private final String placeId;
    private final String pager;
    private final String pageSize;

    public GetAllRateListApiParameter(String placeId, String pager, String pageSize) {
        this.placeId = placeId;
        this.pager = pager;
        this.pageSize = pageSize;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("pager", new ApiParamMap.ParamData(this.pager));
        map.put("placeId", new ApiParamMap.ParamData(this.placeId));
        map.put("pageSize", new ApiParamMap.ParamData(this.pageSize));
        return map;
    }
}
