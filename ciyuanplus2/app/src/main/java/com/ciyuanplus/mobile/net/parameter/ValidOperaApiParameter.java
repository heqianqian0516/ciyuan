package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * Created by Alen on 2017/2/11.
 * <p>
 * 喜欢   踩   取消喜欢   取消踩
 */
public class ValidOperaApiParameter extends ApiParameter {
    private final String wikiUuid;
    private final String userUuid;

    public ValidOperaApiParameter(String wikiUuid) {
        this.wikiUuid = wikiUuid;
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("wikiUuid", new ApiParamMap.ParamData(this.wikiUuid));
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;
    }

}
