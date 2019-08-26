package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class AddPostBrowseCountApiParameter extends ApiParameter {
    private final String postUuid;

    public AddPostBrowseCountApiParameter(String postUuid) {
        this.postUuid = postUuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("postUuid", new ApiParamMap.ParamData(this.postUuid));
        return map;
    }

}
