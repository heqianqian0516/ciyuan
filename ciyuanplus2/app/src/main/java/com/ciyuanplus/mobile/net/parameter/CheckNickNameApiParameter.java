package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 */
public class CheckNickNameApiParameter extends ApiParameter {
    private final String nickname;

    public CheckNickNameApiParameter(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("nickname", new ApiParamMap.ParamData(this.nickname));
        return map;
    }
}
