package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class GetPlaceDetailApiParameter extends ApiParameter {
    private final String wikiId;

    public GetPlaceDetailApiParameter(String wikiId) {
        this.wikiId = wikiId;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("wikiId", new ApiParamMap.ParamData(this.wikiId));
        return map;
    }

}
