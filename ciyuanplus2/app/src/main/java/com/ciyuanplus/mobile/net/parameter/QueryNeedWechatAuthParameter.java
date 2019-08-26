package com.ciyuanplus.mobile.net.parameter;

import com.ciyuanplus.mobile.manager.UserInfoData;
import com.ciyuanplus.mobile.net.ApiParamMap;
import com.ciyuanplus.mobile.net.ApiParameter;

/**
 * 查询是否需要微信授权
 * Created by kk on 2018/5/2.
 */

public class QueryNeedWechatAuthParameter extends ApiParameter {

    private String userUuid;

    public QueryNeedWechatAuthParameter() {
        this.userUuid = UserInfoData.getInstance().getUserInfoItem().uuid;
    }

    @Override
    public ApiParamMap buildExtraParameter() {
        ApiParamMap map = new ApiParamMap();
        map.put("userUuid", new ApiParamMap.ParamData(this.userUuid));
        return map;
    }
}
