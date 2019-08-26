package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by 小狼 on 2018/3/22.
 */

public class ImageParameter extends ApiParameter {

    private final String showSection;

    public ImageParameter(String showSection) {
        this.showSection = showSection;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("showSection", new ApiParamMap.ParamData(this.showSection));
        return map;
    }
}
