package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class RequestLiveHoodApiParameter extends ApiParameter {

    private final String pager;
    private final String pageSize;

    public RequestLiveHoodApiParameter(String pager, String pageSize) {
        this.pager = pager;
        this.pageSize = pageSize;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("pager", new ApiParamMap.ParamData(this.pager));
        map.put("pageSize", new ApiParamMap.ParamData(this.pageSize));
        return map;
    }
}
